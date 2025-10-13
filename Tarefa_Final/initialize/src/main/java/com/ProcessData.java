package com;

import lombok.Getter;
import java.util.Arrays;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.apache.commons.math3.stat.descriptive.rank.Percentile;
import org.apache.commons.math3.stat.StatUtils;

@Getter
public class ProcessData {
    private int[] timeSequencial;
    private int[][] timeSimples, timePool, timeForkJoin, timeVirtual;
    private int iterations;
    private int[] threads;

    private double timeSequencialAverage;
    private double timeSequencialStdDev;

    private double[] timeSimplesAverage, timePoolAverage, timeForkJoinAverage, timeVirtualAverage;
    private double[] timeSimplesStdDev, timePoolStdDev, timeForkJoinStdDev, timeVirtualStdDev;

    private double[] speedupSimples, speedupPool, speedupForkJoin, speedupVirtual;
    private double[] efficiencySimples, efficiencyPool, efficiencyForkJoin, efficiencyVirtual;

    private static final int WARMUP = 0;

    public ProcessData(int[] timeSequencial, int[][] timeSimples, int[][] timePool, int[][] timeForkJoin, int[][] timeVirtual, int iterations, int[] threads) {

        this.timeSequencial = timeSequencial;
        this.timeSimples = timeSimples;
        this.timePool = timePool;
        this.timeForkJoin = timeForkJoin;
        this.timeVirtual = timeVirtual;
        this.iterations = iterations;
        this.threads = threads;

        this.timeSimplesAverage = new double[threads.length];
        this.timePoolAverage = new double[threads.length];
        this.timeForkJoinAverage = new double[threads.length];
        this.timeVirtualAverage = new double[threads.length];

        this.timeSimplesStdDev = new double[threads.length];
        this.timePoolStdDev = new double[threads.length];
        this.timeForkJoinStdDev = new double[threads.length];
        this.timeVirtualStdDev = new double[threads.length];

        this.speedupSimples = new double[threads.length];
        this.speedupPool = new double[threads.length];
        this.speedupForkJoin = new double[threads.length];
        this.speedupVirtual = new double[threads.length];

        this.efficiencySimples = new double[threads.length];
        this.efficiencyPool = new double[threads.length];
        this.efficiencyForkJoin = new double[threads.length];
        this.efficiencyVirtual = new double[threads.length];

        processData();
    }

    private void processData() {
        removeWarmUp();
        removeOutliers();
        calculateStatistics();
        calculateSpeedupEfficiency();
    }

    private void removeWarmUp() {
        int originalSize = timeSequencial.length;
        
        int newSize = originalSize - WARMUP;
        iterations = newSize;

        this.timeSequencial = Arrays.copyOfRange(timeSequencial, WARMUP, originalSize);

        this.timeSimples = trimWarmup(timeSimples);
        this.timePool = trimWarmup(timePool);
        this.timeForkJoin = trimWarmup(timeForkJoin);
        this.timeVirtual = trimWarmup(timeVirtual);
    }

    private int[][] trimWarmup(int[][] data) {
        int[][] trimmed = new int[data.length][];
        for (int i = 0; i < data.length; i++) {
            int originalSize = data[i].length;
            trimmed[i] = Arrays.copyOfRange(data[i], WARMUP, originalSize);
        }
        return trimmed;
    }

    private void removeOutliers() {
        this.timeSequencial = removeOutliersFromArray(timeSequencial);

        for (int i = 0; i < threads.length; i++) {
            timeSimples[i] = removeOutliersFromArray(timeSimples[i]);
            timePool[i] = removeOutliersFromArray(timePool[i]);
            timeForkJoin[i] = removeOutliersFromArray(timeForkJoin[i]);
            timeVirtual[i] = removeOutliersFromArray(timeVirtual[i]);
        }
    }

    private int[] removeOutliersFromArray(int[] times) {
        
        double[] doubleArray = Arrays.stream(times).asDoubleStream().toArray();
        Percentile percentile = new Percentile();
        percentile.setData(doubleArray);

        double q1 = percentile.evaluate(25);
        double q3 = percentile.evaluate(75);
        double iqr = q3 - q1;
        double lowerBound = q1 - (1.5 * iqr);
        double upperBound = q3 + (1.5 * iqr);

        int[] filtered = Arrays.stream(times)
                               .filter(time -> time >= lowerBound && time <= upperBound)
                               .toArray();
        
        return filtered ;
    }

    private void calculateStatistics() {
        StandardDeviation stdDev = new StandardDeviation();

        double[] seqDouble = Arrays.stream(timeSequencial).asDoubleStream().toArray();
        this.timeSequencialAverage = StatUtils.mean(seqDouble);
        this.timeSequencialStdDev = stdDev.evaluate(seqDouble);

        calcStatsForParallel(timeSimples, timeSimplesAverage, timeSimplesStdDev, stdDev);
        calcStatsForParallel(timePool, timePoolAverage, timePoolStdDev, stdDev);
        calcStatsForParallel(timeForkJoin, timeForkJoinAverage, timeForkJoinStdDev, stdDev);
        calcStatsForParallel(timeVirtual, timeVirtualAverage, timeVirtualStdDev, stdDev);
    }

    private void calcStatsForParallel(int[][] data, double[] avg, double[] std, StandardDeviation stdDev) {
        for (int i = 0; i < threads.length; i++) {
            if (data[i].length == 0) {
                avg[i] = 0.0;
                std[i] = 0.0;
                continue;
            }
            double[] doubleArr = Arrays.stream(data[i]).asDoubleStream().toArray();
            avg[i] = StatUtils.mean(doubleArr);
            std[i] = stdDev.evaluate(doubleArr);
        }
    }

    private void calculateSpeedupEfficiency() {
        calcSpeedupEfficiency(timeSimplesAverage, speedupSimples, efficiencySimples);
        calcSpeedupEfficiency(timePoolAverage, speedupPool, efficiencyPool);
        calcSpeedupEfficiency(timeForkJoinAverage, speedupForkJoin, efficiencyForkJoin);
        calcSpeedupEfficiency(timeVirtualAverage, speedupVirtual, efficiencyVirtual);
    }

    private void calcSpeedupEfficiency(double[] avg, double[] speedup, double[] efficiency) {
        for (int i = 0; i < threads.length; i++) {
            speedup[i] = timeSequencialAverage / avg[i];
            efficiency[i] = speedup[i] / threads[i];
        }
    }

    public void print() {
        System.out.println("================================ RESULTADOS =================================");
        System.out.printf("Sequencial -> Média: %.2f ms | Desvio: %.2f ms\n",
                          timeSequencialAverage, timeSequencialStdDev);
        System.out.println("----------------------------------------------------------------------------");

        printParallelResults("Simples", timeSimplesAverage, timeSimplesStdDev, speedupSimples, efficiencySimples);
        printParallelResults("Pool", timePoolAverage, timePoolStdDev, speedupPool, efficiencyPool);
        printParallelResults("ForkJoin", timeForkJoinAverage, timeForkJoinStdDev, speedupForkJoin, efficiencyForkJoin);
        printParallelResults("Virtual", timeVirtualAverage, timeVirtualStdDev, speedupVirtual, efficiencyVirtual);
        System.out.println("============================================================================");
    }

    private void printParallelResults(String name, double[] avg, double[] std, double[] speedup, double[] efficiency) {
        System.out.println("[" + name + "]");
        for (int i = 0; i < threads.length; i++) {
            System.out.printf("%2d threads -> Média: %.2f ms | Desvio: %.2f ms | Speedup: %.2fx | Eficiência: %.2f%%\n",
                              threads[i], avg[i], std[i], speedup[i], efficiency[i] * 100);
        }
        System.out.println();
    }
}