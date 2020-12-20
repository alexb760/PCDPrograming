package parallelprograming.week3.streams;

/** @author Alexander Bravo */
public class Run {

  public static void printMessageTimeInMilliseconds(String mess, long time, double result) {
    System.out.printf(
        " %s completed in %8.3f milliseconds with sum = %8.5f \n%n", mess, time / 1e6, result);
  }

  public static double averageStreamParallelPerson(Person[] person){
      long startTime = System.nanoTime();
      double media = Analytics.averageOfAgePerson(person);
      long finalTime = System.nanoTime() - startTime;
      printMessageTimeInMilliseconds("Parallel Media Person", finalTime, media);
      return media;
  }
    public static double varianceStreamParallelPerson(Person[] person, double mediaAge){
        long startTime = System.nanoTime();
        double variance = Analytics.varianceOfTheAgePerson(person, mediaAge);
        long finalTime = System.nanoTime() - startTime;
        printMessageTimeInMilliseconds("Parallel Media Person", finalTime, variance);
        return variance;
    }

  public static void main(String[] args) {
    Person[] person = DataGenerates.generateStudentData();
    double media = averageStreamParallelPerson(person);
    double variance = varianceStreamParallelPerson(person, media);
  }
}
