package com.github.swankdave.fastest.kotlin;

import com.github.swankdave.fastest.MethodScope;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.kotlin.kdoc.lexer.KDocTokens;
import org.jetbrains.kotlin.lexer.KtTokens;

public class KotlinMethodScope extends MethodScope {

    @NotNull
    @Override
    protected TokenSet getDocFilter() {
        return TokenSet.create(KDocTokens.KDOC);
    }

    @NotNull
    @Override
    protected TokenSet getModifierFilter() {
        return KtTokens.MODIFIER_KEYWORDS;
    }

    @Override
    protected boolean getIsStatic(@NotNull ASTNode method) {
        return method.getTreeParent().getTreeParent().getElementType().toString().equals("OBJECT_DECLARATION");
    }

    public KotlinMethodScope(@NotNull ASTNode method, String methodName, Integer testNumber) {
        super(method,methodName,testNumber);
    }
}
