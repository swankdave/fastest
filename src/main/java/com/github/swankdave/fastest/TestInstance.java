package com.github.swankdave.fastest;

import com.github.swankdave.fastest.parser.TestConfig;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class TestInstance implements IProvideScope {

    TestConfig config;

    @Nullable
    private String trim(String str){
        if (str!=null)
            return str.trim();
        return null;
    }

    private static String getTestName(String name, String testMethodName,int testNumber) {
        return name.isBlank()?
                testMethodName + testNumber + "_test":
                name.trim();
    }

    public TestInstance(TestConfig testConfig) {
        assert !testConfig.predicate.isBlank():"predicate required";
        assert !testConfig.result.isBlank():"result required";
        config = new TestConfig(testConfig);
    }

    public HashMap<String, Object> getScopes(){
        HashMap<String, Object> map = new HashMap<>() {
            {
                put(Constants.IS_STATIC, (config.isStatic ? "true" : null));
                put(Constants.TEST_DOC, config.testDoc);
                put(Constants.TEST_PREAMBLE, config.preamble);
                put(Constants.TEST_NAME, getTestName(trim(config.testName),config.testMethodName, config.testIndex));
                put(Constants.CONSTRUCTOR, trim(config.constructor));
                put(Constants.PREDICATE, trim(config.predicate));
                put(Constants.TEST_METHOD_NAME, config.testMethodName);
                put(Constants.EXCEPTION_TEST, config.isException?true: null);
                put(Constants.RESULT, trim(config.result));
                put(Constants.ERROR_MESSAGE, trim(config.error));
                put(Constants.POST_TEST, config.postTest);
            }
        };
        //noinspection StatementWithEmptyBody
        while (map.values().remove(null));
        return map;
    }
}
