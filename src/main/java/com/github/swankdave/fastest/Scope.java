package com.github.swankdave.fastest;

import java.util.HashMap;
import java.util.Map;

/**
 * The Scope class represents a scope in a test document. It provides access to the test declaration, setup, teardown, test fragments,
 * and test data defined in the current scope. It also holds a reference to the language configuration used for parsing and templating.
 */
public abstract class Scope {
    protected String testDeclaration;
    protected String testSetup;
    protected String testTeardown;
    protected Map<String, String> testFragments;
    protected Map<String, String> testData;
    public LanguageConfig languageConfig;

    public Scope(LanguageConfig languageConfig) {
        this.testDeclaration = "";
        this.testSetup = "";
        this.testTeardown = "";
        this.languageConfig = languageConfig;
        testFragments = new HashMap<>();
        testData = new HashMap<>();
    }

    public String getTestDeclaration() {
        return testDeclaration;
    }

    public String getTestSetup() {
        return testSetup;
    }

    public String getTestTeardown() {
        return testTeardown;
    }

    /**
     * @return the list of test fragment identifiers that are defined in the current scope, and their definitions
     */
    public Map<String, String> getTestFragments() {
        return testFragments;
    }

    /**
     * @return the list of test data identifiers that are defined in the current scope, and their definitions
     */
    public Map<String, String> getTestData() {
        return testData;
    }

    /**
     * @return the list of key-value pairs that define this scope, to be supplied to the templating engine
     */
    public abstract HashMap<String, Object> getScopes();
}
