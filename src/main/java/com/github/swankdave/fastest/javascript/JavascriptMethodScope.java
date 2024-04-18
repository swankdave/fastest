package com.github.swankdave.fastest.javascript;

import com.github.swankdave.fastest.ClassScope;
import com.github.swankdave.fastest.IProvideScope;
import com.github.swankdave.fastest.MethodScope;
import com.intellij.lang.ASTNode;
import com.intellij.lang.javascript.JSStubElementTypes;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;

public class JavascriptMethodScope extends MethodScope implements IProvideScope {

    @NotNull
    @Override
    protected TokenSet getDocFilter() {
        return TokenSet.create(JSStubElementTypes.DOC_COMMENT);
    }

    @NotNull
    @Override
    protected TokenSet getModifierFilter() {
        return JSStubElementTypes.ATTRIBUTE_LISTS;
    }

    public JavascriptMethodScope(ClassScope classScope, @NotNull ASTNode method, String methodName, int testNumber) {
        super(classScope, method,methodName,testNumber);
    }
}
