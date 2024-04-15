package com.github.swankdave.fastest;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.github.swankdave.fastest.Util.getSection;

public abstract class ClassScope implements IProvideScope {
    protected String packageName;
    public String className;
    protected String testDeclaration;
    protected String testSetup;
    protected String testTeardown;
    protected List<MethodScope> methodList;
    protected PsiFile psiFile;

    public abstract String getMustacheTemplateFilename();

    public String getTestDeclaration() {
        return testDeclaration;
    }

    public String getTestSetup() {
        return testSetup;
    }

    public String getTestTeardown() {
        return testTeardown;
    }

    public List<MethodScope> getMethodList() {
        return methodList;
    }

    protected Integer getTestCount(String methodName) {
        return methodList.stream()
                .filter(m -> Objects.equals(m.methodName, methodName))
                .map(a -> a.getTests().size())
                .reduce(0, Integer::sum);
    }

    @NotNull
    abstract protected TokenSet getDocFilter();
    @NotNull
    abstract protected TokenSet getClassFilter();
    @NotNull
    abstract protected TokenSet getFunctionFilter();

    @NotNull
    protected abstract String getClassName();
    @NotNull
    abstract protected String getPackageName();
    @NotNull
    abstract protected MethodScope getFunctionScope(ASTNode method);
    @NotNull
    abstract protected String getFunctionName(ASTNode node);

    @NotNull
    protected  ASTNode getPsiClassBody() {
        return getPsiClass();
    }

    @NotNull
    protected  ASTNode getPsiClass() {
        var classList = psiFile.getNode().getChildren(getClassFilter());
        if (classList.length>0)
            return classList[0];
        throw new RuntimeException("class not found for file "+psiFile.getName());
    }

    @NotNull
    protected String getClassDocBlock(){
        var docs = getPsiClass().getChildren(getDocFilter());
        if (docs.length>0)
            return docs[0].getText();
        return "";
    }

    public ClassScope(PsiFile psiFile) {
        this.psiFile = Objects.requireNonNull(psiFile);
        packageName = getPackageName();
        className = getClassName();
        assert !className.endsWith("test") :"this is a test file, bailing";

        var classDocBlock = getClassDocBlock();
        testDeclaration = getSection(Util.TestSections.testDeclaration, classDocBlock);
        testSetup = getSection(Util.TestSections.testSetup, classDocBlock);
        testTeardown = getSection(Util.TestSections.testTeardown, classDocBlock);

        methodList = new ArrayList<>();
        for (var m: getPsiClassBody().getChildren(getFunctionFilter()))
            methodList.add(getFunctionScope(m));
    }

    @NotNull
    public HashMap<String, Object> getScopes() {
        HashMap<String, Object> scopes = new HashMap<>() {
            {
                put("testNamespace", Constants.TEST_NAMESPACE);
                put("package_name", packageName);
                put(Constants.CLASS_NAME, className);
                put("class_fqname", packageName + "." + className);
                put(Util.TestSections.testDeclaration.toString(), testDeclaration);
                put(Util.TestSections.testSetup.toString(), testSetup);
                put(Util.TestSections.testTeardown.toString(), testTeardown);
                put(Constants.METHOD_LIST, methodList.stream().map(MethodScope::getScopes).toArray());
            }
        };
        //noinspection StatementWithEmptyBody
        while (scopes.values().remove(null));
        return scopes;
    }
}
