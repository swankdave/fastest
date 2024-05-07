package com.github.swankdave.fastest.javascript;

import com.github.swankdave.fastest.ClassScope;
import com.github.swankdave.fastest.MethodScope;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class JavascriptMethodScope extends MethodScope {
    public JavascriptMethodScope(ClassScope classScope, @NotNull ASTNode method, String methodName, HashMap<String, Integer> nameMap) {
        super(new JavascriptLanguageConfig(), classScope, method,methodName,nameMap);
    }
}
