package v2ch01.collecting;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CollectingResults {
    public static Stream<String> noVowels() throws IOException {
        String contents = Files.readString(Paths.get("./alice30.txt"), StandardCharsets.UTF_8);
        List<String> wordList = Arrays.asList(contents.split("\\PL+"));
        Stream<String> words = wordList.stream();
        return words.map(s -> s.replaceAll("[aeiouAEIOU]", ""));
    }

    public static <T> void show(String label, Set<T> set) {
        System.out.println(label + ": " + set.getClass().getName());
        System.out.println("[" + set
                .stream()
                .limit(10)
                .map(Objects::toString)
                .collect(Collectors.joining(", ")) +"");
    }

    public static void main(String[] args) throws IOException {
        Iterator<Integer> iterator = Stream
                .iterate(0, n -> n + 1)
                .limit(10)
                .iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }

        Object[] numbers = Stream
                .iterate(0, n -> n + 1)
                .limit(10)
                .toArray();

        try {
            Integer number = (Integer) numbers[0]; // OK
            System.out.println("number: " +number);
            System.out.println("The following statement throws an exception");
            Integer[] numbers2 = (Integer[]) numbers; // Throws exception
        } catch (ClassCastException e) {
            System.out.println(e);
        }

        Integer[] numbers3 = Stream
                .iterate(0, n -> n + 1)
                .limit(10)
                .toArray(Integer[]::new);
        System.out.println("Integer array: " + numbers3); // Note it's an Interger[] array

        Set<String> noVowelSet = noVowels()
                .collect(Collectors.toSet());
        show("noVowelSet", noVowelSet);

        TreeSet<String> noVowelTreeSet = noVowels()
                .collect(Collectors.toCollection(TreeSet::new));
        show("noVowelTreeSet", noVowelTreeSet);

        String result = noVowels()
                .limit(10)
                .collect(Collectors.joining());
        System.out.println("Joining: " + result);
        result = noVowels()
                .limit(10)
                .collect(Collectors.joining(", "));
        System.out.println("Joining with commas: " + result);

        IntSummaryStatistics summary = noVowels()
                .collect(Collectors.summarizingInt(String::length));
        double averageWordLength = summary.getAverage();
        double maxWordLength = summary.getMax();
        System.out.println("Average word length: " + averageWordLength);
        System.out.println("Max word length: " + maxWordLength);
        System.out.println("forEach:");
        noVowels()
                .limit(20)
                .forEach(System.out::println);
    }
}
