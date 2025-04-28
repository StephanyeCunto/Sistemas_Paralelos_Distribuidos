package com;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestExecutor {
    private String[] words;
    private String[][] searchWords;
    private int iterations;
    private int[] threads;

    public TestExecutor(String[] words, String[][] searchWords, int iterations, int[] threads) {
        this.words = words;
        this.searchWords = searchWords; 
        this.iterations = iterations;
        this.threads = threads;
    }

    public int[][] runTestsSequencial() {
        int[][] timeSequentialSearchWords = new int[this.searchWords.length][this.iterations];

        for (int i = 0; i < this.searchWords.length; i++) {
            for (int j = 0; j < this.iterations; j++) {
                Sequencial sequencial = new Sequencial(this.words, this.searchWords[i]);
                timeSequentialSearchWords[i][j] = (int) sequencial.getTime();
            }
        }
        return timeSequentialSearchWords;
    }

    public int[][][] runTestsParalelo() {
        int[][][] timeParallelSearchWords = new int[this.searchWords.length][this.threads.length][this.iterations];

        for (int i = 0; i < this.searchWords.length; i++) {
            for (int j = 0; j < this.threads.length; j++) {
                for (int k = 0; k < this.iterations; k++) {
                    Paralelo paralelo = new Paralelo(this.threads[j], this.words, this.searchWords[i]);
                    timeParallelSearchWords[i][j][k] = (int) paralelo.getTime();
                }
            }
        }
        return timeParallelSearchWords;
    }

    public Map<String, List<Integer>> getWordsCount() {
        Map<String, List<Integer>> wordMap = new HashMap<>();

        for (int i = 0; i < this.searchWords.length; i++) {
            for (int j = 0; j < this.iterations; j++) {
                Sequencial sequencial = new Sequencial(words, searchWords[i]);

                 if (j == iterations - 1) {
                    sequencial.getWordMap().forEach((key, value) -> {
                        List<Integer> list = new ArrayList<>();
                        list.add(value);
                        list.add(sequencial.getSearchWordsCount()[value]);
                        wordMap.put(key, list);
                    });
                }
            }
        }
        return wordMap;
    }
}
