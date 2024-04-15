package com.github.swankdave.fastest.javascript;

import com.github.swankdave.fastest.ClassScope;
import com.github.swankdave.fastest.MethodScope;
import com.intellij.lang.ASTNode;
import com.intellij.lang.ecmascript6.ES6ElementTypes;
import com.intellij.lang.ecmascript6.psi.impl.ES6ClassImpl;
import com.intellij.lang.javascript.JSStubElementTypes;
import com.intellij.lang.javascript.psi.impl.JSFunctionImpl;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class JavascriptClassScope extends ClassScope {
    @NotNull
    @Override
    public String getMustacheTemplateFilename() {
        return "Files/javascript.jest.mustache";
    }

    @NotNull
    @Override
    protected TokenSet getDocFilter() {
        return TokenSet.create(JSStubElementTypes.DOC_COMMENT);
    }

    @NotNull
    @Override
    protected TokenSet getClassFilter() {
        return TokenSet.create(ES6ElementTypes.CLASS);
    }

    @NotNull
    @Override
    protected TokenSet getFunctionFilter() {
        return TokenSet.create(JSStubElementTypes.FUNCTION_DECLARATION);
    }

    @NotNull
    protected ES6ClassImpl getStrongClass(){
        return (ES6ClassImpl)getPsiClass().getPsi();
    }

    @NotNull
    @Override
    protected String getClassName() {
        return Objects.requireNonNull(getStrongClass().getName());
    }

    @NotNull
    @Override
    protected String getPackageName() {
        String namespace = getStrongClass().getJSNamespace().toString();
        if (namespace.equals("*"))
            return "";
        return namespace;
    }

    @NotNull
    @Override
    protected String getFunctionName(ASTNode node){
        return ((JSFunctionImpl)node.getPsi()).getName();
    }

    @NotNull
    @Override
    protected MethodScope getFunctionScope(ASTNode method){
        return new JavascriptMethodScope(method, getFunctionName(method), getTestCount(getFunctionName(method)));
    }

    public JavascriptClassScope(PsiFile psiFile) {
        super(psiFile);
    }
}
