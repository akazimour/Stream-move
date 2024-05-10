package com.epam.rd.autotask.stream.intermediate;

import com.epam.rd.autotask.stream.intermediate.model.Genre;
import com.epam.rd.autotask.stream.intermediate.model.Movie;
import com.epam.rd.autotask.stream.intermediate.model.Person;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MovieQueries {

    private List<Movie> sharedMovies;

    public MovieQueries(List<Movie> movies) {
        if (movies == null){
            throw new IllegalArgumentException();
        }
        sharedMovies = movies;
    }


    public boolean checkGenreOfAllMovies(Genre genre) {

       return sharedMovies.stream().map(movie -> movie.getGenres()
               .stream()
               .anyMatch(genre1 -> genre1==genre))
               .allMatch(Predicate.isEqual(true));
    }

    public boolean checkGenreOfAnyMovies(Genre genre) {
        return sharedMovies.stream().map(movie -> movie.getGenres()
                        .stream().anyMatch(genre1 -> genre1==genre)).anyMatch(Predicate.isEqual(true));
    }

    public boolean checkMovieIsDirectedBy(Person person) {
      return sharedMovies.stream().map(movie -> movie.getDirectors().stream().anyMatch(
              person1 -> person1.equals(person))).anyMatch(Predicate.isEqual(true));
    }

    public int calculateTotalLength() {

       return listOfLength().stream().reduce(0,Integer::sum);
    }

    public long moviesLongerThan(int minutes) {
        return sharedMovies.stream().filter(movie -> movie.getLength()>minutes).count();
    }

    public List<Person> listOfWriters() {
        Set<Person> personSet = new HashSet<>();
        sharedMovies.forEach(movie -> personSet.addAll(movie.getWriters()));
        return new ArrayList<>(personSet);
    }

    public String[] movieTitlesWrittenBy(Person person) {
         return sharedMovies.stream().filter(movie -> movie.getWriters()
                        .stream()
                        .anyMatch(person1 -> person1.equals(person)))
                .collect(Collectors.toList())
                .stream().map(Movie::getTitle).toArray(String[]::new);
    }

    public List<Integer> listOfLength() {
       return sharedMovies.stream().map(Movie::getLength).collect(Collectors.toList());
    }

    public Movie longestMovie() {
        int i = listOfLength().stream().mapToInt(Integer::intValue).max().orElseThrow(IllegalArgumentException::new);
       return sharedMovies.stream().filter(movie -> movie.getLength()==i).findFirst().orElseThrow(IllegalArgumentException::new);
    }

    public Movie oldestMovie() {
       return sortedListOfMoviesBasedOnReleaseYear().stream().findFirst().orElseThrow(IllegalArgumentException::new);
    }

    public List<Movie> sortedListOfMoviesBasedOnReleaseYear() {
        return sharedMovies.stream().sorted(Comparator.comparingInt(Movie::getReleaseYear)).collect(Collectors.toList());
    }

    public List<Movie> sortedListOfMoviesBasedOnTheDateOfBirthOfOldestDirectorsOfMovies() {
        List<LocalDate> collect = sharedMovies.stream().map(movie -> movie.getDirectors()
                        .stream()
                        .map(Person::getDateOfBirth)
                        .min(Comparator.naturalOrder())
                        .get()).collect(Collectors.toList())
                .stream().sorted(Comparator.naturalOrder()).collect(Collectors.toList());
        return sharedMovies.stream().filter(movie -> movie.getDirectors().stream().map(Person::getDateOfBirth).allMatch(collect::contains)).sorted().collect(Collectors.toList());
    }

    public List<Movie> moviesReleasedEarlierThan(int releaseYear) {
        return sharedMovies.stream().filter(movie -> movie.getReleaseYear()<=releaseYear).collect(Collectors.toList());
    }
}
