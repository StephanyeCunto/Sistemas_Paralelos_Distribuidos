package com;

public class Main {
    public static void main(String[] args) throws InterruptedException  {
        int iterations = 30;
        Sequencial sequencial = new Sequencial(iterations);

        int[] threads = {2, 4, 8};
    
        Paralelo paralelo = new Paralelo(iterations, threads);

        int[][][] timeParaleloSearchWords = paralelo.getTimeParaleloSearchWords();
        int[][] timeSequencialSearchWords = sequencial.getTimeSequencialSearchWords();

        int wordGroup = 2;

        DataProcessor dataProcessor = new DataProcessor(timeSequencialSearchWords, timeParaleloSearchWords, threads, iterations,wordGroup);
    }
}
