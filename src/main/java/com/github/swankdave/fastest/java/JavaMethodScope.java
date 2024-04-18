package com.github.swankdave.fastest.java;

import com.github.swankdave.fastest.ClassScope;
import com.github.swankdave.fastest.IProvideScope;
import com.github.swankdave.fastest.MethodScope;
import com.intellij.lang.ASTNode;
import com.intellij.psi.impl.java.stubs.JavaStubElementTypes;
import com.intellij.psi.impl.source.tree.JavaDocElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;

public class JavaMethodScope extends MethodScope implements IProvideScope {

    @NotNull
    @Override
    protected TokenSet getDocFilter() {
        return TokenSet.create(JavaDocElementType.DOC_COMMENT);
    }

    @NotNull
    @Override
    protected TokenSet getModifierFilter(){
        return TokenSet.create(JavaStubElementTypes.MODIFIER_LIST);
    }

    public JavaMethodScope(ClassScope classScope, @NotNull ASTNode method, String methodName, int testNumber) {
        super(classScope, method,methodName,testNumber);
    }
}
