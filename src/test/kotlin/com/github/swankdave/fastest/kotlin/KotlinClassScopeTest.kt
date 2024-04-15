package com.github.swankdave.fastest.kotlin
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class KotlinClassScopeTest: BasePlatformTestCase()  {

    fun testDoesNotCrashIfNoClassDocumentation(){
        KotlinClassScope(
            super.myFixture.addFileToProject(
                "src/KotlinTestClass.kt", """
                package testPackage
                class KotlinTestClass{
                    fun myTest(){}
                }
            """.trimIndent()
            )
        );
    }

    fun testDoesNotCrashIfEmptyClassDocumentation(){
        KotlinClassScope(
            super.myFixture.addFileToProject(
                "src/KotlinTestClass.kt", """
                //
                class KotlinTestClass{
                    fun myTest(){} 
                }
            """.trimIndent()
            )
        );
    }

    fun testBasicInitialization(){
        val classScope = KotlinClassScope(
            super.myFixture.addFileToProject(
                "src/KotlinTestClass.kt", """
                /**
                 * @testDeclaration
                 * val i = 0
                 * @testSetup
                 * print("Class Test Setup");
                 * @testTeardown
                 * print("Class Test Teardown");
                 */
                class KotlinTestClass{
                    fun myTest(){} 
                }
            """.trimIndent()
            )
        );
        assert(classScope.className=="KotlinTestClass"){"class name is wrong"};
        assert(classScope.testDeclaration.contains("val i = 0")){"test Declaration Missing"}
        assert(classScope.testSetup.contains("print(\"Class Test Setup\")")){"testSetup is missing"}
        assert(classScope.testTeardown.contains("print(\"Class Test Teardown\")")){"testTeardown is missing"}
        assert(classScope.methodList.count()==1){"method count is incorrect"}
    }

}