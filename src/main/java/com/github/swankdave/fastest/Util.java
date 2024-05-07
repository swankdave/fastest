package com.github.swankdave.fastest;

import com.intellij.lang.ASTNode;
import javaslang.Tuple2;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class Util {

    public static List<Tuple2<TestHeaders,String>> GetFragmentNames(ASTNode docBlock, LanguageConfig language){
        return Arrays.stream(docBlock.getChildren(null))
                .filter(node -> node.getElementType().equals(language.getDocSectionTokenType()) &&
                                node.getChildren(null).length>3 &&
                                Arrays.stream(TestHeaders.values()).map(Enum::toString)
                                    .anyMatch(n -> n.equals(node.getFirstChildNode().getText().substring(1))))
                .map(node -> new Tuple2<>(
                        TestHeaders.valueOf(node.getFirstChildNode().getText().substring(1)),
                        node.getChildren(null)[2].getText()))
                .collect(Collectors.toList());
    }
    /**
     *
     * @return
     * @param docBlockFragments
     */
    public static String getNamedFragment(
            TestHeaders FragmentType,
            String FragmentName,
            List<ASTNode> docBlockFragments,
            LanguageConfig language){
        StringBuilder rtn = new StringBuilder();
        boolean foundTarget = false;
        LinkedList<ASTNode> fragments = new LinkedList<>(docBlockFragments);
        Collections.reverse(fragments);
        while (!fragments.isEmpty()) {
            var node = fragments.removeLast();
            foundTarget = foundTarget || language.isDocTagWithName(FragmentType, FragmentName, node);

            //replace each tree node with its children so we can filter out DOC_COMMENT_LEADING_ASTERISKS
            var children = new LinkedList<>(Arrays.stream(node.getChildren(null)).toList());
            Collections.reverse(children);
            if (!children.isEmpty()) {
                fragments.addAll(children);
                continue;
            }

            if(!foundTarget)
                continue;

            //filter out comment characters
            if (Arrays.stream(language.getDocBlockTokens()).noneMatch(t->t.equals(node.getElementType())))
                rtn.append(node.getText());

            //bail when we find the next tag
            if (language.isDocTag(FragmentType, node.getTreeNext()))
                break;

        }
        return rtn.toString();
    }

    public static String getDocBlockAsString(ASTNode docBlock, LanguageConfig language) {
        StringBuilder stringBuilder = new StringBuilder();
        var fragments = new java.util.LinkedList<>(Arrays.stream(docBlock.getChildren(null)).toList());
        Collections.reverse(fragments);
        while (!fragments.isEmpty()) {
            var node = fragments.removeLast();

            java.util.LinkedList<ASTNode> children = new java.util.LinkedList<>(Arrays.stream(node.getChildren(null)).toList());
            Collections.reverse(children);
            if (!children.isEmpty()) {
                fragments.addAll(children);
                continue;
            }

            if (Arrays.stream(language.getDocBlockTokens())
                    .noneMatch(t->t.equals(node.getElementType())))
                    stringBuilder.append(node.getText());
        }

        StringBuilder rtn = new StringBuilder();
        boolean keepLine = false;
        for (var line : stringBuilder.toString().split("\n")) {
            if (line.trim().startsWith("@"))
                keepLine = line.trim().startsWith("@test");
            else if (keepLine)
                rtn.append(line.isBlank()?"":line).append("\n");
        }
        return rtn.toString();
    }

    public static String getUnnamedFragment(TestHeaders FragmentType, ASTNode docBlock, LanguageConfig language){
        return getNamedFragment(FragmentType, "", new LinkedList<>(Arrays.stream(docBlock.getChildren(null)).toList()), language);
    }

    /**
     * convenience function that joins the non-empty members of a list of strings with the aforementioned delimiter
     * @param delimiter delimiter to use to seperate strings
     * @param strings the list of strings to be joined
     * @return block of text
     *
     * @test
     * (",",new String[]{"a","test"}) => "a,test"
     */
    static String filteredJoin(String delimiter, String[] strings){
        return Arrays.stream(strings).filter(s -> s!=null && !s.isBlank()).collect(Collectors.joining(delimiter));
    }

    /**
     * Returns the minimum indent level of the given input block.
     *
     * @param inputBlock the input block to be analyzed
     * @return the minimum indent level of the input block
     *
     * @test
     * ("")=>0
     * (" ")=>1
     * ("    ")=>4
     * ("test\n    test")=>0
     * ("    test\ntest")=>0
     * ("    test\n test")=>1
     */
    public static int minIndentLevel(String inputBlock){
        return
            Arrays.stream(inputBlock.split("\n"))
                .map(line->{
                    int tabCount = 0;
                    //noinspection StatementWithEmptyBody
                    while (line.startsWith(" ".repeat(++tabCount))){}
                    return tabCount-1;
                }).min(Integer::compare).orElse(0);
    }

    /**
     * Sets the minimum indentation level of the given text to the target indent level.
     *
     * @param text          the text to set the minimum indentation level for
     * @param targetIndent  the target indent level
     * @return the text with the minimum indentation level set to the target indent level
     *
     * @test
     * emptyTest ("",10).length()=>0                              //empty string should not be indented
     * (("test", " test", "  test", "    test")SET,3)=>"   test" //set indent to 3, reguardless of input
     * (" test",1)=>" test"
     * (" test\ntest",1)=>"  test\n test"
     * blankTest ("   ",1)=>"   "                                //just return blank lines, they might mean something else
     * ("test\n   \ntest",1)=>" test\n   \n test"                //don't process blank lines
     */
    public static @NotNull String setMindent(String text, int targetIndent) {
        if (text.trim().isEmpty())
            return text;
        text = text.replace("\t"," ".repeat(4));
        while(minIndentLevel(text) != targetIndent) {
            int mindent = minIndentLevel(text);
            text = Arrays.stream(text.split("\n")).map(line-> {
                if (line.trim().isEmpty())
                    return line;
                if (mindent< targetIndent)
                    return  " ".repeat(targetIndent - mindent) + line;
                return line.substring(mindent - targetIndent);
            }).collect(Collectors.joining("\n"));
        }
        return text;
    }

    public static @NotNull String getDeclarationFromDefinition(String name, String value, LanguageConfig languageConfig) {
        return String.join(" ", new String[]{
                " ".repeat(languageConfig.getDefaultIndent()-1),
                languageConfig.getCompileTimeTypeDeterminationKeyword(),
                name,
                languageConfig.getAssignmentOperator(),
                value + languageConfig.getLineterminationKeyword(),
                "\n"});
    }
}
