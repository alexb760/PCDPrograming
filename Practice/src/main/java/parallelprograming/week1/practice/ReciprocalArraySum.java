package parallelprograming.week1.practice;

import edu.rice.pcdp.PCDP;
import java.util.concurrent.ForkJoinPool;

/** @author Alexander Bravo */
public class ReciprocalArraySum {
  public static double sum1, sum2 = 0D;

  public static void main(String[] args) {
    int threshHold = 100999000;
    double[] a = new double[threshHold];
    for (int i = 0; i < threshHold; i++) {
      a[i] = i + 2;
    }
    parallelSum(a);
    seqSum(a);
  }

  public static void printResult(String m, long time, double result) {
    System.out.printf(
        " %s completed in %8.3f milliseconds with sum = %8.5f \n", m, time / 1e6, result);
  }

  public static double seqSum(double[] x) {
    long starTime = System.nanoTime();
    sum1 = 0D;
    sum2 = 0D;

    // lowe half
    for (int i = 0; i < x.length / 2; i++) {
      sum1 += 1 / x[1];
    }
    // upper half
    for (int i = x.length / 2; i < x.length; i++) {
      sum2 += 1 / x[1];
    }
    double sum = sum1 + sum2;
    long finalTime = System.nanoTime() - starTime;
    printResult("sequential ", finalTime, sum);
    return sum;
  }

  public static double parallelSum(double[] x) {
    long starTime = System.nanoTime();
    sum1 = 0D;
    sum2 = 0D;

    PCDP.finish(() -> {
          PCDP.async(() -> {
                // lowe half
                for (int i = 0; i < x.length / 2; i++) {
                  sum1 += 1 / x[1];
                }
              });
          // upper half
          for (int i = x.length / 2; i < x.length; i++) {
            sum2 += 1 / x[1];
          }
        });
    double sum = sum1 + sum2;
    long finalTime = System.nanoTime() - starTime;
    printResult("parallel ", finalTime, sum);
    return sum;
  }
}
