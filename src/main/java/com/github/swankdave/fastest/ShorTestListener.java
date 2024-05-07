package com.github.swankdave.fastest;

import com.github.swankdave.fastest.parser.ShorTestBaseListener;
import com.github.swankdave.fastest.parser.ShorTestParser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

import static com.github.swankdave.fastest.Constants.ARTICLE_NAME;

public class ShorTestListener extends ShorTestBaseListener {
    private final ArrayList<TestInstance> testList;
    private final HashMap<String, Integer> nameMap;
    private final ClassScope classScope;
    private final MethodScope methodScope;
    private TestConfig config;
    private boolean postTest = false;

    //HashMap<SetIdentifier, HashMap<FullSetCommand,List<SetMember>>>
    //example:
    //      ActiveSets["SETA"]["(1,2,3)SETA"]={1,2,3};
    //      ActiveSets["SETA"]["(A,B,C)SETA"]={A,B,C};

    private final HashMap<String,HashMap<String,ArrayList<String>>> ActiveSets;
    private boolean preCode=true;
    /**
     * Indicates whether the default preamble is being used.
     *
     * The default preamble is constructors for datafragments and the article under test (if the function isn't static).
     * This variable determines whether the default preamble should be used or not.
     */
    private boolean UsingDefaultPreamble = true;

    private String getFUllText(ParserRuleContext ctx){
        if (ctx.start == null || ctx.stop == null || ctx.start.getStartIndex() < 0 || ctx.stop.getStopIndex() < 0)
            return ctx.getText(); // Fallback
        return ctx.start.getInputStream().getText(Interval.of(ctx.start.getStartIndex(), ctx.stop.getStopIndex()));

    }

    /**
     * @param classScope
     * @param methodScope
     * @test ()=>""
     */
    public ShorTestListener(ClassScope classScope, MethodScope methodScope, HashMap<String, Integer> nameMap) {
        this.classScope = classScope;
        this.methodScope = methodScope;
        this.nameMap = nameMap;
        config = new TestConfig(methodScope);
        testList = new ArrayList<>();
        ActiveSets = new HashMap<>();
    }

    /**
     * Inflates sets by replacing keywords with corresponding values from the testFragments map.
     *
     * @param ActiveSetsCopy  The original map of active sets.
     * @param testFragments   The map of test fragments containing keywords and their corresponding values.
     * @return The inflated map of sets with replaced keywords.
     */
    public Map<String, Map<String, List<String>>> inflateSets(Map<String, Map<String, List<String>>> ActiveSetsCopy, Map<String, String> testFragments){
        return ActiveSetsCopy.entrySet().stream().collect(
                Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().entrySet().stream().collect(
                                Collectors.toMap(
                                        f -> {
                                            var ref = new Object() {
                                                String key = f.getKey();
                                            };
                                            testFragments.forEach((from, to) -> ref.key = ref.key.replace(classScope.languageConfig.getKeywordPrefixForLanguage() + from, to));
                                            return ref.key;
                                        }, g ->
                                                g.getValue().stream().map(item -> {
                                                            var ref = new Object() {
                                                                String value = item;
                                                            };
                                                            testFragments.forEach((from, to) -> ref.value = ref.value.replace(classScope.languageConfig.getKeywordPrefixForLanguage() +from, to));
                                                            return ref.value;
                                                        }
                                                ).collect(Collectors.toList()))
                        )));
    }

    /**
     * Add a test to the list of tests.
     * This method expands references and sets in the test configuration before adding it to the list.
     */
    private void addTestToList() {

        if (config.getPreamble().isEmpty() && !config.isStatic()) {
            UsingDefaultPreamble = true;
            config.setPreamble(Util.getDeclarationFromDefinition(
                    ARTICLE_NAME,
                    classScope.languageConfig.getNewValueKeyword()+ " " +
                            classScope.getClassName() + (config.getConstructor().isBlank() ? "()" : config.getConstructor()), classScope.languageConfig));
        }

        // every expansion introduces the possibility of more references, so whenever we find a keyword,
        // we need to restart our search for keywords in the proper precedent ordering, so we keep expanding
        // references, and starting over in precedence search
        while (true) {
            if (methodScope.getTestFragments().keySet().stream().anyMatch(config::containsKeyword)) {
                methodScope.getTestFragments().forEach(config::expandKeyword);
                continue; //because method fragments can refer to each-other, we need to do this until it has no effect
            }

            if (methodScope.getTestData().keySet().stream().anyMatch(config::containsKeyword)) {
                methodScope.getTestData().forEach(config::dataExpand);
                continue;
            }

            if (classScope.getTestFragments().keySet().stream().anyMatch(config::containsKeyword)) {
                classScope.getTestFragments().forEach(config::expandKeyword);
                continue;
            }

            if (classScope.getTestData().keySet().stream().anyMatch(config::containsKeyword)) {
                classScope.getTestData().forEach(config::dataExpand);
                continue;
            }
            break;
        }

        config.format(classScope.languageConfig);
        List<TestConfig> tests = new LinkedList<>();
        tests.add(new TestConfig(config));

        //search for sets to contain fragments, and integrate them in to the set statements
        //SO THAT we will recognize set statements in test parts where this substitution has already
        //taken place
        if (config.containsSet) {
            var ActiveSetsCopy = getActiveSetsClone();
            while(true) {
                if (SetsContainKeyword(ActiveSetsCopy, methodScope.getTestFragments().keySet().stream().toList())){
                    ActiveSetsCopy = inflateSets(ActiveSetsCopy, methodScope.getTestFragments());
                    continue;
                }

                if (SetsContainKeyword(ActiveSetsCopy, methodScope.getTestData().keySet().stream().toList())){
                    ActiveSetsCopy = inflateSets(ActiveSetsCopy, methodScope.getTestData());
                    continue;
                }

                if (SetsContainKeyword(ActiveSetsCopy, classScope.getTestFragments().keySet().stream().toList())){
                    ActiveSetsCopy = inflateSets(ActiveSetsCopy, classScope.getTestFragments());
                    continue;
                }

                if (SetsContainKeyword(ActiveSetsCopy, classScope.getTestData().keySet().stream().toList())){
                    ActiveSetsCopy = inflateSets(ActiveSetsCopy, classScope.getTestData());
                    continue;
                }
                break;
            }

            //foreach map identifier (SET, SETA, SETB...)
            for (Map.Entry<String, Map<String, List<String>>> entry : ActiveSetsCopy.entrySet()) {
                String setId = entry.getKey();
                //list of set groups, consisting of the literal text of the set declaration, and a list of the set members/parameters broken up
                Map<String, List<String>> setGroup = entry.getValue();
                //for every defined test. (sets multiply tests)
                // results of this will result in every test returning itself, or the tests it became
                tests = tests.stream().flatMap(cfg -> {
                    var rtn = new LinkedList<TestConfig>();
                    //get the list of sets (under the current set identifier) that exist in the current test,
                    // by looking for each litteral definition of that set
                    //set litteral: (1,2,3)SETA
                    var setLiterals = (setGroup.keySet().stream().filter(cfg::containsLiteral)).toList();
                    //if we found any full text set definitions
                    if (!setLiterals.isEmpty()) {
                        //for each member of the first set for this set litteral
                        //for set litteral (1,2,3)SETA, the members are 1, 2 and 3, which will be substituted, in the text, for (1,2,3)SETA
                        for (int i = 0; i < setGroup.get(setLiterals.get(0)).size(); i++) {
                            //copy the current test
                            var newTest = new TestConfig(cfg);
                            //java doesn't like handing iteration variables directly to lambda's for some reason
                            int finalI = i;
                            //replace full text definition of all set literals with the fully expanded set value corresponding to the current set item index
                            setLiterals.forEach(literal -> newTest.expandLiteral(literal, setGroup.get(literal).get(finalI)));
                            //add new test to list of tests to be returned and flattened by flatmap
                            rtn.add(newTest);
                        }
                    } else
                        //this test does not need set expansion
                        rtn.add(cfg);
                    return rtn.stream();
                }).collect(Collectors.toList());
            }
        }
        testList.addAll(tests.stream().map(TestInstance::new).toList());
        config.invalidateTest();
        preCode=true; //reset our code reading context
    }

    private @NotNull Map<String, Map<String, List<String>>> getActiveSetsClone() {
        return ActiveSets.entrySet().stream().collect(
                Collectors.toMap(Map.Entry::getKey, e -> e.getValue().entrySet().stream().collect(
                        Collectors.toMap(Map.Entry::getKey, g -> g.getValue().stream().map(String::new).collect(Collectors.toList())))));
    }

    private boolean SetsContainKeyword(Map<String, Map<String, List<String>>> ActiveSetsCopy, List<String> fragmentNames) {
        return ActiveSetsCopy.entrySet().stream().anyMatch(
                superSet -> superSet.getValue().entrySet().stream().anyMatch(
                        set -> fragmentNames.stream().anyMatch(
                                fragmentId -> set.getKey().contains(classScope.languageConfig.getKeywordPrefixForLanguage() + fragmentId))));
    }

    public ArrayList<TestInstance> getTestList(){
        for (var test : testList){
            String testName = test.config.getTestName();
            int indexValue = nameMap.getOrDefault(testName, 0);
            test.config.testIndex = indexValue;
            nameMap.put(testName,indexValue + 1);
        }

        return testList;
    }
    /**
     * Exit a parse tree produced by {@link ShorTestParser#start}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void exitStart(ShorTestParser.StartContext ctx) {
        if (config.isValid())
            addTestToList();
    }

    /**
     * Enter a parse tree produced by {@link ShorTestParser#setup}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void enterSetup(ShorTestParser.SetupContext ctx) {
        if (preCode && (getFUllText(ctx).trim().startsWith("#"))||getFUllText(ctx).trim().startsWith("//")){
            if (!config.getTestDoc().isBlank())
                config.setTestDoc(config.getTestDoc() + "\n");
            config.setTestDoc(config.getTestDoc() + getFUllText(ctx));
        }else {
            preCode = false;
            if (!postTest) {
                if (!config.getPreamble().isBlank())
                    config.setPreamble(config.getPreamble() + "\n");
                config.setPreamble(config.getPreamble() + getFUllText(ctx));
                UsingDefaultPreamble=false;
            } else {
                if (!config.getPostTest().isBlank())
                    config.setPostTest(config.getPostTest() + "\n");
                config.setPostTest(config.getPostTest() + getFUllText(ctx));
            }
        }
    }

    /**
     * Enter a parse tree produced by {@link ShorTestParser#rule_()}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void enterRule(ShorTestParser.RuleContext ctx) {
        if (config.isValid())
            addTestToList();
    }

    /**
     * Exit a parse tree produced by {@link ShorTestParser#rule_()}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void exitRule(ShorTestParser.RuleContext ctx) {
         this.postTest = true;
    }

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     *
     * @param ctx
     */
    @Override
    public void enterRule_token(ShorTestParser.Rule_tokenContext ctx) {
        config.isInverted = ctx.getText().startsWith("!");
    }

    /**
     * Enter a parse tree produced by {@link ShorTestParser#test_name()}.
     *
     * @param ctx the parse tree
     */
    public void enterTest_name(ShorTestParser.Test_nameContext ctx) {
        super.enterTest_name(ctx);
        config.setTestName(getFUllText(ctx));
    }

    /**
     * Enter a parse tree produced by {@link ShorTestParser#constructor}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void enterConstructor(ShorTestParser.ConstructorContext ctx) {
        config.setConstructor(getFUllText(ctx));
    }

    /**
     * Enter a parse tree produced by {@link ShorTestParser#predicate}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void enterPredicate(ShorTestParser.PredicateContext ctx) {
        config.isInverted = false;
        config.setPredicate(getFUllText(ctx));
    }

    /**
     * Enter a parse tree produced by {@link ShorTestParser#result}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void enterResult(ShorTestParser.ResultContext ctx) {
        config.setResult(getFUllText(ctx));
    }

    /**
     * Enter a parse tree produced by {@link ShorTestParser#error_text}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void enterError_text(ShorTestParser.Error_textContext ctx) {
        config.setError(ctx.anything_but_newline().stream().map(this::getFUllText).collect(Collectors.joining("")));
    }

    private String getSetId(String keyword){
        if (keyword.length()==3|keyword.length()==7)
            return "default";
        if (keyword.length()==4)
            return keyword.substring(3);
        if (keyword.length()==8)
            return keyword.substring(7);
        throw new RuntimeException("Unknown set generator:" + keyword);
    }

    /**
     * Enter a parse tree produced by {@link ShorTestParser#set_method_statement}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void enterSet_method_statement(ShorTestParser.Set_method_statementContext ctx) {
        config.containsSet = true;
        if (ctx.set_keyword().SET_KEYWORD().getSymbol().getType() == ShorTestParser.SET_KEYWORD)
        {
            ShorTestParser.ParameterListContext mapMembers = ctx.ballanced_parenthesis_statement().parameterList();
            String setDefinition = getFUllText(ctx);
            var mapIdentifier = getSetId(getFUllText(ctx.set_keyword()));
            if (!ActiveSets.containsKey(mapIdentifier))
                ActiveSets.put(mapIdentifier,new HashMap<>());
            var map = ActiveSets.get(mapIdentifier);
            if (!map.containsKey(setDefinition))
                map.put(setDefinition,new ArrayList<>());
            var list = map.get(setDefinition);
            list.clear();
            list.addAll(mapMembers.statement().stream().map(this::getFUllText).toList());
        }

    }

    /**
     * Enter a parse tree produced by {@link ShorTestParser#exception_method_statement}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void enterException_method_statement(ShorTestParser.Exception_method_statementContext ctx) {
        config.isException = true;
        config.setResult(getFUllText(ctx.ballanced_parenthesis_statement().parameterList()));
    }

    /**
     * Enter a parse tree produced by {@link ShorTestParser#post_test_reset}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void enterPost_test_reset(ShorTestParser.Post_test_resetContext ctx) {
        if (config.isValid())
            addTestToList();
        config.invalidatePostTest();
        if (UsingDefaultPreamble)
            config.setPreamble("");
        postTest = false;
    }

    /**
     * Enter a parse tree produced by {@link ShorTestParser#pre_test_reset()}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void enterPre_test_reset(ShorTestParser.Pre_test_resetContext  ctx) {
        if (config.isValid())
            addTestToList();
        config.invalidatePreTest();
        config = new TestConfig(methodScope);
        postTest = false;
    }

    /**
     * Enter a parse tree produced by {@link ShorTestParser#set_keyword}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void enterSet_keyword(ShorTestParser.Set_keywordContext ctx) {
        config.containsSet = true;
    }


    /**
     * @param node node where error was found
     */
    @Override
    public void visitErrorNode(ErrorNode node) {
        System.out.println("Shortest Parse Error: " + node.getText());
    }
}
