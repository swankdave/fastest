package com.github.swankdave.fastest;

import com.github.swankdave.fastest.parser.ShorTestParser;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//HashMap<SetIdentifier, HashMap<FullSetCommand,List<SetMember>>>
//example:
//      ActiveSets["SETA"]["(1,2,3)SETA"]={1,2,3};
//      ActiveSets["SETA"]["(A,B,C)SETA"]={A,B,C};

public class SetCollection extends HashMap<String, HashMap<String, List<String>>> {

    /**
     * Constructs an empty {@code HashMap} with the default initial capacity
     * (16) and the default load factor (0.75).
     */
    public SetCollection() {
    }

    /**
     * Constructs a new {@code HashMap} with the same mappings as the
     * specified {@code Map}.  The {@code HashMap} is created with
     * default load factor (0.75) and an initial capacity sufficient to
     * hold the mappings in the specified {@code Map}.
     *
     * @param m the map whose mappings are to be placed in this map
     * @throws NullPointerException if the specified map is null
     */
    public SetCollection(Map<String, Map<String, List<String>>> m) {
        super((Map)m);
    }

    /**
     * Adds a set to the SetCollection based on the provided context.
     *
     * @param ctx The context of the set method statement.
     */
    void add(ShorTestParser.Set_method_statementContext ctx) {
        if (ctx.set_keyword().SET_KEYWORD().getSymbol().getType() == ShorTestParser.SET_KEYWORD)
        {
            var mapMembers = ctx.parameterList().statement();
            if (!containsKey(Util.getFUllText(ctx.set_keyword())))
                put(Util.getFUllText(ctx.set_keyword()),new HashMap<>());
            var map = get(Util.getFUllText(ctx.set_keyword()));
            String setDefinition = Util.getFUllText(ctx);
            if (!map.containsKey(setDefinition))
                map.put(setDefinition,new ArrayList<>());
            var list = map.get(setDefinition);
            list.clear();
            list.addAll(mapMembers.stream().map(Util::getFUllText).toList());
        }
    }


    /**
     * Creates and returns a deep clone of the SetCollection object.
     *
     * @return a deep clone of the SetCollection object.
     */
    @Override
    @NotNull
    public SetCollection clone() {
        return new SetCollection(entrySet().stream().collect(
                Collectors.toMap(Entry::getKey, e -> e.getValue().entrySet().stream().collect(
                        Collectors.toMap(Entry::getKey, g -> g.getValue().stream().map(String::new).collect(Collectors.toList()))))));
    }

    /**
     * Checks if any of the fragment names in the given ActiveSetsCopy map contains the specified keyword prefix for language.
     *
     * @param fragmentNames           The list of fragment names to be searched.
     * @param keywordPrefixForLanguage The keyword prefix for language to be checked.
     * @return {@code true} if any of the fragment names contains the specified keyword prefix for language,
     *         {@code false} otherwise.
     */
    boolean containsKeyword(List<String> fragmentNames, String keywordPrefixForLanguage) {
        return this.entrySet().stream().anyMatch(
                superSet -> superSet.getValue().entrySet().stream().anyMatch(
                        set -> fragmentNames.stream().anyMatch(
                                fragmentId -> set.getKey().contains(keywordPrefixForLanguage + fragmentId))));
    }

    /**
     * Inflates test set list by replacing keywords with corresponding values from the testFragments map.
     *
     * @param testFragments            The map of test fragments containing keywords and their corresponding values.
     * @param keywordPrefixForLanguage
     * @return The inflated map of sets with replaced keywords.
     */
    public SetCollection inflateSets(Map<String, String> testFragments, String keywordPrefixForLanguage){
        return new SetCollection(this.entrySet().stream().collect(
                Collectors.toMap(
                        Entry::getKey,
                        e -> e.getValue().entrySet().stream().collect(
                                Collectors.toMap(
                                        f -> {
                                            var ref = new Object() {
                                                String key = f.getKey();
                                            };
                                            testFragments.forEach((from, to) -> ref.key = ref.key.replace(keywordPrefixForLanguage + from, to));
                                            return ref.key;
                                        }, g ->
                                                g.getValue().stream().map(item -> {
                                                            var ref = new Object() {
                                                                String value = item;
                                                            };
                                                            testFragments.forEach((from, to) -> ref.value = ref.value.replace(keywordPrefixForLanguage +from, to));
                                                            return ref.value;
                                                        }
                                                ).collect(Collectors.toList()))
                        ))));
    }

    /**
     * search for sets to contain fragments, and integrate them in to the set statements
     * SO THAT we will recognize set statements in test parts where this substitution has already
     * taken place
     *
     * @param methodScope
     * @param classScope
     * @param fragmentPrefix
     * @return A map of expanded sets with replaced keywords.
     */
    @NotNull SetCollection expandSetsWithFragments(MethodScope methodScope, ClassScope classScope, String fragmentPrefix) {
        var sets = clone();
        while(true) {
            if (containsKeyword(methodScope.getTestFragments().keySet().stream().toList(), fragmentPrefix)){
                sets = inflateSets(methodScope.getTestFragments(), fragmentPrefix);
                continue;
            }

            if (containsKeyword(methodScope.getTestData().keySet().stream().toList(), fragmentPrefix)){
                sets = inflateSets(methodScope.getTestData(), fragmentPrefix);
                continue;
            }

            if (containsKeyword(classScope.getTestFragments().keySet().stream().toList(), fragmentPrefix)){
                sets = inflateSets(classScope.getTestFragments(), fragmentPrefix);
                continue;
            }

            if (containsKeyword(classScope.getTestData().keySet().stream().toList(), fragmentPrefix)){
                sets = inflateSets(classScope.getTestData(), fragmentPrefix);
                continue;
            }
            break;
        }
        return sets;
    }

    /**
     * Expands the tests with sets.
     *
     * @param tests          The list of tests to expand.
     * @return The expanded list of tests.
     */
    List<TestConfig> expandTestsWithSets(List<TestConfig> tests) {
        //foreach map identifier (SET, SETA, SETB...)
        for (var entry : this.entrySet()) {
            //list of set groups, consisting of the literal text of the set declaration, and a list of the set members/parameters broken up
            var setGroup = entry.getValue();
            //for every defined test. (sets multiply tests)
            // results of this will result in every test returning itself, or the tests it became
            tests = tests.stream().flatMap(cfg -> cfg.expandTestWithSets(setGroup).stream()).collect(Collectors.toList());
        }
        return tests;
    }
}
