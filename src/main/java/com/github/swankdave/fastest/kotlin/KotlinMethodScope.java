package com.github.swankdave.fastest.kotlin;

import com.github.swankdave.fastest.ClassScope;
import com.github.swankdave.fastest.MethodScope;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class KotlinMethodScope extends MethodScope {

    @Override
    protected boolean getIsStatic(@NotNull ASTNode method) {
        return method.getTreeParent().getTreeParent().getElementType().toString().equals("OBJECT_DECLARATION");
    }

    public KotlinMethodScope(ClassScope classScope, @NotNull ASTNode method, String methodName, HashMap<String, Integer> nameMap) {
        super(new KotlinLanguageConfig(), classScope, method,methodName,nameMap);
    }
}
