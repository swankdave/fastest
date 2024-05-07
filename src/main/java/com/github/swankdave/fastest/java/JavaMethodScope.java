package com.github.swankdave.fastest.java;

import com.github.swankdave.fastest.ClassScope;
import com.github.swankdave.fastest.MethodScope;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class JavaMethodScope extends MethodScope {
    public JavaMethodScope(ClassScope classScope, @NotNull ASTNode method, String methodName, HashMap<String, Integer> nameMap) {
        super(new JavaLanguageConfig(), classScope, method,methodName, nameMap);
    }
}
