package parallelprograming.tasklevelparallelism.practice;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 * Using {@link ForkJoinPool} by divide and conquer Env. setup: JDK 1.8 Windows 10 Core i5 1.60Ghz
 * 1.80 Ghz 4 cores RAM 16 GB Numbers of records = 300999000
 *
 * <p>Amdahl´s law speedUp = 1/q q= fraction of the sequential flow SpeedUp = 1/378 = 0.007 percent
 * of parallelism overload
 *
 * <p>Result 1: Threshold = 100000 Cores = 2
 * sequential completed in 377.724 milliseconds with sum =20.09983
 * Parallel completed in 201.361 milliseconds with sum = 20.09983
 *
 * <p>
 *
 * <p>Result 1: Threshold = 100000 Cores = 3
 * sequential completed in 375.477 milliseconds with sum = 20.09983
 * Parallel completed in 186.154 milliseconds with sum = 20.09983
 *
 * In this case add a 4 cores will be worthless.
 * @author Alexander Bravo
 */
public class ReciprocalSumForJoin {
  public static void main(String[] args) {
    //
      int limit = 300999000;
      //Number of workers it depends how many core your machine have.
      System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "3");
      double[] x = new double[limit];
      for(int i = 0; i < limit; i++) x[i] = (1 + i);

      seqSum(x);
      parallelSum(x);

  }

    public static void printResult(String m, long time, double result) {
        System.out.printf(
            " %s completed in %8.3f milliseconds with sum = %8.5f \n", m, time / 1e6, result);
    }

    public static double parallelSum(double[] x){
        long starTime = System.nanoTime();
        sumArrayParallel t = new sumArrayParallel(x, 0, x.length);
        ForkJoinPool.commonPool().invoke(t);
        double sum = t.ans;
        long finalTime = System.nanoTime() - starTime;
        printResult("Parallel ", finalTime, sum);
        return sum;
    }

    public static double seqSum(double[] x) {
        long starTime = System.nanoTime();
        double sum = 0D;

        for (double v : x) {
            sum += 1 / v;
        }
        long finalTime = System.nanoTime() - starTime;
        printResult("sequential ", finalTime, sum);
        return sum;
    }

    private static class sumArrayParallel extends RecursiveAction{
    // The threshold plays a important roll, a less threshold more overload, that means a poor
    // performance.
    // lets say, int THRESHOLD_SEQ = 1000 and two worker core and 100.999.000 records giver the
    // result below:
    //
    // sequential  completed in  125.455 milliseconds with sum = 19.00784
    // Parallel  completed in  213.421 milliseconds with sum = 19.00784
    //
    // In this case the better threshold would be a 100000
    //
    // sequential  completed in  126.818 milliseconds with sum = 19.00784
    // Parallel  completed in   68.436 milliseconds with sum = 19.00784
    static int THRESHOLD_SEQ = 100000;
        int lo;
        int hi;
        double arr[];
        double ans = 0;

        public sumArrayParallel(double[] arr, int lo, int hi)
        {
            this.lo = lo;
            this.hi = hi;
            this.arr = arr;
        }

        @Override
        protected void compute()
        {
            if ((hi - lo) <= THRESHOLD_SEQ){
                for (int i = lo; i < hi; ++i){
                    ans += 1 / arr[i];
                }
            }else{
                sumArrayParallel left = new sumArrayParallel(arr, lo, (lo + hi) / 2);
                sumArrayParallel right = new sumArrayParallel(arr, (lo + hi) / 2, hi);
                left.fork();
                right.compute();
                left.join();
                ans =left.ans + right.ans;
            }

        }
    }
}
