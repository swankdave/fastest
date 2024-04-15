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

    public enum TestSections{
        test,
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
}
