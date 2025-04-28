package com;

import java.util.Arrays;

import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.apache.commons.math3.stat.descriptive.rank.Percentile;

import lombok.Getter;

@Getter
public class DataProcessor {
    private int[][] timeSequentialSearchWords;
    private int[][][] timeParallelSearchWords;
    private double[] timeSequentialAverage;
    private double[][] timeParallelAverage;
    private double[] timeSequentialStdDev;
    private double[][] timeParallelStdDev;
    private double[][] speedup;
    private double[][] efficiency;
    private int iterations;
    private int searchWordsCount;
    private int threads[];

    public DataProcessor(int[][] timeSequentialSearchWords, int[][][] timeParallelSearchWords, int iterations, int searchWordsCount, int[] threads) {
        this.timeSequentialSearchWords = timeSequentialSearchWords;
        this.timeParallelSearchWords = timeParallelSearchWords;
        this.iterations = iterations;
        this.searchWordsCount = searchWordsCount;
        this.threads = threads;

        this.timeSequentialAverage = new double[searchWordsCount];
        this.timeParallelAverage = new double[searchWordsCount][threads.length];
        this.timeSequentialStdDev = new double[searchWordsCount];
        this.timeParallelStdDev = new double[searchWordsCount][threads.length];
        this.speedup = new double[searchWordsCount][threads.length];
        this.efficiency = new double[searchWordsCount][threads.length];

        removeWarmUp();
        removeOutliers();
        calculateStatistics();
    }

    private void removeWarmUp() {
        int warmupCount = this.iterations / 10; 
        int newSize = this.iterations - warmupCount;
        
        for (int i = 0; i < this.searchWordsCount; i++) {
            int[] newSequential = new int[newSize];
            System.arraycopy(this.timeSequentialSearchWords[i], warmupCount, newSequential, 0, newSize);
            this.timeSequentialSearchWords[i] = newSequential;
            
            for (int j = 0; j < this.threads.length; j++) {
                int[] newParallel = new int[newSize];
                System.arraycopy(this.timeParallelSearchWords[i][j], warmupCount, newParallel, 0, newSize);
                this.timeParallelSearchWords[i][j] = newParallel;
            }
        }
        
        iterations = newSize;
    }

        private void removeOutliers() {
        for (int i = 0; i < this.searchWordsCount; i++) {
            this.timeSequentialSearchWords[i] = removeOutliersFromArray(timeSequentialSearchWords[i]);
            
            for (int j = 0; j < this.threads.length; j++) {
                this.timeParallelSearchWords[i][j] = removeOutliersFromArray(timeParallelSearchWords[i][j]);
            }
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
        
        return Arrays.stream(times)
                     .filter(time -> time >= lowerBound && time <= upperBound)
                     .toArray();
    }

    private void calculateStatistics() {
        StandardDeviation stdDev = new StandardDeviation();
        
        for (int i = 0; i < this.searchWordsCount; i++) {
            double[] sequentialDouble = Arrays.stream(timeSequentialSearchWords[i])
                                             .asDoubleStream().toArray();

            this.timeSequentialAverage[i] = calculateAverage(sequentialDouble);
            this.timeSequentialStdDev[i] = stdDev.evaluate(sequentialDouble);
            
            for (int j = 0; j < this.threads.length; j++) {
                double[] parallelDouble = Arrays.stream(timeParallelSearchWords[i][j])
                                               .asDoubleStream().toArray();
                this.timeParallelAverage[i][j] = calculateAverage(parallelDouble);
                this.timeParallelStdDev[i][j] = stdDev.evaluate(parallelDouble);
                
                this.speedup[i][j] = this.timeSequentialAverage[i] / this.timeParallelAverage[i][j];
                this.efficiency[i][j] = this.speedup[i][j] / this.threads[j];
            }
        }
    }

    private double calculateAverage(double[] values) {
        double sum = 0;
        for (double value : values) {
            sum += value;
        }
        return sum / values.length;
    }
}
