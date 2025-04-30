package com;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

@Getter
public class Sequencial {
    private final String[] words;    
    private final Map<String, Integer> wordMap;


    private int[] searchWordsCount;
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
        this.startTime = System.nanoTime()/1000;
    }

    private void setEndTime() {
        this.endTime = System.nanoTime()/1000;
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

    
}