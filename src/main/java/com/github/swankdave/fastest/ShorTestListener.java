package com.github.swankdave.fastest;

import com.github.swankdave.fastest.parser.ShorTestBaseListener;
import com.github.swankdave.fastest.parser.ShorTestParser;
import com.github.swankdave.fastest.parser.TestConfig;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.tree.ErrorNode;

import java.util.*;
import java.util.stream.Collectors;

public class ShorTestListener extends ShorTestBaseListener {
    private final ArrayList<TestInstance> testList;
    private int testIndex;
    private TestConfig config;
    private boolean postTest = false;
    private final HashMap<String,HashMap<String,ArrayList<String>>> sets;
    private boolean preCode=true;

    private String getFUllText(ParserRuleContext ctx){
        if (ctx.start == null || ctx.stop == null || ctx.start.getStartIndex() < 0 || ctx.stop.getStopIndex() < 0)
            return ctx.getText(); // Fallback
        return ctx.start.getInputStream().getText(Interval.of(ctx.start.getStartIndex(), ctx.stop.getStopIndex()));

    }

    public ShorTestListener(String testMethodName, boolean isStatic, int size) {
        config = new TestConfig(testMethodName, isStatic, size -1);
        testIndex = size;
        testList = new ArrayList<>();
        sets = new HashMap<>();
    }

    private void addTestToList() {
        //TODO: process test sets

        List<TestConfig> tests = new LinkedList<>();
        tests.add(new TestConfig(config));

        if (config.containsSet) {
            for (Map.Entry<String, HashMap<String, ArrayList<String>>> entry : sets.entrySet()) {
                HashMap<String, ArrayList<String>> setGroup = entry.getValue();
                tests = tests.stream().flatMap(cfg -> {
                    var rtn = new LinkedList<TestConfig>();
                    var setLiterals = setGroup.keySet().stream().filter(cfg::containsLiteral).collect(Collectors.toList());
                    if (!setLiterals.isEmpty()) {
                        for (int i = 0; i < setGroup.get(setLiterals.get(0)).size(); i++) {
                            var newTest = new TestConfig(cfg);
                            newTest.testIndex = testIndex+i;
                            int finalI = i;
                            setLiterals.forEach(literal -> newTest.expand(literal, setGroup.get(literal).get(finalI)));
                            rtn.add(newTest);
                        }
                    } else
                        rtn.add(cfg);
                    return rtn.stream();
                }).collect(Collectors.toList());
            }
            preCode=true;
        }

        testList.addAll(tests.stream().map(TestInstance::new).collect(Collectors.toList()));
        config.invalidateTest();
        testIndex += 1;
    }

    public ArrayList<TestInstance> getTestList(){
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
            if (!config.testDoc.isBlank())
                config.testDoc += "\n";
            config.testDoc += getFUllText(ctx);
        }else {
            preCode = false;
            if (!postTest) {
                if (!config.preamble.isBlank())
                    config.preamble += "\n";
                config.preamble += getFUllText(ctx);
            } else {
                if (!config.postTest.isBlank())
                    config.postTest += "\n";
                config.postTest += getFUllText(ctx);
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
        config.testIndex = testIndex;
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
     * Enter a parse tree produced by {@link ShorTestParser#test_name()}.
     *
     * @param ctx the parse tree
     */
    public void enterTest_name(ShorTestParser.Test_nameContext ctx) {
        super.enterTest_name(ctx);
        config.testName = getFUllText(ctx);
    }

    /**
     * Enter a parse tree produced by {@link ShorTestParser#constructor}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void enterConstructor(ShorTestParser.ConstructorContext ctx) {
        config.constructor = getFUllText(ctx);
    }

    /**
     * Enter a parse tree produced by {@link ShorTestParser#predicate}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void enterPredicate(ShorTestParser.PredicateContext ctx) {
        config.predicate = getFUllText(ctx);
    }

    /**
     * Enter a parse tree produced by {@link ShorTestParser#result}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void enterResult(ShorTestParser.ResultContext ctx) {
        config.result = getFUllText(ctx);
    }

    /**
     * Enter a parse tree produced by {@link ShorTestParser#error_text}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void enterError_text(ShorTestParser.Error_textContext ctx) {
        config.error = ctx.anything_but_newline().stream().map(this::getFUllText).collect(Collectors.joining(""));
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
            ShorTestParser.ParameterListContext parameterList = ctx.ballanced_parenthesis_statement().parameterList();
            String setDefinition = getFUllText(ctx);
            var id = getSetId(getFUllText(ctx.set_keyword()));
            if (!sets.containsKey(id))
                sets.put(id,new HashMap<>());
            var map = sets.get(id);
            if (!map.containsKey(setDefinition))
                map.put(setDefinition,new ArrayList<>());
            var list = map.get(setDefinition);
            list.clear();
            list.add(getFUllText(parameterList));
            list.addAll(parameterList.parameter().stream().map(this::getFUllText).collect(Collectors.toList()));
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
        config.result = getFUllText(ctx.ballanced_parenthesis_statement().parameterList());
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
        postTest = false;
    }

    /**
     * Enter a parse tree produced by {@link ShorTestParser#setup_reset}.
     *
     * @param ctx the parse tree
     */
    @Override
    public void enterSetup_reset(ShorTestParser.Setup_resetContext ctx) {
        if (config.isValid())
            addTestToList();
        config.invalidatePreTest();
        config = new TestConfig(config.testMethodName, config.isStatic, config.testIndex);
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

    }
}
