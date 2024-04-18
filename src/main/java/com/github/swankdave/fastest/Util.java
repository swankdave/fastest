package com.github.swankdave.fastest;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class Util {
    public static Map<String, String> getNamedSections(String docBlock){
        return Arrays.stream(docBlock.split("\\\\?\\*\\s*@"))
                .map(Util::stripComments)
                .filter(block -> block.startsWith("test"))
                .collect(Collectors.toMap(entry -> entry.split("[\\s\\n]",2)[0], entry -> {
                    var split = entry.split("[\\s\\n]", 2);
                    if (split.length<2)
                        return "";
                    return split[1].trim();
                }));
    }

    public static Map<String, String> getNamedFragments(String docBlock){
        return Arrays.stream(docBlock.split("\\\\?\\*\\s*@"))
                .map(Util::stripComments)
                .filter(block -> block.startsWith(TestSections.testFragment.toString()))
                .filter(block -> block.split("[\\s\\n]",3).length>2)
                .filter(block -> block.split("[\\s\\n]",3)[1].startsWith("$"))
                .collect(Collectors.toMap(
                        entry -> entry.split("[\\s\\n]",3)[1],
                        entry -> {
                    var split = entry.split("[\\s\\n]", 3);
                    if (split.length<3)
                        return "";
                    return split[2].trim();
                }));
    }

    public static @NotNull String setMindent(String text, int targetIndent) {
        if (text.trim().isEmpty())
            return text;
        text = text.replace("\t"," ".repeat(4));
        while(minIndentLevel(text) != targetIndent) {
            int mindent = minIndentLevel(text);
            text = Arrays.stream(text.split("\n")).map(line-> {
                if (mindent< targetIndent)
                    return  " ".repeat(targetIndent -mindent) + line;
                return line.substring(mindent- targetIndent);
            }).collect(Collectors.joining("\n"));
        }
        return text;
    }

    static String filteredJoin(String delimiter, String[] strings){
        return Arrays.stream(strings).filter(s -> s!=null && !s.isBlank()).collect(Collectors.joining(delimiter));
    }

    public enum TestSections{
        test,
        testFragment,
        testDeclaration,
        testSetup,
        testTeardown
    }

    public static String getSection(@NotNull TestSections section, String inputBlock) {
        return getNamedSections(inputBlock).getOrDefault(section.toString(), "");
    }

    public static String stripComments(String text){
        return Arrays.stream(text.split("\n"))
                .map(s -> s.replaceAll("^\\s*/?\\*+/?\\s*","").trim())
                .collect(Collectors.joining("\n")).trim();
    }

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
}
