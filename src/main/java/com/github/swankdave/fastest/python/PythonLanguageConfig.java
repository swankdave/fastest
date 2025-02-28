package com.github.swankdave.fastest.python;

import com.github.swankdave.fastest.LanguageConfig;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;

public class PythonLanguageConfig extends LanguageConfig {
    /**
     * Retrieves the class filter for the language.
     *
     * @return The TokenSet representing the class filter.
     */
    @Override
    protected @NotNull TokenSet getClassFilter() {
        return null;
    }

    @Override
    public IElementType[] getDocBlockTokens() {
        return new IElementType[0];
    }

    /**
     * Retrieves the filename of the Mustache template file for the specified language.
     * This method is abstract and needs to be implemented by the subclasses.
     *
     * @return The filename of the Mustache template for the language.
     */
    @Override
    public String getMustacheTemplateFilenameForLanguage() {
        return "";
    }

    /**
     * Retrieves the TokenSet representing the function filter for the language used in the language parser.
     *
     * @return The TokenSet representing the function filter.
     */
    @Override
    protected @NotNull TokenSet getFunctionFilter() {
        return null;
    }

    /**
     * Retrieves the TokenSet representing the filter for documentation blocks used in the language parser.
     *
     * @return The TokenSet representing the filter for documentation blocks.
     */
    @Override
    protected TokenSet getDocFilter() {
        return null;
    }

    /**
     * Retrieves the TokenSet representing the filter for modifiers used in the language parser.
     *
     * @return The TokenSet representing the filter for modifiers.
     */
    @Override
    protected TokenSet getModifierFilter() {
        return null;
    }

    @Override
    public IElementType getDocSectionTokenType() {
        return null;
    }
}
