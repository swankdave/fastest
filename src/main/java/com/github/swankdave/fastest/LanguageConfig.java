package com.github.swankdave.fastest;

import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;

/**
 * The LanguageConfig class represents a configuration for a specific programming language. It contains methods
 * and abstract methods that should be implemented by the subclasses.
 */
public abstract class LanguageConfig {
    public abstract IElementType[] getDocBlockTokens();

    /**
     * Retrieves the filename of the Mustache template file for the specified language.
     * This method is abstract and needs to be implemented by the subclasses.
     *
     * @return The filename of the Mustache template for the language.
     */
    public abstract String getMustacheTemplateFilenameForLanguage();

    /**
     * Retrieves the keyword prefix for the specified language.
     *
     * @return The keyword prefix for the language.
     */
    public String getKeywordPrefixForLanguage() {return "$";}

    /**
     * Retrieves the class filter for the language.
     *
     * @return The TokenSet representing the class filter.
     */
    @NotNull
    abstract protected TokenSet getClassFilter();

    /**
     * Retrieves the TokenSet representing the function filter for the language used in the language parser.
     *
     * @return The TokenSet representing the function filter.
     */
    @NotNull
    abstract protected TokenSet getFunctionFilter();

    /**
     * Retrieves the TokenSet representing the filter for documentation blocks used in the language parser.
     *
     * @return The TokenSet representing the filter for documentation blocks.
     */
    protected abstract TokenSet getDocFilter();

    /**
     * Retrieves the TokenSet representing the filter for modifiers used in the language parser.
     *
     * @return The TokenSet representing the filter for modifiers.
     */
    protected abstract TokenSet getModifierFilter();

    /**
     * Retrieves the compile-time type determination keyword used in the language.
     *
     * @return The compile-time type determination keyword.
     */
    public String getCompileTimeTypeDeterminationKeyword(){return "var";}

    /**
     *
     * Retrieves the line termination keyword for the language.
     *
     * @return The line termination keyword.
     */
    public String getLineterminationKeyword(){return ";";}

    /**
     * Retrieves the default indent value.
     *
     * @return The default indent value.
     */
    public int getDefaultIndent() {return 4;}

    public abstract IElementType getDocSectionTokenType();

    /**
     * Retrieves the assignment operator used in the language.
     *
     * @return The assignment operator.
     */
    public String getAssignmentOperator() {return "=";}

    public String getNewValueKeyword() {return "new";}

    public boolean isDocTag(TestHeaders fragmentType, ASTNode node) {
        return node!=null && node.getText().contains("@"+fragmentType.toString());
    }

    public boolean isDocTagWithName(TestHeaders fragmentType, String fragmentName, ASTNode node) {
        return node!=null && node.getText().contains("@"+fragmentType.toString()+ " +" +fragmentName);
    }

}
