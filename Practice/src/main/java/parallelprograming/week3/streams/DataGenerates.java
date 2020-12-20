package parallelprograming.week3.streams;

import edu.rice.pcdp.PCDP;
import java.util.Random;

/** @author Alexander Bravo */
public class DataGenerates {
  private static final String[] firstNames = {
    "Sanjay", "Yunming", "John", "Vivek", "Shams", "Max", "Javier", "Gambito", "Wolverin"
  };
  private static final String[] lastNames = {
    "Chatterjee", "Zhang", "Smith", "Sarkar", "Imam", "Grossman", "Charl", "Ross", "Logan"
  };

  public static Person[] generateStudentData() {
    final int N_PERSON = 20_000_000;
    final int N_CURRENT_PERSON = 600000;

    Person[] persons = new Person[N_PERSON];
    Random r = new Random(123);

    PCDP.forall(
        0,
        N_PERSON,
        (s) -> {
          final String firstName = firstNames[r.nextInt(firstNames.length)];
          final String lastName = lastNames[r.nextInt(lastNames.length)];
          final double age = r.nextDouble() * 100.0;
          final int grade = 1 + r.nextInt(100);
          final boolean current = (true); //s < N_CURRENT_PERSON);

          persons[s] = new Person(firstName, lastName, age, grade, current);
        });
    return null;
    }

}
