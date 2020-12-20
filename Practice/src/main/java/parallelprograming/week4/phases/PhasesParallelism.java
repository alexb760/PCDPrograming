package parallelprograming.week4.phases;

import java.util.Arrays;
import java.util.concurrent.Phaser;

/**
 * Some results
 * Run num 0
 * sequential: completed in    7.550 seconds
 * Phases: completed in    2.917 seconds
 * Run num 1
 * sequential: completed in    7.336 seconds
 * Phases: completed in    2.925 seconds
 *
 * @author Alexander Bravo
 */
public class PhasesParallelism {

    public static void doWork(int unitsWork){
        int sum = 0;
        for (int i = 0;  i < unitsWork; i++){
            sum ++;
        }
    }

    //A bad fibonacci implementation just as example.
    public static long fib(int unit){
        if(unit <= 2) return 1;
        return fib(unit - 1) + fib(unit - 2);
    }

    public static void main(String[] args) {
        final int n = 100;
        final int units = 35;


        for (int numRun = 0; numRun < 5; numRun ++){
            System.out.printf("Run num %d\n", numRun);
            final Phaser ph0 = new Phaser(1);
            final Phaser ph1 = new Phaser(1);

            Thread t0 = new Thread(() ->{
                for (int i = 0; i < n; i++){
                    fib(units);
                    ph0.arrive();
                }
            });

            Thread t1 = new Thread(() ->{
                for (int i = 0; i < n; i++){
                    ph0.awaitAdvance(i);
                    fib(units);
                    ph1.arrive();
                }
            });

            Thread t2 = new Thread(() ->{
                for (int i = 0; i < n; i++){
                    ph1.awaitAdvance(i);
                    fib(units);
                }
            });

            //Sequential.
            long starTime = System.nanoTime();
            for (int i = 0; i<n; i++){
                fib(units);
                fib(units);
                fib(units);
            }
            long endTime = System.nanoTime() - starTime;
            System.out.printf("sequential: completed in %8.3f seconds\n", endTime / 1e9);

            //Parallel using Phases
            long startTimePhases = System.nanoTime();
            t0.start();
            t1.start();
            t2.start();
            try
            {
                t0.join();
                t1.join();
                t2.join();
            }catch (InterruptedException e){
                System.out.println(Arrays.toString(e.getStackTrace()));
            }
            long endTimePhases = System.nanoTime() - startTimePhases;
            System.out.printf("Phases: completed in %8.3f seconds\n", endTimePhases / 1e9);
        }

    }
}
