package com.github.swankdave.fastest.java;

import com.github.swankdave.fastest.LanguageConfig;
import com.github.swankdave.fastest.TestHeaders;
import com.intellij.lang.ASTNode;
import com.intellij.psi.impl.java.stubs.JavaStubElementTypes;
import com.intellij.psi.impl.source.tree.JavaDocElementType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;
import static com.intellij.psi.JavaDocTokenType.*;
import static com.intellij.psi.impl.source.tree.JavaDocElementType.DOC_PARAMETER_REF;
import static com.intellij.psi.impl.source.tree.JavaDocElementType.DOC_TAG;

public class JavaLanguageConfig extends LanguageConfig {

    public IElementType[] getDocBlockTokens(){
        return new IElementType[]{
                DOC_COMMENT_START,
                DOC_COMMENT_LEADING_ASTERISKS,
                DOC_COMMENT_END};
    }

    public JavaLanguageConfig() {}

    @NotNull
    @Override
    public String getMustacheTemplateFilenameForLanguage() {
        return "Files/java.jtest.mustache";
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
    @Override
    protected TokenSet getDocFilter() {
        return TokenSet.create(JavaDocElementType.DOC_COMMENT);
    }

    @NotNull
    @Override
    protected TokenSet getModifierFilter(){
        return TokenSet.create(JavaStubElementTypes.MODIFIER_LIST);
    }

    @Override
    public IElementType getDocSectionTokenType() {
        return DOC_TAG;
    }

    public boolean isDocTag(TestHeaders fragmentType, ASTNode node) {
        return node!=null && node.getElementType().equals(DOC_TAG_NAME);
    }

    @Override
    public boolean isDocTagWithName(TestHeaders fragmentType, String fragmentName, ASTNode node) {
        if (node.getElementType().equals(getDocSectionTokenType())) {
            if (node.getChildren(TokenSet.create(DOC_TAG_NAME))[0].getText().substring(1).equals(fragmentType.toString())) {
                if (fragmentName.isEmpty()) {
                    return true;
                } else {
                    var nameNode = node.findChildByType(DOC_PARAMETER_REF);
                    return nameNode != null && nameNode.getText().equals(fragmentName);
                }
            }
        }
        return false;
    }
}
