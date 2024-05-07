package com.github.swankdave.fastest;

import java.util.*;

/**
 * The TestConfig class represents a configuration for a test.
 */
public class TestConfig {

    private final MethodScope methodScope;

    public enum TestSections {
        testDoc,
        preamble,
        testName,
        constructor,
        predicate,
        result,
        error,
        postTest,
    }

    public boolean isException;
    public boolean isInverted;
    public Integer testIndex;
    public boolean containsSet;
    private final Map<TestSections, List<String>> testPartReferenceMap = new HashMap<>();
    private final Map<TestSections, String> testPartMap = new HashMap<>();

    public String getTestMethodName(){
        return methodScope.getMethodName();
    }
    private String getPrefix() {
        return methodScope.languageConfig.getKeywordPrefixForLanguage();
    }

    public boolean isStatic(){
        return methodScope.getIsStatic();
    }

    public String getPreamble() {
        return testPartMap.get(TestSections.preamble);
    }

    public String getConstructor() {
        return testPartMap.get(TestSections.constructor);
    }

    public String getTestDoc() {
        return testPartMap.get(TestSections.testDoc);
    }

    public String getPredicate() {
        return testPartMap.get(TestSections.predicate);
    }

    public String getResult() {
        return testPartMap.get(TestSections.result);
    }

    public String getError() {
        return testPartMap.get(TestSections.error);
    }

    public String getPostTest() {
        return testPartMap.get(TestSections.postTest);
    }

    public String getTestName() {
        return testPartMap.get(TestSections.testName);
    }

    public void setTestName(String testName) {
        testPartMap.put(TestSections.testName, testName);
    }

    public void setPreamble(String preamble) {
        testPartMap.put(TestSections.preamble, preamble);
    }

    public void setConstructor(String constructor) {
        testPartMap.put(TestSections.constructor, constructor);
    }

    public void setPredicate(String predicate) {
        testPartMap.put(TestSections.predicate, predicate);
    }

    public void setResult(String result) {
        testPartMap.put(TestSections.result, result);
    }

    public void setError(String error) {
        testPartMap.put(TestSections.error, error);
    }

    public void setPostTest(String postTest) {
        testPartMap.put(TestSections.postTest, postTest);
    }

    public void setTestDoc(String testDoc) {
        testPartMap.put(TestSections.testDoc, testDoc);
    }


    public TestConfig(MethodScope methodScope) {
        this.methodScope = methodScope;
        this.testIndex = 0;
        setTestDoc("");
        isException = false;
        isInverted = false;
        Arrays.stream(TestSections.values()).forEach(section -> testPartMap.put(section,""));
        Arrays.stream(TestSections.values()).forEach(section -> testPartReferenceMap.put(section, new ArrayList<>()));
    }

    public TestConfig(TestConfig config){
        this(config.methodScope);
        isException = config.isException;
        containsSet = config.containsSet;
        isInverted = config.isInverted;
        setTestDoc(config.getTestDoc());
        Arrays.stream(TestSections.values()).forEach(section -> testPartMap.put(section, config.testPartMap.get(section)));
        Arrays.stream(TestSections.values()).forEach(section -> testPartReferenceMap.put(section, config.testPartReferenceMap.get(section)));
        //autogenerated pre-amble should be re-autogenerated or not included in new preamble
    }



    /**
     * This method checks if the predicate and result are blank.
     *
     * @return true if both the predicate and result are not blank, false otherwise.
     */
    public boolean isValid() {
        return (!this.getPredicate().isBlank());
    }

    /**
     * Invalidates the test by resetting the predicate, result, test name, and error fields, and setting isException to false.
     */
    public void invalidateTest(){
        setPredicate("");
        setResult("");
        setTestName("");
        setError("");

        isException = false;
    }

    /**
     * Invalidates the post-test by resetting the test name and setting the post-test to an empty string.
     */
    public void invalidatePostTest(){
        invalidateTest();
        setPostTest("");
    }


    /**
     * Invalidates the pre-test by calling invalidatePostTest(), setting the preamble to an empty string, and setting the constructor to an empty string.
     */
    public void invalidatePreTest(){
        invalidatePostTest();
        setPreamble("");
        setConstructor("");
    }

    public boolean containsKeyword(String keyword){
        return containsLiteral(getPrefix() + keyword);
    }

    public boolean containsLiteral(String literal){
        return Arrays.stream(TestSections.values()).anyMatch(section -> testPartMap.get(section).contains(literal));
    }

    /**
     * Expands the test sections in the TestConfig with the specified set literal and replaces them with the given value.
     *
     * @param setLiteral  the set literal to be expanded
     * @param replaceWith the value to replace the set literal with
     */
    public void expandKeyword(String setLiteral, String replaceWith){
        if (replaceWith.trim().split("\\n",2).length == 1)
            replaceWith = replaceWith.trim();
        expandLiteral(getPrefix()+setLiteral, replaceWith);
    }

    /**
     * Expands the test sections in the TestConfig with the specified set literal and replaces them with the given value.
     *
     * @param setLiteral  the set literal to be expanded
     * @param replaceWith the value to replace the set literal with
     */
    public void expandLiteral(String setLiteral, String replaceWith){
        Arrays.stream(TestSections.values()).forEach(section ->{
            if (testPartMap.get(section).contains(setLiteral))
                    testPartReferenceMap.get(section).add(setLiteral);
            testPartMap.put(section, testPartMap.get(section).replace(setLiteral,replaceWith));
        });
    }

    /**
     * Expands the test sections in the TestConfig with the specified set literal and replaces them with the given value.
     *
     * @param name           the name of the section to be expanded
     * @param value          the value to replace the section with
     */
    public void dataExpand(String name, String value) {
        String token = getPrefix() + name;

        if (value.trim().split("\\n",2).length == 1)
            value = value.trim();

        String declaration = Util.getDeclarationFromDefinition(
                name,
                value,
                methodScope.languageConfig);

        if (Arrays.stream(new TestSections[]{
                TestSections.preamble,
                TestSections.constructor,
                TestSections.predicate,
                TestSections.result,
                TestSections.error}).anyMatch(section->testPartMap.get(section).contains(token))){
            addDeclaration(TestSections.preamble, token, declaration);

            Arrays.stream(TestSections.values()).forEach(section->testPartMap.put(section, testPartMap.get(section).replace(token,name)));
        } else if (testPartMap.get(TestSections.postTest).contains(name)) {
            addDeclaration(TestSections.postTest, token, declaration);
            setPostTest(getPostTest().replace(token, value));
            setTestDoc(getTestDoc().replace(token, value));
        }
    }


    private void addDeclaration(TestSections testSection, String name, String declaration) {
        String declarationRegex = "\n\\s*" + name + "\\s*(" + methodScope.languageConfig.getLineterminationKeyword() + ")?\\s*\n?";
        if ( testPartMap.get(testSection).matches(declarationRegex))
            testPartMap.put(testSection, testPartMap.get(testSection).replaceAll(declarationRegex,"\n"+ declaration + "\n"));
        else
            testPartMap.put(testSection,  declaration + testPartMap.get(testSection));
    }



    public void format(LanguageConfig languageConfig) {
        setTestDoc(Util.setMindent(getTestDoc(), languageConfig.getDefaultIndent() ));
        setPreamble(Util.setMindent(getPreamble(), 2*languageConfig.getDefaultIndent()));
    }
}
