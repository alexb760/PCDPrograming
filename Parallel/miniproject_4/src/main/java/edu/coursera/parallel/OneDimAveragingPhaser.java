package edu.coursera.parallel;

import java.util.concurrent.Phaser;

/**
 * Wrapper class for implementing one-dimensional iterative averaging using
 * phasers.
 */
public final class OneDimAveragingPhaser {
    /**
     * Default constructor.
     */
    private OneDimAveragingPhaser() {
    }

    /**
     * Sequential implementation of one-dimensional iterative averaging.
     *
     * @param iterations The number of iterations to run
     * @param myNew A double array that starts as the output array
     * @param myVal A double array that contains the initial input to the
     *        iterative averaging problem
     * @param n The size of this problem
     */
    public static void runSequential(final int iterations, final double[] myNew,
            final double[] myVal, final int n) {
        double[] next = myNew;
        double[] curr = myVal;

        for (int iter = 0; iter < iterations; iter++) {
            for (int j = 1; j <= n; j++) {
                next[j] = (curr[j - 1] + curr[j + 1]) / 2.0;
            }
            double[] tmp = curr;
            curr = next;
            next = tmp;
        }
    }

    /**
     * An example parallel implementation of one-dimensional iterative averaging
     * that uses phasers as a simple barrier (arriveAndAwaitAdvance).
     *
     * @param iterations The number of iterations to run
     * @param myNew A double array that starts as the output array
     * @param myVal A double array that contains the initial input to the
     *        iterative averaging problem
     * @param n The size of this problem
     * @param tasks The number of threads/tasks to use to compute the solution
     */
    public static void runParallelBarrier(final int iterations,
            final double[] myNew, final double[] myVal, final int n,
            final int tasks) {
        Phaser ph = new Phaser(0);
        ph.bulkRegister(tasks);

        Thread[] threads = new Thread[tasks];

        for (int ii = 0; ii < tasks; ii++) {
            final int i = ii;

            threads[ii] = new Thread(() -> {
                double[] threadPrivateMyVal = myVal;
                double[] threadPrivateMyNew = myNew;

                final int chunkSize = (n + tasks - 1) / tasks;
                final int left = (i * chunkSize) + 1;
                int right = (left + chunkSize) - 1;
                if (right > n) right = n;

                for (int iter = 0; iter < iterations; iter++) {
                    for (int j = left; j <= right; j++) {
                        threadPrivateMyNew[j] = (threadPrivateMyVal[j - 1]
                            + threadPrivateMyVal[j + 1]) / 2.0;
                    }
                    ph.arriveAndAwaitAdvance();

                    double[] temp = threadPrivateMyNew;
                    threadPrivateMyNew = threadPrivateMyVal;
                    threadPrivateMyVal = temp;
                }
            });
            threads[ii].start();
        }

        for (int ii = 0; ii < tasks; ii++) {
            try {
                threads[ii].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

  /**
   * A parallel implementation of one-dimensional iterative averaging that uses the Phaser.arrive
   * and Phaser.awaitAdvance APIs to overlap computation with barrier completion.
   *
   * <p>TODO Complete this method based on the provided runSequential and runParallelBarrier
   * methods.
   *
   * @param iterations The number of iterations to run
   * @param myNew A double array that starts as the output array
   * @param myVal A double array that contains the initial input to the iterative averaging problem
   * @param n The size of this problem
   * @param tasks The number of threads/tasks to use to compute the solution
   */
  //    public static void runParallelFuzzyBarrier(final int iterations,
  //            final double[] myNew, final double[] myVal, final int n,
  //            final int tasks) {
  //
  //        Phaser ph = new Phaser(0);
  //        ph.bulkRegister(tasks);
  //
  //        Phaser[] phasers = new Phaser[tasks];
  //        for(int i = 0; i < phasers.length; i++){
  //            phasers[i] = new Phaser(1);
  //        }
  //
  //        Thread[] threads = new Thread[tasks];
  //
  //        for (int k = 0; k < tasks; k++){
  //            final int j = k;
  //
  //            threads[k] = new Thread(() -> {
  //                double[] internalArrVal = myVal;
  //                double[] internalArrNew = myNew;
  //
  //                for (int t = 0; t < iterations; t++){
  //                    //divide the arrays in parts base in the numbers of tasks
  //                    final int leftPart = j * (n / tasks) +1;
  //                    final int rightPart = (j + 1) * (n / tasks);
  //
  //                    for(int i = leftPart; i <= rightPart - 1; i++)
  //                        internalArrNew[i] = (internalArrVal[i-1] + internalArrVal[i+1]) / 2.0;
  //
  //                    //wait for previous phase to complete before advancing
  //                    int signalArrive = phasers[j].arrive();
  ////                    System.out.println("Signal task " + signalArrive);
  //                    if(j-1 >= 0){
  //                        phasers[j - 1].awaitAdvance(signalArrive);
  //                    }
  //                    if (j +1 < tasks){
  //                        phasers[j + 1].awaitAdvance(signalArrive);
  //                    }
  //
  //                    double[] tmp = internalArrNew;
  //                    internalArrNew = internalArrVal;
  //                    internalArrVal = tmp;
  //                }
  //            });
  //            threads[k].start();
  //        }
  //
  //        for (int j = 0; j < tasks; j++){
  //            try{
  //                threads[j].join();
  //            }catch (InterruptedException e){
  //                e.printStackTrace();
  //            }
  //        }
  //    }

  // 13 min long
  //    public static void runParallelFuzzyBarrier(final int iterations,
  //            final double[] myNew, final double[] myVal, final int n,
  //            final int tasks) {
  //
  //        Phaser ph = new Phaser(0);
  //        ph.bulkRegister(tasks);
  //
  //        Thread[] threads = new Thread[tasks];
  //
  //        for (int k = 0; k < tasks; k++){
  //            final int j = k;
  //
  //            threads[k] = new Thread(() -> {
  //                double[] internalArrVal = myVal;
  //                double[] internalArrNew = myNew;
  //                final int chunkSize = (n + tasks - 1) / tasks;
  //                final int left = (j * chunkSize) +1;
  //                int right = (left + chunkSize) -1 ;
  //                if (right > n) right = n;
  //
  //                for (int t = 0; t < iterations; t++){
  //                    internalArrNew[left] = (internalArrVal[left - 1] + internalArrVal[left +1])
  // / 2.0;
  //                    internalArrNew[right] = (internalArrVal[right - 1] + internalArrVal[right
  // +1]) / 2.0;
  //
  //                    int currentPhase = ph.arrive();
  //
  //                    for(int i = left + 1; i <= right - 1; i++)
  //                        internalArrNew[i] = (internalArrVal[i - 1] + internalArrVal[i + 1]) /
  // 2.0;
  //
  //                    //wait for previous phase to complete before advancing
  //                    ph.awaitAdvance(currentPhase);
  //
  //                    double[] tmp = internalArrNew;
  //                    internalArrNew = internalArrVal;
  //                    internalArrVal = tmp;
  //                }
  //            });
  //            threads[k].start();
  //        }
  //
  //        for (int j = 0; j < tasks; j++){
  //            try{
  //                threads[j].join();
  //            }catch (InterruptedException e){
  //                e.printStackTrace();
  //            }
  //        }
  //    }

  public static void runParallelFuzzyBarrier(
      final int iterations,
      final double[] myNew,
      final double[] myVal,
      final int n,
      final int tasks) {

    Phaser[] phs = new Phaser[tasks];
    for (int i = 0; i < phs.length; i++) {
      phs[i] = new Phaser(1);
      phs[i].bulkRegister(tasks);
    }

    Thread[] threads = new Thread[tasks];

    for (int ii = 0; ii < tasks; ii++) {
      final int i = ii;

      threads[ii] =
          new Thread(
              () -> {
                double[] threadPrivateMyVal = myVal;
                double[] threadPrivateMyNew = myNew;

                for (int iter = 0; iter < iterations; iter++) {
                  final int left = i * (n / tasks) + 1;
                  final int right = (i + 1) * (n / tasks);
                  if (left - 1 >= 0 && left + 1 < threadPrivateMyNew.length) {
                    threadPrivateMyNew[left] =
                        (threadPrivateMyNew[left - 1] + threadPrivateMyNew[left + 1]) / 2.0;
                  }
                  if (right - 1 >= 0 && right + 1 <= threadPrivateMyNew.length) {
                    threadPrivateMyNew[right] =
                        (threadPrivateMyNew[right - 1] + threadPrivateMyNew[right + 1]) / 2.0;
                  }

                  phs[i].arrive();
                  for (int j = left; j <= right; j++) {
                    threadPrivateMyNew[j] =
                        (threadPrivateMyVal[j - 1] + threadPrivateMyVal[j + 1]) / 2.0;
                  }

                  if (i - 1 >= 0) {
                    phs[i - 1].awaitAdvance(1);
                  }
                  if (i + 1 < tasks) {
                    phs[i + 1].awaitAdvance(1);
                  }

                  double[] temp = threadPrivateMyNew;
                  threadPrivateMyNew = threadPrivateMyVal;
                  threadPrivateMyVal = temp;
                }
              });
      threads[ii].start();
    }
    for (int ii = 0; ii < tasks; ii++) {
      try {
        threads[ii].join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
