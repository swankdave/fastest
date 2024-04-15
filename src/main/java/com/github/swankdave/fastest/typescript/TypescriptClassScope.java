package com.github.swankdave.fastest.typescript;

import com.github.swankdave.fastest.javascript.JavascriptClassScope;

import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;


public class TypescriptClassScope extends JavascriptClassScope {
    @NotNull
    @Override
    public String getMustacheTemplateFilename() {
        return "Files/typescript.jtest.mustache";
    }


    public TypescriptClassScope(PsiFile psiFile) {
        super(psiFile);

        //var classDocBlock = (PsiDocComment)getStrongClass().getNode().getFirstChildNode();
    }

}
