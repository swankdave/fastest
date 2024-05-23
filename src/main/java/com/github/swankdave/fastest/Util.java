package com.github.swankdave.fastest;

import com.intellij.lang.ASTNode;
import javaslang.Tuple2;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Util {

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
                if (mindent < targetIndent)
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

    public static List<String> getSublistFromParameter(String statement, List<String> parameters) {
        var rtn = new ArrayList<String>();
        if (statement.trim().equals("*"))
            return new LinkedList<>(parameters);

        while (!statement.isBlank())
            if (statement.trim().startsWith("[")) {
                var firstBracket = statement.indexOf('[')+1;
                var secondBracket = statement.indexOf(']')+1;
                var rangeStatement = statement.substring(firstBracket, secondBracket - firstBracket).trim();
                statement = statement.substring(secondBracket).trim();
                for (var range : rangeStatement.split(",")) {
                    var start = range.split("-")[0].trim();
                    var end = "";
                    if (range.split("-").length>1)
                        end = range.split("-")[1].trim();

                    if (start.isBlank() && end.isBlank())
                        throw new Error("illegal range detected");
                    if (!start.isBlank() && !parameters.contains(start))
                        throw new Error("illegal range element: " + start);
                    if (!end.isBlank() && !parameters.contains(end))
                        throw new Error("illegal range element: " + end);

                    boolean keep = start.isBlank();
                    for (var parameter : parameters) {
                        if (parameter.equals(start))
                            keep = true;
                        if (keep)
                            if (!rtn.contains(parameter))
                                rtn.add(parameter);
                        if (parameter.equals(end))
                            keep = false;
                    }

                }
            } else if (statement.trim().startsWith("/")) {
                var firstBracket = statement.indexOf('/')+1;
                var secondBracket = statement.indexOf('/', firstBracket)+1;
                var rangeStatement = statement.substring(firstBracket, secondBracket - firstBracket).trim();
                statement = statement.substring(secondBracket).trim();
                var pattern = Pattern.compile(rangeStatement);
                for (var parameter : parameters)
                    if (pattern.matcher(parameter).matches())
                        rtn.add(parameter);
            } else
                for (var value : statement.split(" "))
                    if (!parameters.contains(value)) {
                        throw new Error("illegal range element: " + value);
                    } else {
                        rtn.add(value);
                        statement = statement.substring(value.length()).trim();
                    }
        return rtn;
    }

    public static Tuple2<List<String>, String>breakRangeParameters(String statement){
        List<String> rtn = new ArrayList<>();
        StringBuilder currentParameter = new StringBuilder();
        boolean inBracket = false;
        boolean inRegex = false;
        int parenthesisCount = 0;
        while (!statement.isBlank()){
            if (!inRegex && statement.charAt(0) == '(') {
                parenthesisCount++;
                if (parenthesisCount == 1) {
                    statement = statement.substring(1);
                    continue;
                }
            }
            if (!inRegex && statement.charAt(0) == ')') {
                parenthesisCount--;
                if (parenthesisCount == 0) {
                    statement = statement.substring(1);
                    break;
                }
            }
            if (!inRegex && statement.charAt(0) == '[')
                inBracket = true;
            if (statement.charAt(0) == '/') {
                inRegex = !inRegex;
            }
            if (!inBracket && !inRegex && statement.charAt(0) == ','){
                rtn.add(currentParameter.toString());
                currentParameter = new StringBuilder();
            } else
                currentParameter.append(statement.charAt(0));
            statement = statement.substring(1);
            if (!inRegex && !statement.isEmpty() && statement.charAt(0) == ']')
                inBracket = false;
        }
        rtn.add(currentParameter.toString());
        return new Tuple2<>(rtn, statement);
    }
}
