package com.github.swankdave.fastest.python;

import com.github.swankdave.fastest.ClassScope;
import com.github.swankdave.fastest.MethodScope;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class PythonClassScope extends ClassScope {

    @Override
    public @NotNull String getClassName() {
        return "";
    }

    @Override
    public @NotNull String getPackageName() {
        return "";
    }

    @Override
    protected @NotNull String getFunctionName(ASTNode node) {
        return "";
    }

    @Override
    protected @NotNull MethodScope getFunctionScope(ClassScope classScope, ASTNode method, HashMap<String, Integer> nameMap) {
        return null;
    }

    public PythonClassScope(PsiFile psiFile) { super(psiFile, new PythonLanguageConfig()); }
}
