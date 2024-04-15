package com.github.swankdave.fastest.java
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class JavaClassScopeTest: BasePlatformTestCase()  {

    fun testDoesNotCrashIfNoClassDocumentation(){
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

    fun testDoesNotCrashIfEmptyClassDocumentation(){
        JavaClassScope(
            super.myFixture.addFileToProject(
                "src/JavaTestClass.java", """
                    //
                class JavaTestClass{
                    public void myTest(){} 
                }
            """.trimIndent()
            )
        )
    }

    fun testBasicInitialization(){
        val classScope = JavaClassScope(
            super.myFixture.addFileToProject(
                "src/JavaTestClass.java", """
                /**
                 * @testDeclaration
                 * int i;
                 * @testSetup
                 * System.out.print("Class Test Setup");
                 * @testTeardown
                 * System.out.print("Class Test Teardown");
                 */
                class JavaTestClass{
                    public void myTest(){} 
                }
            """.trimIndent()
            )
        )
        assert(classScope.className=="JavaTestClass"){"class name is wrong"}
        assert(classScope.testDeclaration.contains("int i;")){"test Declaration Missing"}
        assert(classScope.testSetup.contains("System.out.print(\"Class Test Setup\");")){"testSetup is missing"}
        assert(classScope.testTeardown.contains("System.out.print(\"Class Test Teardown\");")){"testTeardown is missing"}
        assert(classScope.methodList.count()==1){"method count is incorrect"}
    }

}