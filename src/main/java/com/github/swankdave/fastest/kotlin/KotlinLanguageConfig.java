package com.github.swankdave.fastest.kotlin;

import com.github.swankdave.fastest.LanguageConfig;
import com.github.swankdave.fastest.TestHeaders;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.kotlin.KtNodeTypes;
import org.jetbrains.kotlin.kdoc.lexer.KDocTokens;
import org.jetbrains.kotlin.lexer.KtTokens;
import org.jetbrains.kotlin.psi.stubs.elements.KtStubElementTypes;



public class KotlinLanguageConfig extends LanguageConfig {
    @Override
    public String getMustacheTemplateFilenameForLanguage() {
        return "Files/kotlin.jtest.vtl";
    }

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
    public IElementType getDocSectionTokenType() {
        return null;
    }

    @NotNull
    @Override
    protected TokenSet getClassFilter() {
        return TokenSet.create(KtStubElementTypes.CLASS);
    }

    @NotNull
    @Override
    protected TokenSet getFunctionFilter() {
        return TokenSet.create(KtNodeTypes.FUN);
    }

    @NotNull
    @Override
    public String getNewValueKeyword() {return "";}

    public IElementType[] getDocBlockTokens(){
        return new IElementType[]{KDocTokens.START, KDocTokens.LEADING_ASTERISK, KDocTokens.END};
    }

    public boolean isDocTag(TestHeaders fragmentType, ASTNode node) {
        return node!=null && node.getElementType().equals(KDocTokens.TAG_NAME);
    }

    @Override
    public boolean isDocTagWithName(TestHeaders fragmentType, String fragmentName, ASTNode node) {
        var children = node.getChildren(TokenSet.create(KDocTokens.TAG_NAME));
        if (children.length>0)
            if (children[0].getText().substring(1).equals(fragmentType.toString()))
                return fragmentName.isEmpty() || node.getText().substring(1).matches("^" + fragmentType + " *" + fragmentName);
        return false;
    }
}
