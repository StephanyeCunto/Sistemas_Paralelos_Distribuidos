package com;

import lombok.Getter;

import java.util.Arrays;

import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.apache.commons.math3.stat.descriptive.rank.Percentile;
import org.apache.commons.math3.stat.StatUtils;

@Getter
public class ProcessData {
    private int[][] timeSequencial;
    private int[][][] timeParalelo;
    private int iterations;
    private int[] threads;

    private double[] timeSequencialAverage;
    private double[][] timeParaleloAverage;

    private double[] timeSequencialStdDev;
    private double[][] timeParaleloStdDev;

    private double[][] speedup;
    private double[][] efficiency;

    private final static int WARMUP = 2;

    public ProcessData(int[][] timeSequencial, int[][][] timeParalelo, int iterations, int[] threads) {
        this.timeSequencial = timeSequencial;
        this.timeParalelo = timeParalelo;
        this.iterations = iterations;
        this.threads = threads;

        this.timeSequencialAverage = new double[timeSequencial.length];
        this.timeSequencialStdDev = new double[timeSequencial.length];

           this.timeParaleloAverage = new double[timeParalelo.length][threads.length];
           this.timeParaleloStdDev = new double[timeParalelo.length][threads.length];

           this.speedup = new double[timeParalelo.length][threads.length];
           this.efficiency = new double[timeParalelo.length][threads.length];

        processData();
    }


    private void processData(){
        removeWarmUp(); 
        removeOutliers();
        calculateStatistics();
        calculateSpeedupEfficiency();
    }

    private void removeWarmUp(){
        int newSize = iterations - WARMUP;
        iterations = newSize;

        int[][] newTimeSequencial = new int[timeSequencial.length][newSize];
        for (int i = 0; i < timeSequencial.length; i++) {
            System.arraycopy(timeSequencial[i], WARMUP, newTimeSequencial[i], 0, newSize);
        }
        this.timeSequencial = newTimeSequencial;

        int[][][] newTimeParalelo = new int[timeSequencial.length][threads.length][newSize];
           for(int i = 0; i < timeSequencial.length; i++){
               for(int j = 0; j < threads.length; j++){
                   System.arraycopy(timeParalelo[i][j], WARMUP, newTimeParalelo[i][j], 0, newSize);
               }
           }
           this.timeParalelo = newTimeParalelo;
    }

    private void removeOutliers(){
        for (int i = 0; i < timeSequencial.length; i++) {
            this.timeSequencial[i] = removeOutliersFromArray(timeSequencial[i]);
        }
           for(int i = 0; i< timeParalelo.length; i++){
               for (int j = 0; j < this.threads.length; j++) {
                   this.timeParalelo[i][j] = removeOutliersFromArray(timeParalelo[i][j]);
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

    private void calculateStatistics(){
        StandardDeviation stdDev = new StandardDeviation();

        for(int i = 0; i< timeSequencial.length; i++){
            double[] sequencialDouble = Arrays.stream(timeSequencial[i]).asDoubleStream().toArray();
            this.timeSequencialAverage[i] = StatUtils.mean(sequencialDouble);
            this.timeSequencialStdDev[i] = stdDev.evaluate(sequencialDouble);
        }
  
        for(int i = 0; i< timeParalelo.length; i++){
            for(int j =0 ; j < threads.length; j++){
                double[] paraleloDouble = Arrays.stream(timeParalelo[i][j]).asDoubleStream().toArray();
                this.timeParaleloAverage[i][j] = StatUtils.mean(paraleloDouble);
                this.timeParaleloStdDev[i][j] = stdDev.evaluate(paraleloDouble);
            }
        }  
    }

    private void calculateSpeedupEfficiency(){
        for(int i = 0; i< timeParalelo.length; i++){
            for(int j =0 ; j < threads.length; j++){
                this.speedup[i][j] = this.timeSequencialAverage[i] / this.timeParaleloAverage[i][j];
                this.efficiency[i][j] = this.speedup[i][j] / threads[j];
            }
        }
    }

    public void print() {
        System.out.println("============================================== RESULTADOS DE PERFORMANCE ===============================================");
    
        for (int i = 0; i < this.timeSequencial.length; i++) {
            System.out.println("🔍 CONJUNTO DE PALAVRAS " + (i + 1));
    
            System.out.printf("│ SEQUENCIAL    │ Tempo médio: %8.2f ms │ Desvio padrão: %8.2f ms │\n", 
                              this.timeSequencialAverage[i], 
                              this.timeSequencialStdDev[i]);
    
            System.out.println("├───────────────┼─────────────────────────┼──────────────────────────┼───────────────────┼──────────────────────────────┤");
            System.out.println("│   PARALELO    │       TEMPO MÉDIO       │      DESVIO PADRÃO       │      SPEEDUP      │          EFICIÊNCIA          │");
            System.out.println("├───────────────┼─────────────────────────┼──────────────────────────┼───────────────────┼──────────────────────────────┤");
    
            for (int j = 0; j < this.threads.length; j++) {
                System.out.printf("│ %2d Threads    │ %8.2f ms             │ %8.2f ms              │ %6.2fx           │  %6.2f%%                     │\n", 
                                  this.threads[j],
                                  this.timeParaleloAverage[i][j],
                                  this.timeParaleloStdDev[i][j],
                                  this.speedup[i][j],
                                  this.efficiency[i][j] * 100);
            }
    
            System.out.println("└───────────────┴─────────────────────────┴──────────────────────────┴───────────────────┴──────────────────────────────┘");
            System.out.println();
  
        }
    
        System.out.println("=======================================================================================================================");
    }
    
}
