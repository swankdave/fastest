package com.github.swankdave.fastest.typescript
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class TypescriptClassScopeTest: BasePlatformTestCase()  {

    fun testDoesNotCrashIfNoClassDocumentation(){
        TypescriptClassScope(
            super.myFixture.addFileToProject(
                "src/TypescriptTestClass.js", """
                class TypescriptTestClass{
                    public void myTest(){}
                }
            """.trimIndent()
            )
        )
    }

    fun testDoesNotCrashIfEmptyClassDocumentation(){
        TypescriptClassScope(
            super.myFixture.addFileToProject(
                "src/TypescriptTestClass.js", """
                    //
                class TypescriptTestClass{
                    public void myTest(){}
                }
            """.trimIndent()
            )
        )
    }

    fun testBasicInitialization(){
        val classScope = TypescriptClassScope(
            super.myFixture.addFileToProject(
                "src/TypescriptTestClass.js", """
                /**
                 * @testDeclaration
                 * let i;
                 * @testSetup
                 * console.log("Class Test Setup");
                 * @testTeardown
                 * console.log("Class Test Teardown");
                 */
                class TypescriptTestClass{
                    static myTest(){}
                }
            """.trimIndent()
            )
        )
        assert(classScope.className=="TypescriptTestClass"){"class name is wrong"}
        assert(classScope.testDeclaration.contains("let i;")){"test Declaration Missing"}
        assert(classScope.testSetup.contains("console.log(\"Class Test Setup\");")){"testSetup is missing"}
        assert(classScope.testTeardown.contains("console.log(\"Class Test Teardown\");")){"testTeardown is missing"}
        assert(classScope.methodList.count()==1){"method count is incorrect"}
    }

}