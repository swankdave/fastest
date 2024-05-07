/* This file is output by shortTest as a translation of inline test shorthand.
 * THIS FILE WILL BE OVERWRITTEN WITHOUT WARNING.
 * You have been warned */

package com.github.swankdave.fastest.test;

import com.github.swankdave.fastest.Util;

import static org.junit.Assert.*;

import org.junit.*;

public class UtilTest {


    /*
     * Ensuring minIndentLevel("") results in 0
     */
    @Test
    public void minIndentLevel0_test() {
        assertEquals(
                0,
                Util.minIndentLevel(""));

    }

    /*
     * Ensuring minIndentLevel(" ") results in 1
     */
    @Test
    public void minIndentLevel1_test() {
        assertEquals(
                1,
                Util.minIndentLevel(" "));

    }

    /*
     * Ensuring minIndentLevel("    ") results in 4
     */
    @Test
    public void minIndentLevel2_test() {
        assertEquals(
                4,
                Util.minIndentLevel("    "));

    }

    /*
     * Ensuring minIndentLevel("test\n    test") results in 0
     */
    @Test
    public void minIndentLevel3_test() {
        assertEquals(
                0,
                Util.minIndentLevel("test\n    test"));

    }

    /*
     * Ensuring minIndentLevel("    test\ntest") results in 0
     */
    @Test
    public void minIndentLevel4_test() {
        assertEquals(
                0,
                Util.minIndentLevel("    test\ntest"));

    }

    /*
     * Ensuring minIndentLevel("    test\n test") results in 1
     */
    @Test
    public void minIndentLevel5_test() {
        assertEquals(
                1,
                Util.minIndentLevel("    test\n test"));

    }

    /*
     * Ensuring setMindent("",10).length() results in 0
     */
    @Test
    public void emptyTest() {
        assertEquals(
                "empty string should not be indented", 0,
                Util.setMindent("", 10).length());

    }

    /*
     * Ensuring setMindent("test",3) results in "   test"
     */
    @Test
    public void setMindent1_test() {
        assertEquals(
                "set indent to 3, reguardless of input", "   test",
                Util.setMindent("test", 3));

    }

    /*
     * Ensuring setMindent(" test",3) results in "   test"
     */
    @Test
    public void setMindent2_test() {
        assertEquals(
                "set indent to 3, reguardless of input", "   test",
                Util.setMindent(" test", 3));

    }

    /*
     * Ensuring setMindent("  test",3) results in "   test"
     */
    @Test
    public void setMindent3_test() {
        assertEquals(
                "set indent to 3, reguardless of input", "   test",
                Util.setMindent("  test", 3));

    }

    /*
     * Ensuring setMindent("    test",3) results in "   test"
     */
    @Test
    public void setMindent4_test() {
        assertEquals(
                "set indent to 3, reguardless of input", "   test",
                Util.setMindent("    test", 3));

    }

    /*
     * Ensuring setMindent(" test",1) results in " test"
     */
    @Test
    public void setMindent5_test() {
        assertEquals(
                " test",
                Util.setMindent(" test", 1));

    }

    /*
     * Ensuring setMindent(" test\ntest",1) results in "  test\n test"
     */
    @Test
    public void setMindent6_test() {
        assertEquals(
                "  test\n test",
                Util.setMindent(" test\ntest", 1));

    }

    /*
     * Ensuring setMindent("   ",1) results in "   "
     */
    @Test
    public void blankTest() {
        assertEquals(
                "just return blank lines, they might mean something else", "   ",
                Util.setMindent("   ", 1));

    }

    /*
     * Ensuring setMindent("test\n   \ntest",1) results in " test\n   \n test"
     */
    @Test
    public void setMindent8_test() {
        assertEquals(
                "don't process blank lines", " test\n   \n test",
                Util.setMindent("test\n   \ntest", 1));

    }

}