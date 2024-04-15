package com.github.swankdave.fastest.javascript
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class JavascriptClassScopeTest: BasePlatformTestCase()  {

    fun testDoesNotCrashIfNoClassDocumentation(){
        JavascriptClassScope(
            super.myFixture.addFileToProject(
                "src/JavascriptTestClass.js", """
                class JavascriptTestClass{
                    public void myTest(){}
                }
            """.trimIndent()
            )
        )
    }

    fun testDoesNotCrashIfEmptyClassDocumentation(){
        JavascriptClassScope(
            super.myFixture.addFileToProject(
                "src/JavascriptTestClass.js", """
                    //
                class JavascriptTestClass{
                    public void myTest(){}
                }
            """.trimIndent()
            )
        )
    }

    fun testBasicInitialization(){
        val classScope = JavascriptClassScope(
            super.myFixture.addFileToProject(
                "src/JavascriptTestClass.js", """
                /**
                 * @testDeclaration
                 * let i;
                 * @testSetup
                 * console.log("Class Test Setup");
                 * @testTeardown
                 * console.log("Class Test Teardown");
                 */
                class JavaTestClass{
                    static myTest(){}
                }
            """.trimIndent()
            )
        )
        assert(classScope.className=="JavaTestClass"){"class name is wrong"}
        assert(classScope.testDeclaration.contains("let i;")){"test Declaration Missing"}
        assert(classScope.testSetup.contains("console.log(\"Class Test Setup\");")){"testSetup is missing"}
        assert(classScope.testTeardown.contains("console.log(\"Class Test Teardown\");")){"testTeardown is missing"}
        assert(classScope.methodList.count()==1){"method count is incorrect"}
    }

}