package parallelprograming.week3.streams;

import java.util.Arrays;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/** @author Alexander Bravo */
public class Analytics {

    /**
     * Returns the Media of the age
     *
     * @param people
     * @return
     */
  public static double averageOfAgePerson(Person[] people) {
    return Arrays.stream(people)
        .parallel()
        .mapToDouble(Person::getAge)
        .average()
        .orElseGet(() -> 0.0);
  }

  public static double varianceOfTheAgePerson(Person[] people, final double media){
      return Arrays.stream(people)
          .parallel()
          .mapToDouble(Person::getAge)
          .map(age -> (age - media)  * (age - media))
          .sum() / people.length;
  }

  public static ConcurrentMap<Integer, Long> ageClassificationByCounting(Person[] person){
      return Arrays.stream(person)
          .parallel()
          .collect(Collectors.groupingByConcurrent(Person::getGrade, Collectors.counting()));
  }
}
