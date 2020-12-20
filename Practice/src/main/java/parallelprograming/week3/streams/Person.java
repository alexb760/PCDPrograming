package parallelprograming.week3.streams;

/** @author Alexander Bravo */
public class Person {
  /** First name of the student. */
  private final String firstName;
  /** Surname of the student. */
  private final String lastName;
  /** Age of the student. */
  private final double age;
  /** Grade the student has received in the class so far. */
  private final int grade;
  /** Whether the student is currently enrolled, or has already completed the course. */
  private final boolean isCurrent;

  /**
   * Constructor.
   *
   * @param setFirstName Student first name
   * @param setLastName Student last name
   * @param setAge Student age
   * @param setGrade Student grade in course
   * @param setIsCurrent Student currently enrolled?
   */
  public Person(
      final String setFirstName,
      final String setLastName,
      final double setAge,
      final int setGrade,
      final boolean setIsCurrent) {
    this.firstName = setFirstName;
    this.lastName = setLastName;
    this.age = setAge;
    this.grade = setGrade;
    this.isCurrent = setIsCurrent;
  }

  public String getFirstName()
  {
    return firstName;
  }

  public String getLastName()
  {
    return lastName;
  }

  public double getAge()
  {
    return age;
  }

  public int getGrade()
  {
    return grade;
  }

  public boolean isCurrent()
  {
    return isCurrent;
  }
}
