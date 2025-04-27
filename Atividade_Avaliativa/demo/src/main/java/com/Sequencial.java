package com;

import java.util.HashMap;
import java.util.Map;

public class Sequencial {
    private String[] words;
    private String[] searchWords;
    private int[] searchWordsCount;
    private long startTime;
    private long endTime;
    private long time;

    public Sequencial(String[] words,String[] searchWords) {
        this.words = words;
        this.searchWords = searchWords;
        this.searchWordsCount = new int[searchWords.length];
        setStartTime();
        searchWords();
        setEndTime();    
    }

    private void setStartTime() {
        this.startTime = System.currentTimeMillis();
    }

    private void setEndTime() {
        this.endTime = System.currentTimeMillis();
        this.time = endTime - startTime;
    }

    private void searchWords() {
        Map<String, Integer> wordMap = new HashMap<>();
        for (int i = 0; i < searchWords.length; i++) {
            wordMap.put(searchWords[i], i);
        }
    
        for (String word : words) {
            Integer index = wordMap.get(word);
            if (index != null) { 
                searchWordsCount[index]++;
            }
        }
    }

    public void print() {
        for (int i = 0; i < searchWords.length; i++) {
            System.out.println("Palavra " + searchWords[i] + " encontrada " + searchWordsCount[i] + " vezes.");
        }
        System.out.println("Total de palavras: " + words.length);
    }

    public long getTime() {
        return time;
    }
}
