package com.github.swankdave.fastest;

import com.github.swankdave.fastest.parser.ShorTestBaseListener;
import com.github.swankdave.fastest.parser.ShorTestParser;
import javaslang.Tuple2;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ShorTestListener extends ShorTestBaseListener {
    private final ArrayList<TestInstance> testList;
    private final HashMap<String, Integer> nameMap;
    private final ClassScope classScope;
    private final MethodScope methodScope;
    private final String fragmentPrefix;
    private TestConfig config;
    private boolean postTest = false;
    private final List<String> parameterSetNameList; //needed because we need to know the order in which they were declared
    private final Map<String,Map<String,String>> parameterSets;
    private String currentParametersetName;
    private Map<String,String> currentParameterset;
    private boolean divertRulesToParameterSet = false;
    private List<TestConfig> divertedRules = new ArrayList<>();



    private final SetCollection activeSets;
    private boolean preCode=true;

    public Map<String, Map<String, String>> getParameterSets() {
        return parameterSets;
    }


    /**
     * Constructs a new instance of the ShorTestListener class.
     *
     * @param classScope The class scope used in the ShorTestListener.
     * @param methodScope The method scope used in the ShorTestListener.
     * @param nameMap The name map used in the ShorTestListener.
     */
    public ShorTestListener(ClassScope classScope, MethodScope methodScope, HashMap<String, Integer> nameMap) {
        this.classScope = classScope;
        this.methodScope = methodScope;
        this.nameMap = nameMap;
        config = new TestConfig(methodScope);
        testList = new ArrayList<>();
        activeSets = new SetCollection();
        parameterSetNameList = new ArrayList<>();
        parameterSets = new HashMap<>();
        currentParametersetName = "";
        currentParameterset = new HashMap<>();
        fragmentPrefix = this.classScope.languageConfig.getKeywordPrefixForLanguage();
    }



    /**
     * Add a test to the list of tests.
     * This method expands references and sets in the test configuration before adding it to the list.
     */
    private void addTestToList() {

        if (config.getPreamble().isEmpty() && !config.isStatic()) 
            config.applyDefaultPreamble(classScope);

        config.expandTestWithFragments(classScope);
        
        config.format(classScope.languageConfig);
        List<TestConfig> tests = new LinkedList<>();
        tests.add(new TestConfig(config));
        
        if (config.containsSet)
            tests = activeSets
                    .expandSetsWithFragments(methodScope, classScope, fragmentPrefix)
                    .expandTestsWithSets(tests);

        if (divertRulesToParameterSet)
            divertedRules.addAll(tests);
        else
            testList.addAll(tests.stream().map(TestInstance::new).toList());
        activeSets.clear();
        config.invalidateTest();
        preCode=true; //reset our code reading context
    }





    /**
     * Retrieves the list of test instances.
     *
     * This method iterates through each test instance in the testList, and performs
     * operations that involve the test configuration such as setting the test index
     * and incrementing the test name count. The modified test instances are then
     * returned as an ArrayList<TestInstance>.
     *
     * @return The list of test instances
     */
    public ArrayList<TestInstance> getTestList(){
        testList.forEach(test -> {
            String testName = test.config.getTestName();
            int indexValue = nameMap.getOrDefault(testName, 0);
            test.config.testIndex = indexValue;
            nameMap.put(testName, indexValue + 1);
        });

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
        if (preCode && (Util.getFUllText(ctx).trim().startsWith(classScope.languageConfig.getCommentString()))){
            if (!config.getTestDoc().isBlank())
                config.setTestDoc(config.getTestDoc() + "\n");
            config.setTestDoc(config.getTestDoc() + Util.getFUllText(ctx));
        }else {
            preCode = false;
            if (!postTest) {
                if (!config.getPreamble().isBlank())
                    config.setPreamble(config.getPreamble() + "\n");
                config.setPreamble(config.getPreamble() + Util.getFUllText(ctx));
                config.UsingDefaultPreamble=false;
            } else {
                if (!config.getPostTest().isBlank())
                    config.setPostTest(config.getPostTest() + "\n");
                config.setPostTest(config.getPostTest() + Util.getFUllText(ctx));
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
        config.setTestName(Util.getFUllText(ctx));
    }

    /**
     * Enter a parse tree produced by {@link ShorTestParser#constructor}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void enterConstructor(ShorTestParser.ConstructorContext ctx) {
        config.setConstructor(Util.getFUllText(ctx));
    }

    /**
     * Enter a parse tree produced by {@link ShorTestParser#predicate}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void enterPredicate(ShorTestParser.PredicateContext ctx) {
        config.isInverted = false;
        config.setPredicate(Util.getFUllText(ctx));
    }

    /**
     * Enter a parse tree produced by {@link ShorTestParser#result}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void enterResult(ShorTestParser.ResultContext ctx) {
        config.setResult(Util.getFUllText(ctx));
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
        activeSets.add(ctx);
    }



    /**
     * Enter a parse tree produced by {@link ShorTestParser#exception_method_statement}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void enterException_method_statement(ShorTestParser.Exception_method_statementContext ctx) {
        config.isException = true;
        config.setResult(Util.getFUllText(ctx.programming_contents()));
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
        if (config.UsingDefaultPreamble)
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
