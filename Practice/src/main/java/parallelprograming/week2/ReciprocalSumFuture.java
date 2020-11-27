package parallelprograming.week2;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * @author Alexander Bravo
 */
public class ReciprocalSumFuture
{
    static final int REPEATS = 60;

    public static void printResult(String m, long time, double result) {
        System.out.printf(
            " %s completed in %8.3f milliseconds with sum = %8.5f \n", m, time / 1e6, result);
    }

    public static void sequentialSum(double[] arr){
        long starTime = System.nanoTime();
        double sum = 0D;
        for (double i : arr)
            sum += 1 / i;
        long finalTime = System.nanoTime() - starTime;
//        printResult("Sequential", finalTime, sum);
    }

    public static void parallelSumFuture(double[] arr){
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "3");
        long starTime = System.nanoTime();
        SumArrayFuture sumTask = new SumArrayFuture(arr, 0, arr.length);
        double sum = ForkJoinPool.commonPool().invoke(sumTask);

        long finalTime = System.nanoTime() - starTime;
//        printResult("ParallelFuture", finalTime, sum);
    }

  public static void main(String[] args) {


        int length = 200_000_000;
        double[] data = new double[length];
        for (int i = 0; i < length; i++){
            data[i] = i + 1;
        }
      /*
       * Run several repeats of the sequential and parallel versions to get an accurate measurement of parallel
       * performance.
       */
      final long seqStartTime = System.currentTimeMillis();
      for (int r = 0; r < REPEATS; r++) {
          sequentialSum(data);
      }
      final long seqEndTime = System.currentTimeMillis();

      final long parStartTime = System.currentTimeMillis();
      for (int r = 0; r < REPEATS; r++) {
          parallelSumFuture(data);
      }
      final long parEndTime = System.currentTimeMillis();

      final long seqTime = (seqEndTime - seqStartTime) / REPEATS;
      final long parTime = (parEndTime - parStartTime) / REPEATS;

      double speedup =  (double)seqTime / (double)parTime;

      System.out.println("Sequential Time: " + seqTime);
      System.out.println("Parallel Time: " + parTime);
      System.out.println("Speedup was: "+ speedup);
  }
}
class SumArrayFuture extends RecursiveTask<Double>{
    static int THRESHOLD = 5000;
    private final int lo;
    private final int hi;
    private final double[] arr;

    public SumArrayFuture(double[] arr, int lo, int hi)
    {
        this.lo = lo;
        this.hi = hi;
        this.arr = arr;
    }

    @Override
    protected Double compute()
    {
        if (hi - lo <= THRESHOLD){
            double sum = 0D;
            for (int i = lo; i < hi; ++i)
                sum += 1 / arr[i];
            return sum;
        }else{
            SumArrayFuture left = new SumArrayFuture(arr, lo, (lo + hi) / 2);
            left.fork();
            SumArrayFuture right = new SumArrayFuture(arr, (lo + hi) / 2, hi);
            return left.join() + right.compute();
        }
    }
}
