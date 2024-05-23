package com.github.swankdave.fastest.javascript;

import com.github.swankdave.fastest.LanguageConfig;
import com.github.swankdave.fastest.TestHeaders;
import com.intellij.lang.ASTNode;
import com.intellij.lang.ecmascript6.ES6ElementTypes;
import static com.intellij.lang.javascript.JSDocTokenTypes.*;
import static com.intellij.lang.javascript.JSStubElementTypes.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;

public class JavascriptLanguageConfig extends LanguageConfig {
    @Override
    public IElementType[] getDocBlockTokens() {
        return new IElementType[]{
                DOC_COMMENT_START,
                DOC_COMMENT_LEADING_ASTERISK,
                DOC_COMMENT_END};
    }

    @NotNull
    @Override
    public String getMustacheTemplateFilenameForLanguage() {
        return "Files/javascript.jest.vtl";
    }

    @NotNull
    @Override
    protected TokenSet getDocFilter() {
        return TokenSet.create(DOC_COMMENT);
    }

    @NotNull
    @Override
    protected TokenSet getClassFilter() {
        return TokenSet.create(ES6ElementTypes.CLASS);
    }

    @NotNull
    @Override
    protected TokenSet getFunctionFilter() {
        return TokenSet.create(FUNCTION_DECLARATION);
    }

    @NotNull
    @Override
    protected TokenSet getModifierFilter() {
        return ATTRIBUTE_LISTS;
    }

    @Override
    public IElementType getDocSectionTokenType() {
        return null;
    }

    public boolean isDocTagWithName(TestHeaders fragmentType, String fragmentName, ASTNode node) {
        var children = node.getChildren(TokenSet.create(DOC_TAG_NAME));
        if (children.length>0)
            if (children[0].getText().substring(1).equals(fragmentType.toString()))
                return fragmentName.isEmpty() || node.getText().substring(1).matches("^" + fragmentType + " *" + fragmentName);
        return false;
    }
}
