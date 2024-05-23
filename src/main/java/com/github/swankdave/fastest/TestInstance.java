package com.github.swankdave.fastest;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class TestInstance {

    public TestConfig config;

    @Nullable
    private String trim(String str){
        if (str!=null)
            return str.trim();
        return null;
    }

    private static String getTestName(String customName, String testMethodName, int testNumber) {
        return (customName.isBlank()? testMethodName: customName).trim() + (testNumber>0?"_"+testNumber:"")+"_test";
    }

    public TestInstance(TestConfig testConfig) {
        assert !testConfig.getPredicate().isBlank():"predicate required";
        config = new TestConfig(testConfig);
    }

    public HashMap<String, Object> getScopes(){
        HashMap<String, Object> map = new HashMap<>() {
            {
                put(Constants.IS_STATIC, (config.isStatic() ? "true" : null));
                put(Constants.TEST_DOC, config.getTestDoc());
                put(Constants.TEST_PREAMBLE, config.getPreamble());
                put(Constants.TEST_NAME, getTestName(trim(config.getTestName()),config.getTestMethodName(), config.testIndex));
                put(Constants.CONSTRUCTOR, trim(config.getConstructor()));
                put(Constants.PREDICATE, trim(config.getPredicate()));
                put(Constants.TEST_METHOD_NAME, config.getTestMethodName());
                put(Constants.EXCEPTION_TEST, config.isException?true: null);
                put(Constants.TEST_INVERTED, config.isInverted?true: null);
                put(Constants.RESULT, trim(config.getResult()));
                put(Constants.ERROR_MESSAGE, trim(config.getError()));
                put(Constants.POST_TEST, config.getPostTest());
            }
        };
        //noinspection StatementWithEmptyBody
        while (map.values().remove(null));
        while (map.values().remove(""));
        return map;
    }
}
