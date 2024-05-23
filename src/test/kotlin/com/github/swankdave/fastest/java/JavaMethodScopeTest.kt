package com.github.swankdave.fastest.java

import com.github.swankdave.fastest.Constants
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import junit.framework.TestCase

class JavaMethodScopeTest: BasePlatformTestCase()  {

    fun testDoesNotCrashIfNoMethodDocumentation(){
        JavaClassScope(
            super.myFixture.addFileToProject(
                "src/JavaTestClass.java", """
                class JavaTestClass{
                    public void myTest(){} 
                }
            """.trimIndent()
            )
        )
    }

    fun testDoesNotCrashIfEmptyMethodDocumentation(){
        JavaClassScope(
            super.myFixture.addFileToProject(
                "src/JavaTestClass.java", """
                class JavaTestClass{
                    //
                    public void myTest(){} 
                }
            """.trimIndent()
            )
        )
    }

    fun testDiscoverStaticMethod(){
        val methodScope = JavaClassScope(
            super.myFixture.addFileToProject(
                "src/JavaTestClass.java", """

                class JavaTestClass{
                /** @test
                 *      ("Shor","Test") => "ShorTest" # simple tests should be simple to write!
                 */
                    public static String myTest(String a, String b){
                        return a.concat(b);
                    }
                }
            """.trimIndent()
            )
        ).methodList[0]
        assert(methodScope.tests[0].scopes.containsKey(Constants.IS_STATIC)) { "failed to detect static method" }
    }

    fun testDiscoverNonStaticMethod(){
        val methodScope = JavaClassScope(
            super.myFixture.addFileToProject(
                "src/JavaTestClass.java", """

                class JavaTestClass{
                /** @test
                 *      ("Shor","Test") => "ShorTest" # simple tests should be simple to write!
                 */
                    public String myTest(String a, String b){
                        return a.concat(b);
                    }
                }
            """.trimIndent()
            )
        ).methodList[0]
        assert(!methodScope.tests[0].scopes.containsKey(Constants.IS_STATIC)) { "failed to detect non-static method" }
    }

    fun testDiscoverTestDocumentation(){
        val methodScope = JavaClassScope(
            super.myFixture.addFileToProject(
                "src/JavaTestClass.java", """

                class JavaTestClass{
                /** 
                 * @test
                 *   //This explains what the test is doing
                 *   ("Shor","Test") => "ShorTest" 
                 */
                    public String myTest(String a, String b){
                        return a.concat(b);
                    }
                }
            """.trimIndent()
            )
        ).methodList[0]
        assert(methodScope.tests[0].scopes.containsKey(Constants.TEST_DOC)) { "failed to detect test documentation" }
        assert((methodScope.tests[0].scopes[Constants.TEST_DOC]).toString().contains("This explains what the test is doing")){"incorrect test documentation"}
    }

    fun testDiscoverTestPreamble(){
        val methodScope = JavaClassScope(
            super.myFixture.addFileToProject(
                "src/JavaTestClass.java", """

                class JavaTestClass{
                /** 
                 * @test
                 *   String s = "This is setup for the test";
                 *   ("Shor","Test") => "ShorTest" 
                 */
                    public String myTest(String a, String b){
                        return a.concat(b);
                    }
                }
            """.trimIndent()
            )
        ).methodList[0]
        assert(methodScope.tests[0].scopes.containsKey(Constants.TEST_PREAMBLE)) { "failed to detect test preamble" }
        assert((methodScope.tests[0].scopes[Constants.TEST_PREAMBLE]).toString().contains("String s = \"This is setup for the test\"")){"incorrect test preamble "}
    }

    fun testDiscoverNamedTest(){
        val methodScope = JavaClassScope(
            super.myFixture.addFileToProject(
                "src/JavaTestClass.java", """

                class JavaTestClass{
                /** 
                 * @test
                 *   demotest ("Shor","Test") => "ShorTest" 
                 */
                    public String myTest(String a, String b){
                        return a.concat(b);
                    }
                }
            """.trimIndent()
            )
        ).methodList[0]
        assert(methodScope.tests[0].scopes.containsKey(Constants.TEST_NAME)) { "failed to detect test name" }
        assert((methodScope.tests[0].scopes[Constants.TEST_NAME]).toString().contains("demotest")){"incorrect testName "}
    }

    fun testDiscoverMultipleNamedTest(){
        val methodScope = JavaClassScope(
            super.myFixture.addFileToProject(
                "src/JavaTestClass.java", """

                class JavaTestClass{
                /** 
                 * @test
                 *   demotest (("Shor","Bor")SET,"Test") => ("ShorTest","BorTest")SET
                 */
                    public String myTest(String a, String b){
                        return a.concat(b);
                    }
                }
            """.trimIndent()
            )
        ).methodList[0]
        assert(methodScope.tests[0].scopes.containsKey(Constants.TEST_NAME)) { "failed to detect test name" }
        assert((methodScope.tests[0].scopes[Constants.TEST_NAME]).toString().contains("demotest")){"incorrect testName "}
        assert(methodScope.tests[1].scopes.containsKey(Constants.TEST_NAME)) { "failed to detect test name" }
        assert((methodScope.tests[1].scopes[Constants.TEST_NAME]).toString().contains("demotest")){"incorrect testName, test names incremented on print, not aquisition"}
    }

    fun testDiscoverTestObjectConstructor(){
        val methodScope = JavaClassScope(
            super.myFixture.addFileToProject(
                "src/JavaTestClass.java", """

                class JavaTestClass{
                /** 
                 * @test
                 *   (a) ("Shor","Test") => "ShorTest" 
                 */
                    public String myTest(String a, String b){
                        return a.concat(b);
                    }
                }
            """.trimIndent()
            )
        ).methodList[0]
        assert(methodScope.tests[0].scopes.containsKey(Constants.CONSTRUCTOR)) { "failed to detect test object constructor" }
        assert(methodScope.tests[0].scopes[Constants.CONSTRUCTOR].toString().contains("(a)")) {"incorrect test object constructor detected"}
    }

    fun testDiscoverTestPredicate(){
        val methodScope = JavaClassScope(
            super.myFixture.addFileToProject(
                "src/JavaTestClass.java", """

                class JavaTestClass{
                /** 
                 * @test
                 *   ("Shor","Test") => "ShorTest" 
                 */
                    public String myTest(String a, String b){
                        return a.concat(b);
                    }
                }
            """.trimIndent()
            )
        ).methodList[0]
        assert(methodScope.tests[0].scopes.containsKey(Constants.PREDICATE)) { "failed to detect test predicate" }
        assert(methodScope.tests[0].scopes[Constants.PREDICATE].toString().contains("(\"Shor\",\"Test\")")) {"incorrect test predicate detected"}
    }

    fun testDiscoverTestName(){
        val methodScope = JavaClassScope(
            super.myFixture.addFileToProject(
                "src/JavaTestClass.java", """

                class JavaTestClass{
                /** 
                 * @test
                 *   ("Shor","Test") => "ShorTest" 
                 */
                    public String myTest(String a, String b){
                        return a.concat(b);
                    }
                }
            """.trimIndent()
            )
        ).methodList[0]
        assert(methodScope.tests[0].scopes.containsKey(Constants.TEST_METHOD_NAME)) { "failed to detect name of method under test" }
        assert(methodScope.tests[0].scopes[Constants.TEST_METHOD_NAME].toString().contains("myTest")) {"incorrect name of method under test"}
    }

    fun testDiscoverTestNoException(){
        val methodScope = JavaClassScope(
            super.myFixture.addFileToProject(
                "src/JavaTestClass.java", """

                class JavaTestClass{
                /** 
                 * @test
                 *   ("Shor","Test") => "ShorTest" 
                 */
                    public String myTest(String a, String b){
                        return a.concat(b);
                    }
                }
            """.trimIndent()
            )
        ).methodList[0]
        assert(!methodScope.tests[0].scopes.containsKey(Constants.EXCEPTION_TEST)) { "incorrectly identified exception test" }
    }

    fun testDiscoverTestExpectException(){
        val methodScope = JavaClassScope(
            super.myFixture.addFileToProject(
                "src/JavaTestClass.java", """

                class JavaTestClass{
                /** 
                 * @test
                 *   ("Shor","Test") => (RuntimeException)EX 
                 */
                    public String myTest(String a, String b){
                        return a.concat(b);
                    }
                }
            """.trimIndent()
            )
        ).methodList[0]
        assert(methodScope.tests[0].scopes.containsKey(Constants.EXCEPTION_TEST)) { "failed to detect exception test" }
    }

    fun testDiscoverTestResult(){
        val methodScope = JavaClassScope(
            super.myFixture.addFileToProject(
                "src/JavaTestClass.java", """

                class JavaTestClass{
                /** 
                 * @test
                 *   ("Shor","Test") => "ShorTest" 
                 */
                    public String myTest(String a, String b){
                        return a.concat(b);
                    }
                }
            """.trimIndent()
            )
        ).methodList[0]
        assert(methodScope.tests[0].scopes.containsKey(Constants.RESULT)) { "failed to detect expected test result" }
        assert(methodScope.tests[0].scopes[Constants.RESULT].toString().contains("ShorTest" )) {"incorrect detected expected test result"}
    }

    fun testDiscoverTestCustomErrorMessage(){
        val methodScope = JavaClassScope(
            super.myFixture.addFileToProject(
                "src/JavaTestClass.java", """

                class JavaTestClass{
                /** 
                 * @test
                 *   ("Shor","Test") => "ShorTest" ///custom error message
                 */
                    public String myTest(String a, String b){
                        return a.concat(b);
                    }
                }
            """.trimIndent()
            )
        ).methodList[0]
        assert(methodScope.tests[0].scopes.containsKey(Constants.ERROR_MESSAGE)) { "failed to detect custom error message" }
        assert(methodScope.tests[0].scopes[Constants.ERROR_MESSAGE].toString().contains("custom error message" )) {"incorrect custom error message detected"}
    }

    fun testDiscoverTestPostamble(){
        val methodScope = JavaClassScope(
            super.myFixture.addFileToProject(
                "src/JavaTestClass.java", """

                class JavaTestClass{
                /** 
                 * @test
                 *   ("Shor","Test") => "ShorTest" 
                 *   String s = "This is teardown for the test";
                 */
                    public String myTest(String a, String b){
                        return a.concat(b);
                    }
                }
            """.trimIndent()
            )
        ).methodList[0]
        assert(methodScope.tests[0].scopes.containsKey(Constants.POST_TEST)) { "failed to detect test postamble" }
        assert((methodScope.tests[0].scopes[Constants.POST_TEST]).toString().contains("String s = \"This is teardown for the test\"")){"incorrect test postamble "}
    }

    fun testDiscoverDualTestPersistantPreamble(){
        val methodScope = JavaClassScope(
            super.myFixture.addFileToProject(
                "src/JavaTestClass.java", """

                class JavaTestClass{
                /** 
                 * @test
                 *   String s = "This is setup for the test";
                 *   ("Shor","Test") => "ShorTest" 
                 *   ("Boar","Test") => "BoarTest" 
                 */
                    public String myTest(String a, String b){
                        return a.concat(b);
                    }
                }
            """.trimIndent()
            )
        ).methodList[0]
        assert(methodScope.tests[1].scopes.containsKey(Constants.TEST_PREAMBLE)) { "failed to detect test preamble" }
        assert((methodScope.tests[1].scopes[Constants.TEST_PREAMBLE]).toString().contains("String s = \"This is setup for the test\"")){"incorrect test preamble "}
    }

    fun testDiscoverPreambleResetNoOverwrite(){
        val methodScope = JavaClassScope(
            super.myFixture.addFileToProject(
                "src/JavaTestClass.java", """

                class JavaTestClass{
                /** 
                 * @test
                 *   String s = "This is setup for the test";
                 *   ("Shor","Test") => "ShorTest" 
                 * 
                 *
                 *
                 *   ("Boar","Test") => "BoarTest" 
                 */
                    public String myTest(String a, String b){
                        return a.concat(b);
                    }
                }
            """.trimIndent()
            )
        ).methodList[0]
        assert(methodScope.tests[1].scopes.containsKey(Constants.TEST_PREAMBLE)) { "failed to detect test preamble" }
        assert(!(methodScope.tests[1].scopes[Constants.TEST_PREAMBLE]).toString().contains("String s = \"This is setup for the test\"")){"incorrect test preamble "}
    }

    fun testDiscoverPreambleResetWithOverwrite(){
        val methodScope = JavaClassScope(
            super.myFixture.addFileToProject(
                "src/JavaTestClass.java", """

                class JavaTestClass{
                /** 
                 * @test
                 *   String s = "This is setup for the test";
                 *   ("Shor","Test") => "ShorTest" 
                 * 
                 *
                 *
                 *   String t = "This is setup for another test";
                 *   ("Boar","Test") => "BoarTest" 
                 *
                 */
                    public String myTest(String a, String b){
                        return a.concat(b);
                    }
                }
            """.trimIndent()
            )
        ).methodList[0]
        assert(methodScope.tests[1].scopes.containsKey(Constants.TEST_PREAMBLE)) { "failed to detect test preamble" }
        assert(!(methodScope.tests[1].scopes[Constants.TEST_PREAMBLE]).toString().contains("String s = \"This is setup for another test\"")){"incorrect test preamble "}
    }

    fun testDiscoverPreambleResetWithABajillionNewlines(){
        val methodScope = JavaClassScope(
            super.myFixture.addFileToProject(
                "src/JavaTestClass.java", """
                                
                class JavaTestClass{
                /** 
                 * @test
                 *   String s = "This is setup for the test";
                 *   ("Shor","Test") => "ShorTest" 
                 * 
                 *
                 *
                 *
                 *
                 *
                 *
                 *
                 *
                 *   String t = "This is setup for another test";
                 *   ("Boar","Test") => "BoarTest" 
                 *
                 */
                    public String myTest(String a, String b){
                        return a.concat(b);
                    }
                }
            """.trimIndent()
            )
        ).methodList[0]
        assert(methodScope.tests.size>1) { "failed to detect all tests" }
    }

    fun testDiscoverDualTestPersistantPostamble(){
        val methodScope = JavaClassScope(
            super.myFixture.addFileToProject(
                "src/JavaTestClass.java", """

                class JavaTestClass{
                /** 
                 * @test
                 *   ("Shor","Test") => "ShorTest" 
                 *   String s = "This is teardown for the test";
                 *   ("Boar","Test") => "BoarTest" 
                 */
                    public String myTest(String a, String b){
                        return a.concat(b);
                    }
                }
            """.trimIndent()
            )
        ).methodList[0]
        assert(methodScope.tests[1].scopes.containsKey(Constants.POST_TEST)) { "failed to detect test postamble" }
        assert((methodScope.tests[1].scopes[Constants.POST_TEST]).toString().contains("String s = \"This is teardown for the test\"")){"incorrect test postamble "}
    }

    fun testDiscoverPostambleResetWithoutOverwrite(){
        val methodScope = JavaClassScope(
            super.myFixture.addFileToProject(
                "src/JavaTestClass.java", """

                class JavaTestClass{
                /** 
                 * @test
                 *   ("Shor","Test") => "ShorTest" 
                 *   String s = "This is teardown for the test";
                 *
                 *
                 *   ("Boar","Test") => "BoarTest" 
                 */
                    public String myTest(String a, String b){
                        return a.concat(b);
                    }
                }
            """.trimIndent()
            )
        ).methodList[0]
        assert(methodScope.tests[0].scopes.containsKey(Constants.POST_TEST)) { "failed to detect test postamble" }
        assert(!(methodScope.tests[1].scopes[Constants.POST_TEST]).toString().contains("String s = \"This is teardown for the test\"")){"incorrect test postamble "}
    }

    fun testDiscoverPostambleResetWithOverwrite(){
        val methodScope = JavaClassScope(
            super.myFixture.addFileToProject(
                "src/JavaTestClass.java", """

                class JavaTestClass{
                /** 
                 * @test
                 *   ("Shor","Test") => "ShorTest" 
                 *   String s = "This is teardown for the test";
                 *
                 *
                 *
                 *   ("Boar","Test") => "BoarTest" 
                 *   String s = "This is teardown for another test";
                 */
                    public String myTest(String a, String b){
                        return a.concat(b);
                    }
                }
            """.trimIndent()
            )
        ).methodList[0]
        assert(methodScope.tests[1].scopes.containsKey(Constants.POST_TEST)) { "failed to detect test postamble" }
        assert((methodScope.tests[1].scopes[Constants.POST_TEST]).toString().contains("String s = \"This is teardown for another test\"")){"incorrect test postamble "}
    }

    fun testDiscoverTestFragment(){
        val methodScope = JavaClassScope(
            super.myFixture.addFileToProject(
                "src/JavaTestClass.java", """

                class JavaTestClass{
                /** 
                 * @testFragment testfragmentname
                 *   String s = "This is teardown for the test";
                 * @test
                 *   ("Boar","Test") => "BoarTest" 
                 *   $"""+"""testfragmentname
                 */
                    public String myTest(String a, String b){
                        return a.concat(b);
                    }
                }
            """.trimIndent()
            )
        ).methodList[0]
        assert(methodScope.testFragments.isNotEmpty())
        assert(methodScope.testData.isEmpty())
        assert(methodScope.testFragments.contains("testfragmentname"))
        TestCase.assertFalse(methodScope.testFragments.contains("notTestFragmentName"))
        assert(methodScope.tests[0].config.postTest.contains("String s = \"This is teardown for the test\";") )
        TestCase.assertFalse(methodScope.tests[0].config.postTest.contains("testfragmentname"))
    }

    fun testDiscoverTestData(){
        val methodScope = JavaClassScope(
            super.myFixture.addFileToProject(
                "src/JavaTestClass.java", """

                class JavaTestClass{
                /** 
                 * @testData testDataName
                 *   "This is data for the test"
                 * @test
                 *   ($"""+"""testDataName,"Test") => "This is data for the testTest" 
                 */
                    public String myTest(String a, String b){
                        return a.concat(b);
                    }
                }
            """.trimIndent()
            )
        ).methodList[0]
        assert(methodScope.testData.isNotEmpty())
        assert(methodScope.testFragments.isEmpty())
        assert(methodScope.testData.contains("testDataName"))
        TestCase.assertFalse(methodScope.testData.contains("notTestDataName"))
        assert(methodScope.tests[0].config.predicate.contains("testDataName"))
        assert(!methodScope.tests[0].config.predicate.contains("$"+"testDataName"))
        assert(methodScope.tests[0].config.preamble.contains("var testDataName ="))
        assert(methodScope.tests[0].config.preamble.contains("\"This is data for the test\""))
    }

    fun testBigRealMethod(){
        val classScope = JavaClassScope(
            super.myFixture.addFileToProject(
                "src/JavaTestClass.java","""
            class JavaTestClass{
                /**
                 * Removes comments from the given text.
                 *
                 * @param text the text with comments to be stripped
                 * @return the text without any comments
                 *
                 * @test
                 * (("text","text ","/*\n*text\n** /\n")SET)=>"text"#tab needs to be accounted for correctly
                 */
                public static String stripComments(String text){
                    return Arrays.stream(
                            text
                                    .replaceFirst("^\\s*/\\s?\\*+\\s*\\n","")
                                    .replaceFirst("\\n?\\s*\\*+\\s?+/[\\s\\n]*${'$'}","")
                                    .split("\n"))
                            .map(s -> s.replaceFirst("^\\s*/?\\*+\\s*","").trim())
                            .collect(Collectors.joining("\n")).trim();
                }
            }
        """.trimIndent())
        )
        TestCase.assertEquals(3, classScope.methodList[0].getTests().size)
    }
    fun testBigRealMethod2() {
        val classScope = JavaClassScope(
            super.myFixture.addFileToProject(
                "src/JavaTestClass.java", """
            class JavaTestClass{
                /**
                 * convenience function that joins the non-empty members of a list of strings with the aforementioned delimiter
                 * @param delimiter delimiter to use to seperate strings
                 * @param strings the list of strings to be joined
                 * @return block of text
                 *
                 * @test
                 * (",",new String[]{"test"}) => "a,test"
                 */
                static String filteredJoin(String delimiter, String[] strings){
                    return Arrays.stream(strings).filter(s -> s!=null && !s.isBlank()).collect(Collectors.joining(delimiter));
                }
            }
        """.trimIndent()
            )
        )
        assertEquals("(\",\",new String[]{\"test\"})", classScope.methodList[0].tests[0].config.predicate)
    }

    fun testConditionNoInvert() {
        val classScope = JavaClassScope(
            super.myFixture.addFileToProject(
                "src/JavaTestClass.java", """
                class JavaTestClass{
                /** 
                 * @test
                 *   ("Shor","Test") => "ShorTest" 
                 */
                    public String myTest(String a, String b){
                        return a.concat(b);
                    }
                }
        """.trimIndent()
            )
        )
        assertFalse(classScope.methodList[0].tests[0].config.isInverted)
    }
    fun testConditionInvert(){
        val classScope = JavaClassScope(
            super.myFixture.addFileToProject(
                "src/JavaTestClass.java","""
                class JavaTestClass{
                /** 
                 * @test
                 *   ("Shor","Test") !=> "ShorTest" 
                 */
                    public String myTest(String a, String b){
                        return a.concat(b);
                    }
                }
        """.trimIndent())
        )
        assert(classScope.methodList[0].tests[0].config.isInverted)
    }
    fun testConditionUnInvert(){
        val classScope = JavaClassScope(
            super.myFixture.addFileToProject(
                "src/JavaTestClass.java","""
                class JavaTestClass{
                /** 
                 * @test
                 *   ("Shor","Test") !=> "ShorTest" 
                 *   ("Shor","Test")  => "ShorTest" 
                 */
                    public String myTest(String a, String b){
                        return a.concat(b);
                    }
                }
        """.trimIndent())
        )
        assert(classScope.methodList[0].tests[0].config.isInverted)
        assertFalse(classScope.methodList[0].tests[1].config.isInverted)
    }

    fun testJustCallIt(){
        val classScope = JavaClassScope(
            super.myFixture.addFileToProject(
                "src/JavaTestClass.java","""
                class JavaTestClass{
                /** 
                 * @test
                 *   ("Shor","Test")
                 */
                    public String myTest(String a, String b){
                        return a.concat(b);
                    }
                }
        """.trimIndent())
        )
        assert(classScope.methodList[0].tests.size>0)
    }
    fun testMoreSetTroubleAndComments() {
        val classScope = JavaClassScope(
            super.myFixture.addFileToProject(
                "src/JavaTestClass.java", """
                class JavaTestClass{
                /** 
                 * @test
                 * //i want this to be ignored
                 *   ( (4,5)SETA,2 ) !=> (9,10)SETB
                 */
                    public String myTest(String a, String b){
                        return a.concat(b);
                    }
                }
        """.trimIndent()
            ))
        assert(classScope.methodList[0].tests.size==4)
        assert(!classScope.methodList[0].tests[0].config.preamble.contains("ignored"))
    }

    fun testHasParameterSet(){
        val classScope = JavaClassScope(
            super.myFixture.addFileToProject(
                "src/JavaTestClass.java", """
                class JavaTestClass{
                    /** 
                    * @test
                    * parameterset : { thing : otherthing, also:stuff and stuff }
                    * {thing3:thing4}
                    * (a,b)=>c
                    **/
                    public void myTest(){} 
                }
            """.trimIndent()
            )
        )
        assertEquals(2, classScope.methodList[0].listener.parameterSets.size)
        val set1: Map<String, String> = classScope.methodList[0].listener.parameterSets["parameterset"]!!
        assertNotNull(set1)
        TestCase.assertEquals(2, set1.size)
        TestCase.assertEquals("otherthing", set1.getValue("thing"))
        TestCase.assertEquals("stuff and stuff", set1.getValue("also").trim())
        val set2: Map<String, String> = classScope.methodList[0].listener.parameterSets["_parameter_1"]!!
        assertNotNull(set2)
        TestCase.assertEquals(1, set2.size)
        TestCase.assertEquals("thing4",set2.getValue("thing3"))
    }
    fun testFakeSetKeyword() {
        val classScope = JavaClassScope(
            super.myFixture.addFileToProject(
                "src/JavaTestClass.java", """
                class JavaTestClass{
                /** 
                 * @test
                 * ( (4,5)SET,2 ) => (6,7)SET
                 * ///u ("SET" above)m
                 */
                    public String myTest(String a, String b){
                        return a.concat(b);
                    }
                }
        """.trimIndent()
            ))
        assert(classScope.methodList[0].tests.size==2)
    }

}