package com.github.swankdave.fastest;

import com.github.swankdave.fastest.javadocparser.JavadocLexer;
import com.github.swankdave.fastest.javadocparser.JavadocParser;
import com.github.swankdave.fastest.parser.ShorTestLexer;
import com.github.swankdave.fastest.parser.ShorTestParser;
import com.intellij.lang.ASTNode;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The MethodScope class represents a scope in a test document that is specific to a method.
 * It extends the Scope class and provides additional functionality and properties for method-specific scopes.
 */
public abstract class MethodScope extends Scope {
    private final ASTNode methodNode;
    private String methodName;
    protected boolean hasData = false;
    public List<TestInstance> tests;
    public ShorTestListener listener;

    public String getMethodName() {
        return methodName;
    }

    protected ASTNode getDoc(){
        var docs = methodNode.getChildren(languageConfig.getDocFilter());
        return docs.length>0?docs[0]:null;
    }

    public final boolean getIsStatic(){
        return getIsStatic(methodNode);
    }

    protected boolean getIsStatic(@NotNull ASTNode node) {
        ASTNode childByType = node.findChildByType(languageConfig.getModifierFilter());
        return childByType != null && childByType.getText().contains("static");
    }

    public List<TestInstance> getTests() {
        return tests;
    }

    /**
     * this will exclude empty (null) scopes so that templating errors are easier to spot
     *
     * @return the map of key-value pairs that define this scope, to be supplied to the templating engine
     */
    public HashMap<String,Object> getScopes(){
        if (listener==null)
            return new HashMap<>();
        HashMap<String, Object> scopes = new HashMap<>() {{
            put(Constants.METHOD_NAME, methodName);
            put(Constants.IS_STATIC, getIsStatic());
            put(Constants.TEST_LIST, tests.stream().map(TestInstance::getScopes).toArray());
        }};
        while (scopes.values().remove(null));
        while (scopes.values().remove(""));
        return scopes;
    }

    public MethodScope(LanguageConfig languageConfig, ClassScope classScope, @NotNull ASTNode method, String methodName, HashMap<String,Integer> nameMap) {
        super(languageConfig);
        tests = new ArrayList<>();
        methodNode = method;
        if (getDoc() == null)
            return;

        hasData = true;
        this.methodName = methodName;

        JavadocListener javadocListener = new JavadocListener();
        String text = getDoc().getText();

        var javadocTree = new JavadocParser(
                new CommonTokenStream(
                        new JavadocLexer(
                                CharStreams.fromString(
                                        text)))).documentation();
        org.antlr.v4.runtime.tree.ParseTreeWalker.DEFAULT.walk(javadocListener, javadocTree);
        List<BlockTag> blockTags = javadocListener.blockTags;

        for(BlockTag blockTag : blockTags) {
            final String content = blockTag.content();
            switch(blockTag.context()){
                case testFragment ->
                        testFragments.put(blockTag.subject(), content);
                case testData ->
                        testData.put(blockTag.subject(), content);
                case test -> {
                    try {
                        ShorTestParser.StartContext tree =
                                new ShorTestParser(
                                    new CommonTokenStream(
                                            new ShorTestLexer(
                                                    CharStreams.fromString(content)))).start();
                        listener = new ShorTestListener(classScope, this, nameMap);
                        org.antlr.v4.runtime.tree.ParseTreeWalker.DEFAULT.walk(listener, tree);

                        ArrayList<TestInstance> testList = listener.getTestList();
                        tests.addAll(testList);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                default ->
                        throw new RuntimeException("unsupported block tag found for method context:"+blockTag.context());
            }
        }

    }
}
