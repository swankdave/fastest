package com.github.swankdave.fastest;

import com.github.swankdave.fastest.javadocparser.JavadocLexer;
import com.github.swankdave.fastest.javadocparser.JavadocParser;
import com.intellij.lang.ASTNode;
import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * This abstract class represents the scope of a class in the source code.
 * It extends the {@link Scope} class.
 */
public abstract class ClassScope extends Scope {
    protected List<MethodScope> methodList;
    protected HashMap<String, Integer> nameMap;
    protected NavigatablePsiElement psiFile;

    @NotNull
    public abstract String getClassName();

    @NotNull
    public abstract String getPackageName();

    @NotNull
    abstract protected MethodScope getFunctionScope(ClassScope classScope, ASTNode method, HashMap<String, Integer> nameMap);

    @NotNull
    abstract protected String getFunctionName(ASTNode node);

    @NotNull
    protected ASTNode getPsiClassBody() {
        return getPsiClass();
    }

    public List<MethodScope> getMethodList() {
        return methodList;
    }

    /**
     * Retrieves the PSI class node.
     *
     * @return The PSI class node.
     * @throws RuntimeException If the class is not found for the file.
     */
    @NotNull
    protected  ASTNode getPsiClass() {
        var classList = psiFile.getNode().getChildren(languageConfig.getClassFilter());
        if (classList.length>0)
            return classList[0];
        throw new RuntimeException("class not found for file "+psiFile.getName());
    }

    protected ASTNode getClassDocBlock(){
        var docs = getPsiClass().getChildren(languageConfig.getDocFilter());
        if (docs.length>0)
            return docs[0];
        return null;
    }

    /**
     * @param directory The directory of the file containing the class under test, so we can search between that and
     *                  the project root for test fragments to inherrit
     */
    private void getNamespaceScope(PsiDirectory directory){
        if (directory.getParentDirectory() != null) {
            if (!directory.getVirtualFile().getPath().equals(directory.getProject().getBasePath()))
                getNamespaceScope(Objects.requireNonNull(directory.getParentDirectory()));
            Arrays.stream(directory.getFiles()).filter(f -> f.getName().equalsIgnoreCase("testfragments.txt")).forEach(file -> incorporateTestParts(file.getNode()));
        }
    }

    public ClassScope(PsiFile psiFile, LanguageConfig languageConfig) {
        super(languageConfig);
        nameMap = new HashMap<>();
        methodList = new ArrayList<>();

        this.psiFile = Objects.requireNonNull(psiFile, "psiFile must not be null in order to calculate full tree dependencies");
        assert !getClassName().endsWith("test") :"this is a test file, bailing";
        getNamespaceScope( psiFile.getContainingDirectory() );

        incorporateTestParts(getClassDocBlock());

        for (var m: getPsiClassBody().getChildren(languageConfig.getFunctionFilter()))
            methodList.add(getFunctionScope(this, m, nameMap));
    }

    public void incorporateTestParts(ASTNode classDocBlock) {
        if (classDocBlock == null)
            return;
        JavadocListener javadocListener = new JavadocListener();
        var javadocTree = new JavadocParser(
                new CommonTokenStream(
                        new JavadocLexer(
                                CharStreams.fromString(
                                        classDocBlock.getText())))).documentation();
        org.antlr.v4.runtime.tree.ParseTreeWalker.DEFAULT.walk(javadocListener, javadocTree);
        List<BlockTag> blockTags = javadocListener.blockTags;

        for(BlockTag blockTag : blockTags)
            switch(blockTag.context()) {
                case testFragment -> testFragments.put(blockTag.subject(), blockTag.content());
                case testData -> testData.put(blockTag.subject(), blockTag.content());
                case testDeclaration -> testDeclaration +=  (testDeclaration.isBlank()?"":"\n") + blockTag.content();
                case testSetup -> testSetup +=  (testSetup.isBlank()?"":"\n") + blockTag.content();
                case testTeardown -> testTeardown +=  (testTeardown.isBlank()?"":"\n") + blockTag.content();
                //TODO: implement this
                default -> throw new RuntimeException("unsupported block tag found for class context:"+blockTag.context());
            }
    }

    /**
     * this will exclude empty (null) scopes so that templating errors are easier to spot
     *
     * @return the map of key-value pairs that define this scope, to be supplied to the templating engine
     */
    @NotNull
    public HashMap<String, Object> getScopes() {
        String packageName = getPackageName();
        String className = getClassName();
        HashMap<String, Object> scopes = new HashMap<>() {
            {
                put("testNamespace", Constants.TEST_NAMESPACE);
                put("package_name", packageName);
                put(Constants.CLASS_NAME, className);
                put("class_fqname", packageName + "." + className);
                put(TestHeaders.testDeclaration.toString(), testDeclaration);
                put(TestHeaders.testSetup.toString(), testSetup);
                put(TestHeaders.testTeardown.toString(), testTeardown);
                put(Constants.METHOD_LIST, methodList.stream().map(MethodScope::getScopes).toArray());
            }
        };
        //noinspection StatementWithEmptyBody
        while (scopes.values().remove(null));
        while (scopes.values().remove(""));
        scopes.put("language_assignment_operator",languageConfig.getAssignmentOperator());
        scopes.put("language_compile_time_type_determination",languageConfig.getCompileTimeTypeDeterminationKeyword());
        scopes.put("language_new_value",languageConfig.getNewValueKeyword());
        scopes.put("class_name_postfix", Constants.CLASS_NAME_POSTFIX);
        scopes.put("article_name",Constants.ARTICLE_NAME);
        return scopes;
    }
}
