package com.github.swankdave.fastest.typescript;

import com.github.swankdave.fastest.javascript.JavascriptLanguageConfig;
import org.jetbrains.annotations.NotNull;

public class TypescriptLanguageConfig extends JavascriptLanguageConfig {
    @NotNull
    @Override
    public String getMustacheTemplateFilenameForLanguage() {
        return "Files/typescript.jtest.mustache";
    }
}
