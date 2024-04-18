package com.github.swankdave.fastest.parser;

import com.github.swankdave.fastest.Util;

public class TestConfig {
    public final String testMethodName;
    public final Boolean isStatic;
    public String testDoc;
    public String preamble;
    public String testName;
    public String constructor;
    public String predicate;
    public boolean isException;
    public String result;
    public String error;
    public String postTest;
    public Integer testIndex;
    public boolean containsSet;

    public TestConfig(String testMethodName, Boolean isStatic, int testIndex){
        this.isStatic = isStatic;
        this.testMethodName = testMethodName;
        this.testIndex = testIndex;
        testDoc = "";
        preamble = "";
        testName = "";
        constructor = "";
        predicate = "";
        isException = false;
        result = "";
        error = "";
        postTest = "";
    }

    public TestConfig(TestConfig config){
        testDoc = config.testDoc;
        preamble = config.preamble;
        testName = config.testName;
        constructor = config.constructor;
        predicate = config.predicate;
        testMethodName = config.testMethodName;
        isException = config.isException;
        result = config.result;
        error = config.error;
        postTest = config.postTest;
        isStatic = config.isStatic;
        testIndex = config.testIndex;
        containsSet = config.containsSet;
    }

    public boolean isValid() {
        return (!this.predicate.isBlank() && !this.result.isBlank());
    }

    public void invalidateTest(){
        predicate = "";
        result = "";
        testName = "";
        error = "";
        isException = false;
    }
    public void invalidatePostTest(){
        invalidateTest();
        postTest = "";
    }
    public void invalidatePreTest(){
        invalidatePostTest();
        preamble = "";
        constructor = "";
    }

    public boolean containsLiteral(String setLiteral){
        return  preamble.contains(setLiteral)||
                testName.contains(setLiteral)||
                constructor.contains(setLiteral)||
                predicate.contains(setLiteral)||
                result.contains(setLiteral)||
                error.contains(setLiteral)||
                postTest.contains(setLiteral);
    }

    public void expand(String setLiteral, String replaceWith){
        preamble = preamble.replace(setLiteral,replaceWith);
        testName = testName.replace(setLiteral,replaceWith);
        constructor = constructor.replace(setLiteral,replaceWith);
        predicate = predicate.replace(setLiteral,replaceWith);
        result = result.replace(setLiteral,replaceWith);
        error = error.replace(setLiteral,replaceWith);
        postTest = postTest.replace(setLiteral,replaceWith);
    }

    public void format() {
        testDoc  = Util.setMindent(testDoc, 4);
        preamble = Util.setMindent(preamble, 8);
    }

}
