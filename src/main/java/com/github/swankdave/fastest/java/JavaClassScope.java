package com.github.swankdave.fastest.java;

import com.github.swankdave.fastest.ClassScope;
import com.github.swankdave.fastest.MethodScope;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.PsiClassImpl;
import com.intellij.psi.impl.source.PsiJavaFileImpl;
import com.intellij.psi.impl.source.PsiMethodImpl;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;

public class JavaClassScope extends ClassScope {

    @NotNull
    protected PsiClassImpl getStrongClass(){
        return (PsiClassImpl)getPsiClass().getPsi();
    }

    @NotNull
    @Override
    public String getClassName() {
        return Objects.requireNonNull(getStrongClass().getName());
    }

    @NotNull
    @Override
    public String getPackageName() {
        return ((PsiJavaFileImpl)psiFile).getPackageName();
    }

    @NotNull
    @Override
    protected String getFunctionName(ASTNode node){
        return ((PsiMethodImpl)node.getPsi()).getName();
    }

    @NotNull
    @Override
    protected MethodScope getFunctionScope(ClassScope classScope, ASTNode method, HashMap<String, Integer> nameMap){
        return new JavaMethodScope(classScope, method, getFunctionName(method), nameMap);
    }

    public JavaClassScope(PsiFile psiFile) {
        super(psiFile, new JavaLanguageConfig());
    }
}

