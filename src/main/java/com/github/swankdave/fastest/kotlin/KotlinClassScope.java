package com.github.swankdave.fastest.kotlin;

import com.github.swankdave.fastest.ClassScope;
import com.github.swankdave.fastest.MethodScope;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.kotlin.psi.KtClass;
import org.jetbrains.kotlin.psi.KtFile;
import org.jetbrains.kotlin.psi.KtNamedFunction;
import org.jetbrains.kotlin.psi.stubs.elements.KtStubElementTypes;

import java.util.HashMap;
import java.util.Objects;


public class KotlinClassScope extends ClassScope {
    @NotNull
    @Override
    public String getClassName() {
        return Objects.requireNonNull(getStrongClass().getName());
    }

    @NotNull
    protected KtClass getStrongClass(){
        return (KtClass)getPsiClass().getPsi();
    }

    @NotNull
    @Override
    public String getPackageName() {
        return ((KtFile)this.psiFile).getPackageFqName().asString();
    }

    @NotNull
    @Override
    protected ASTNode getPsiClassBody() {
        return getPsiClass().getChildren(TokenSet.create(KtStubElementTypes.CLASS_BODY))[0];
    }

    @Override
    protected String getFunctionName(ASTNode node){
        return ((KtNamedFunction)node.getPsi()).getName();
    }

    @NotNull
    @Override
    protected MethodScope getFunctionScope(ClassScope classScope, ASTNode method, HashMap<String, Integer> nameMap){
        return new KotlinMethodScope(classScope, method, getFunctionName(method), nameMap);
    }

    public KotlinClassScope(PsiFile psiFile) {
        super(psiFile, new KotlinLanguageConfig());
        for (var obj: getStrongClass().getCompanionObjects())
            for (var function: Objects.requireNonNull(obj.getBody()).getFunctions())
                methodList.add(new KotlinMethodScope(this, function.getNode(),getFunctionName(function.getNode()),nameMap));
    }
}