package com.github.swankdave.fastest;

import com.github.swankdave.fastest.parser.ShorTestBaseListener;
import com.github.swankdave.fastest.parser.ShorTestParser;
import javaslang.Tuple2;
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
    private final List<String> parameterSetNameList; //needed because we need to know the order in which they were declared
    private final Map<String,Map<String,String>> parameterSets;
    private String currentParametersetName;
    private Map<String,String> currentParameterset;
    private boolean divertRulesToParameterSet = false;
    private List<TestConfig> divertedRules = new ArrayList<>();

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

    public Map<String, Map<String, String>> getParameterSets() {
        return parameterSets;
    }

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
        parameterSetNameList = new ArrayList<>();
        parameterSets = new HashMap<>();
        currentParametersetName = "";
        currentParameterset = new HashMap<>();
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
                            classScope.getClassName().trim() + (config.getConstructor().isBlank() ? "()" : config.getConstructor()), classScope.languageConfig));
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
        if (divertRulesToParameterSet)
            divertedRules.addAll(tests);
        else
            testList.addAll(tests.stream().map(TestInstance::new).toList());
        ActiveSets.clear();
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
        if (preCode && (getFUllText(ctx).trim().startsWith(classScope.languageConfig.getCommentString()))){
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
        this.preCode = false;
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
        config.setError(ctx.getText());
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
            var mapMembers = ctx.parameterList().statement();
            if (!ActiveSets.containsKey(getFUllText(ctx.set_keyword())))
                ActiveSets.put(getFUllText(ctx.set_keyword()),new HashMap<>());
            var map = ActiveSets.get(getFUllText(ctx.set_keyword()));
            String setDefinition = getFUllText(ctx);
            if (!map.containsKey(setDefinition))
                map.put(setDefinition,new ArrayList<>());
            var list = map.get(setDefinition);
            list.clear();
            list.addAll(mapMembers.stream().map(this::getFUllText).toList());
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
        config.setResult(getFUllText(ctx.programming_contents()));
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
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     *
     * @param ctx
     */
    @Override
    public void enterParameter_set(ShorTestParser.Parameter_setContext ctx) {
        var identifier = ctx.getChild(ShorTestParser.IdentifierContext.class,0);
        if (identifier == null)
            currentParametersetName = "_parameter_"+parameterSets.size();
        else
            currentParametersetName = identifier.getText();
        if (!(parameterSets.containsKey(currentParametersetName)))
            parameterSets.put(currentParametersetName,new HashMap<>());
        currentParameterset = parameterSets.get(currentParametersetName);
        if (!parameterSetNameList.contains(currentParametersetName))
            parameterSetNameList.add(currentParametersetName);
    }

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     *
     * @param ctx
     */
    @Override
    public void enterTruth_list(ShorTestParser.Truth_listContext ctx) {
        divertRulesToParameterSet = true;
    }

    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     *
     * @param ctx
     */
    @Override
    public void exitTruth_list(ShorTestParser.Truth_listContext ctx) {
        addTestToList();
        List<Tuple2<TestConfig, List<String>>> testsAndPredicates = getTestsAndPredicates(parameterSetNameList, parameterSets, divertedRules);

        testsAndPredicates.forEach(testAndPredicate ->
                testAndPredicate._2.forEach(predicate -> {
            var test = new TestConfig(testAndPredicate._1);
            test.setPredicate(predicate);
            testList.add(new TestInstance(test));
        }));
        divertedRules = new LinkedList<>();
        divertRulesToParameterSet = false;
    }

    public static @NotNull List<Tuple2<TestConfig, List<String>>> getTestsAndPredicates(
            List<String> parameterSetNameList,
            Map<String, Map<String, String>> parameterSets,
            List<TestConfig> divertedRules) {

        List<List<String>> potentialMethodCalls = new ArrayList<>(); // a rolled out list of all parameter sets possible within the specified parametersets
        // a list of method calls, consisting of lists of parameter value names
        potentialMethodCalls.add(new ArrayList<>());
        for (var name : parameterSetNameList) {
            List<List<String>> newList= new ArrayList<>();
            for (var methodCall : potentialMethodCalls)
                for (var value : parameterSets.get(name).keySet()){
                    var newCall = new ArrayList<>(methodCall);
                    newCall.add(value);
                    newList.add(newCall);
                }
            potentialMethodCalls = newList;
        }

        Map<TestConfig, String> postPreAmbles = new HashMap<>();
        List<Tuple2<TestConfig, List<List<String>>>> callingMaps = new LinkedList<>(); //test predicates, by test, where each parameter is a list of legal named values for each parameter in call
        //A testConfig and a list of parameters, each consisting of a list of legal value names for that parameter for this rule
        for (var test: divertedRules) {
            List<List<String>> callList = new LinkedList<>();
            var broken = Util.breakRangeParameters(test.getPredicate().trim());
            postPreAmbles.put(test, broken._2);
            var params = broken._1;
            for (int i = 0; i < params.size(); i++)
                callList.add(Util.getSublistFromParameter(params.get(i), parameterSets.get(parameterSetNameList.get(i)).keySet().stream().toList()));
            callingMaps.add(new Tuple2<>(test, callList));
        }

        List<Tuple2<TestConfig, List<List<String>>>> methodCalls = new LinkedList<>(); //rolled out calls and the first rule they match
        //a TestConfig and a list of method calls, consisting of lists of parameter value names
        //for each test config
        for (var map: callingMaps){
            var calls = new Tuple2<TestConfig, List<List<String>>>(map._1, new ArrayList<>());
            List<List<String>> takenCalls = new LinkedList<>();
            //for each remaining function call from the lis of all possible calls to the function under test
            for (var call: potentialMethodCalls){
                var keep = true;
                //for each parameter in that call,
                for (int i=0; i<map._2.size(); i++)
                    keep = keep && (map._2.get(i).contains(call.get(i)));
                if (keep) {
                    calls._2.add(call);
                    takenCalls.add(call);
                }
            }
            //remove the calls this rule took, so that subsequent rules can't use it
            potentialMethodCalls.removeAll(takenCalls);
            methodCalls.add(calls);
        }

        List<Tuple2<TestConfig, List<String>>> testsAndPredicates = new LinkedList<>(); //a testConfig and a list of predicates to use in copies of testConfig
        for (var testConfig: methodCalls) {
            List<String> testPredicates = new ArrayList<>();
            for (var call : testConfig._2) {
                var predicate = new StringBuilder();
                for (int i = 0; i < call.size(); i++)
                    predicate.append(", ").append(parameterSets.get(parameterSetNameList.get(i)).get(call.get(i)));
                testPredicates.add("("+predicate.substring(2)+")"+postPreAmbles.get(testConfig._1));
            }
            testsAndPredicates.add(new Tuple2<>(testConfig._1, testPredicates));
        }
        return testsAndPredicates;
    }


    /**
     * {@inheritDoc}
     *
     * <p>The default implementation does nothing.</p>
     *
     * @param ctx
     */
    @Override
    public void enterKey_value_pair(ShorTestParser.Key_value_pairContext ctx) {
        if (ctx.parent != null && ctx.parent.parent != null)
            if (ctx.parent.parent instanceof ShorTestParser.Parameter_setContext){
                var identifier = ctx.getChild(ShorTestParser.IdentifierContext.class,0);
                var statement = ctx.getChild(ShorTestParser.StatementContext.class,0);
                if (identifier != null && statement != null)
                    currentParameterset.put(identifier.getText(),statement.getText());
                //else
                    //throw new ParseException("Malformed Key Par as part of parameter set: "+currentParametersetName, -1  );
            }

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
