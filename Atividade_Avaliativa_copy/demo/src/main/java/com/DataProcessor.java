package com;

import java.util.Arrays;

import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.apache.commons.math3.stat.descriptive.rank.Percentile;

public class DataProcessor {
    private int WARMUP=3;
    
    private int[][] timeSequencialSearchWords;
    private int[][][] timeParaleloSearchWords;
    private int[] threads;
    private int iterations;
    private int wordGroup;

    private double[] timeSequencialAverage;
    private double[][] timeParaleloAverage;

    private double[] timeSequencialStd;
    private double[][] timeParaleloStd;

    public DataProcessor(int[][] timeSequencialSearchWords,int[][][] timeParaleloSearchWords,int[] threads,int iterations, int wordGroup){
        this.timeSequencialSearchWords = timeSequencialSearchWords;
        this.timeParaleloSearchWords = timeParaleloSearchWords;
        this.iterations = iterations;
        this.threads = threads;
        this.wordGroup = wordGroup;

        this.timeSequencialAverage = new double[this.wordGroup];
        this.timeParaleloAverage = new double[this.wordGroup][threads.length];

        this.timeSequencialStd = new double[this.wordGroup];
        this.timeParaleloStd = new double[this.wordGroup][threads.length];

        removeWarmUp();
        removeOutliers();
        calculateAverage();
        calculateStd();
        printAllData();
    }

    private void removeWarmUp(){        
        for (int i = 0; i < this.wordGroup; i++) {
            timeSequencialSearchWords[i] = Arrays.copyOfRange(timeSequencialSearchWords[i], WARMUP, iterations);
            for (int j = 0; j < this.threads.length; j++) {
                timeParaleloSearchWords[i][j] = Arrays.copyOfRange(timeParaleloSearchWords[i][j], WARMUP, iterations);
            }
        }
        this.iterations = timeParaleloSearchWords[0].length;
    }

    private void removeOutliers(){
        for (int i = 0; i < this.wordGroup; i++) {
            this.timeSequencialSearchWords[i] = removeOutliersFromArray(timeSequencialSearchWords[i]);
            
            for (int j = 0; j < this.threads.length; j++) {
                this.timeParaleloSearchWords[i][j] = removeOutliersFromArray(timeParaleloSearchWords[i][j]);
            }
        }
    }

    private int[] removeOutliersFromArray(int[] time){
        double[] doubleArray = Arrays.stream(time).asDoubleStream().toArray();

        Percentile percentile = new Percentile();
        percentile.setData(doubleArray);
        
        double q1 = percentile.evaluate(25);
        double q3 = percentile.evaluate(75);
        double iqr = q3 - q1;
        double lowerBound = q1 - (1.5 * iqr);
        double upperBound = q3 + (1.5 * iqr);
        
        return Arrays.stream(time).filter(times -> times >= lowerBound && times<= upperBound).toArray();
    }

    private void calculateAverage() {
        for(int i=0; i<this.wordGroup; i++){
            double[] doubleArraySequencial = Arrays.stream(this.timeSequencialSearchWords[i]).asDoubleStream().toArray();
            this.timeSequencialAverage[i] = new Mean().evaluate(doubleArraySequencial);

            for(int j=0; j<threads.length; j++){              
                double[] doubleArrayParalelo = Arrays.stream(this.timeParaleloSearchWords[i][j]).asDoubleStream().toArray();
                this.timeParaleloAverage[i][j] = new Mean().evaluate(doubleArrayParalelo);
            }
        }
    }

    private void calculateStd(){
        for(int i=0; i < this.wordGroup; i++){
            double[] doubleArraySequencial = Arrays.stream(this.timeSequencialSearchWords[i]).asDoubleStream().toArray();
            this.timeSequencialStd[i] = new StandardDeviation().evaluate(doubleArraySequencial);

            for(int j=0; j < threads.length; j++){
                double[] doubleArrayParalelo = Arrays.stream(this.timeParaleloSearchWords[i][j]).asDoubleStream().toArray();
                this.timeParaleloStd[i][j] = new StandardDeviation().evaluate(doubleArrayParalelo);
            }
        }
    }

    public void printAllData() {
        System.out.println("=== RESULTADOS COMPLETOS ===\n");
    
        for (int i = 0; i < wordGroup; i++) {
            System.out.println("Grupo de palavras #" + (i + 1));
    
            // Sequencial
            System.out.printf("  → Sequencial:\n");
            System.out.printf("    - Média: %.2f ms\n", timeSequencialAverage[i]);
            System.out.printf("    - Desvio padrão: %.2f ms\n", timeSequencialStd[i]);

            // Paralelo
            for (int j = 0; j < threads.length; j++) {
                System.out.printf("  → Paralelo com %d threads:\n", threads[j]);
                System.out.printf("    - Média: %.2f ms\n", timeParaleloAverage[i][j]);
                System.out.printf("    - Desvio padrão: %.2f ms\n", timeParaleloStd[i][j]);
            }
    
            System.out.println(); // linha em branco para separar grupos
        }
    }
    
}
