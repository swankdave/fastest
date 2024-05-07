package com.github.swankdave.fastest.javascript;

import com.github.swankdave.fastest.ClassScope;
import com.github.swankdave.fastest.LanguageConfig;
import com.github.swankdave.fastest.MethodScope;
import com.intellij.lang.ASTNode;
import com.intellij.lang.ecmascript6.psi.impl.ES6ClassImpl;
import com.intellij.lang.javascript.psi.impl.JSFunctionImpl;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;

public class JavascriptClassScope extends ClassScope {
    @NotNull
    protected ES6ClassImpl getStrongClass(){
        return (ES6ClassImpl)getPsiClass().getPsi();
    }

    @NotNull
    @Override
    public String getClassName() {
        return Objects.requireNonNull(getStrongClass().getName());
    }

    @NotNull
    @Override
    public String getPackageName() {
        String namespace = getStrongClass().getJSNamespace().toString();
        if (namespace.equals("*"))
            return "";
        return namespace;
    }

    @NotNull
    @Override
    protected String getFunctionName(ASTNode node){
        return Objects.requireNonNull(((JSFunctionImpl<?>) node.getPsi()).getName());
    }

    @NotNull
    @Override
    protected MethodScope getFunctionScope(ClassScope classScope, ASTNode method, HashMap<String, Integer> nameMap){
        return new JavascriptMethodScope(classScope, method, getFunctionName(method), nameMap);
    }

    public JavascriptClassScope(PsiFile psiFile, LanguageConfig languageConfig) {
        super(psiFile, languageConfig);
    }

    public JavascriptClassScope(PsiFile psiFile) {
        this(psiFile, new JavascriptLanguageConfig());
    }
}
