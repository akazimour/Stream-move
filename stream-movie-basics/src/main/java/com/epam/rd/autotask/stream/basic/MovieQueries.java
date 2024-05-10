package com.epam.rd.autotask.stream.basic;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class MovieQueries {
private List<String> sharedMovies = new ArrayList<>();

    public MovieQueries(List<String> movies) {
       if (movies == null){
           throw new IllegalArgumentException();
       }
        sharedMovies = movies;
    }

    public long getNumberOfMovies() {
        return sharedMovies.stream().count();
    }

    public long getNumberOfMoviesThatStartsWith(String start) {
        return sharedMovies.stream().filter(s -> s.startsWith(start)).count();
    }

    public long getNumberOfMoviesThatStartsWithAndEndsWith(String start, String end) {
       return sharedMovies.stream().filter(s -> s.startsWith(start)&& s.endsWith(end)).count();
    }

    public List<Integer> getLengthOfTitleOfMovies() {
       return sharedMovies.stream().map(String::length).collect(Collectors.toList());
    }

    public int getNumberOfLettersInShortestTitle() {
        String retString = sharedMovies.stream().min(Comparator.comparing(s -> s.length())).orElseThrow(IllegalArgumentException::new);
        return retString.length();
    }

    public Optional<String> getFirstTitleThatContainsThreeWords() {
       return sharedMovies.stream().filter(s -> s.split(" ").length==3).findFirst();

    }

    public List<String> getFirstFourTitlesThatContainAtLeastTwoWords() {

        List<String> collect = sharedMovies.stream().filter(s -> s.split(" ").length >= 2).limit(4).collect(Collectors.toList());
               return collect;
    }

    public void printAllTitleToConsole() {
        sharedMovies.forEach(System.out::println);
    }
}
