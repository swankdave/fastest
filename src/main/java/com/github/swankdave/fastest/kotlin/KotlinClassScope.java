package com.github.swankdave.fastest.kotlin;

import com.github.swankdave.fastest.ClassScope;
import com.github.swankdave.fastest.MethodScope;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.kotlin.KtNodeTypes;
import org.jetbrains.kotlin.kdoc.lexer.KDocTokens;
import org.jetbrains.kotlin.psi.KtClass;
import org.jetbrains.kotlin.psi.KtFile;
import org.jetbrains.kotlin.psi.KtNamedFunction;
import org.jetbrains.kotlin.psi.stubs.elements.KtStubElementTypes;

import java.util.Objects;


public class KotlinClassScope extends ClassScope {
    @Override
    public String getMustacheTemplateFilename() {
        return "Files/kotlin.jtest.mustache";
    }

    @NotNull
    @Override
    protected TokenSet getDocFilter() {
        return TokenSet.create(KDocTokens.KDOC);
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
    protected String getClassName() {
        return Objects.requireNonNull(getStrongClass().getName());
    }

    @NotNull
    protected KtClass getStrongClass(){
        return (KtClass)getPsiClass().getPsi();
    }

    @NotNull
    @Override
    protected String getPackageName() {
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
    protected MethodScope getFunctionScope(ASTNode method){
        return new KotlinMethodScope(method, getFunctionName(method), getTestCount(getFunctionName(method)));
    }

    public KotlinClassScope(PsiFile psiFile) {
        super(psiFile);
        for (var obj: getStrongClass().getCompanionObjects())
            for (var function: Objects.requireNonNull(obj.getBody()).getFunctions())
                methodList.add(new KotlinMethodScope(function.getNode(),getFunctionName(function.getNode()), getTestCount(getFunctionName(function.getNode()))));
    }
}