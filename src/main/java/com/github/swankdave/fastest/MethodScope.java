package com.github.swankdave.fastest;

import com.github.swankdave.fastest.parser.ShorTestLexer;
import com.github.swankdave.fastest.parser.ShorTestParser;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class MethodScope implements IProvideScope {
    private final ClassScope classScope;
    public String methodName;
    protected boolean isStatic = false;
    protected boolean hasData = false;
    protected List<TestInstance> tests;
    protected ShorTestListener listener;
    protected Map<String, String> testFragments;

    protected abstract TokenSet getDocFilter();

    protected String getDocForMethod(ASTNode method){
        var docs = method.getChildren(getDocFilter());
        if (docs.length>0)
            return docs[0].getText();
        return "";
    }

    protected abstract TokenSet getModifierFilter();

    protected boolean getIsStatic(@NotNull ASTNode node) {
        ASTNode childByType = node.findChildByType(getModifierFilter());
        return childByType != null && childByType.getText().contains("static");
    }

    public Map<String, String> getTestFragments() {
        return testFragments;
    }

    public List<TestInstance> getTests() {
        return tests;
    }

    public HashMap<String,Object> getScopes(){
        if (listener==null)
            return new HashMap<>();
        return new HashMap<>() {{
            put(Constants.METHOD_NAME, methodName);
            put(Constants.IS_STATIC,isStatic);
            put(Constants.TEST_LIST, tests.stream().map(TestInstance::getScopes).toArray());
        }};
    }

    public MethodScope(ClassScope classScope, @NotNull ASTNode method, String methodName, int testNumber) {
        this.classScope = classScope;
        tests = new ArrayList<>();
        String docBlock = Util.getSection(Util.TestSections.test, getDocForMethod(method));
        testFragments = Util.getNamedFragments(getDocForMethod(method));

        if (docBlock.isEmpty())
            return;
        hasData = true;
        this.methodName = methodName;
        isStatic = getIsStatic(method);

        InputStream stream = new ByteArrayInputStream(docBlock.getBytes(StandardCharsets.UTF_8));
        try {
            ShorTestLexer lexer = new ShorTestLexer(CharStreams.fromStream(stream, StandardCharsets.UTF_8));
            CommonTokenStream commonTokenStream = new CommonTokenStream(lexer);
            ShorTestParser parser = new ShorTestParser(commonTokenStream);
            var tree = parser.start();
            listener = new ShorTestListener(classScope, this, methodName, isStatic, testNumber);
            org.antlr.v4.runtime.tree.ParseTreeWalker.DEFAULT.walk(listener, tree);

            tests = listener.getTestList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
