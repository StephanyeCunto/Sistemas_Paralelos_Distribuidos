package com;

import java.util.HashMap;
import java.util.Map;

public class Sequencial {
    private String[] words;
    private int[] searchWordsCount;
    private Map<String, Integer> wordMap;
    private long startTime;
    private long endTime;
    private long time;

    public Sequencial(String[] words,String[] searchWords) {
        this.words = words;
        this.searchWordsCount = new int[searchWords.length];
        this.wordMap = new HashMap<>();
        for (int i = 0; i < searchWords.length; i++) {
            this.wordMap.put(searchWords[i], i);
        }

        setStartTime();
        searchWords();
        setEndTime();    
    }

    private void setStartTime() {
        this.startTime = System.nanoTime()/10000;
    }

    private void setEndTime() {
        this.endTime = System.nanoTime()/10000;
        this.time = endTime - startTime;
    }

    private void searchWords() {
        for (String word : words) {
            Integer index = wordMap.get(word);
            if (index != null) { 
                searchWordsCount[index]++;
            }
        }
    }

    public void print() {
        for (Map.Entry<String, Integer> entry : wordMap.entrySet()) {
            System.out.println("Palavra " + entry.getKey() + " encontrada " + searchWordsCount[entry.getValue()] + " vezes.");
        }

        System.out.println("Total de palavras: " + words.length);
    }

    public long getTime() {
        return time;
    }
}