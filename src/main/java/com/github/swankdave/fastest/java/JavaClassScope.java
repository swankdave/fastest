package com.github.swankdave.fastest.java;

import com.github.swankdave.fastest.ClassScope;
import com.github.swankdave.fastest.IProvideScope;
import com.github.swankdave.fastest.MethodScope;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.java.stubs.JavaStubElementTypes;
import com.intellij.psi.impl.source.PsiClassImpl;
import com.intellij.psi.impl.source.PsiJavaFileImpl;
import com.intellij.psi.impl.source.PsiMethodImpl;
import com.intellij.psi.impl.source.tree.JavaDocElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class JavaClassScope extends ClassScope implements IProvideScope {
    @NotNull
    @Override
    public String getMustacheTemplateFilename() {
        return "Files/java.jtest.mustache";
    }

    @NotNull
    @Override
    protected TokenSet getDocFilter() {
        return TokenSet.create(JavaDocElementType.DOC_COMMENT);
    }

    @NotNull
    @Override
    protected TokenSet getClassFilter() {
        return TokenSet.create(JavaStubElementTypes.CLASS);
    }

    @NotNull
    @Override
    protected TokenSet getFunctionFilter() {
        return TokenSet.create(JavaStubElementTypes.METHOD);
    }

    @NotNull
    protected PsiClassImpl getStrongClass(){
        return (PsiClassImpl)getPsiClass().getPsi();
    }

    @NotNull
    @Override
    protected String getClassName() {
        return Objects.requireNonNull(getStrongClass().getName());
    }

    @NotNull
    @Override
    protected String getPackageName() {
        return ((PsiJavaFileImpl)psiFile).getPackageName();
    }

    @NotNull
    @Override
    protected String getFunctionName(ASTNode node){
        return ((PsiMethodImpl)node.getPsi()).getName();
    }

    @NotNull
    @Override
    protected MethodScope getFunctionScope(ASTNode method){
        return new JavaMethodScope(method, getFunctionName(method), getTestCount(getFunctionName(method)));
    }

    public JavaClassScope(PsiFile psiFile) {
        super(psiFile);
    }
}

