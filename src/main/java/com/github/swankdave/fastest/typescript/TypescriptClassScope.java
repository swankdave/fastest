package com.github.swankdave.fastest.typescript;

import com.github.swankdave.fastest.javascript.JavascriptClassScope;
import com.intellij.psi.PsiFile;

public class TypescriptClassScope extends JavascriptClassScope {
    public TypescriptClassScope(PsiFile psiFile) {
        super(psiFile, new TypescriptLanguageConfig());
    }
}
