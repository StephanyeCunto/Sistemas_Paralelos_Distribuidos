package com;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

@Getter
public class ParaleloVirtual {
    private final int threads;
    private final int wordsPerThread;
    private final String[] words;
    private final Map<String, Integer> wordMap;

    private int[] searchWordsCount;
    private long startTime;
    private long endTime;
    private long time;

    public ParaleloVirtual(int threads,String[] words,String[] searchWords){
        this.threads = threads;
        this.words = words;
        this.wordMap = new HashMap<>();
        for (int i = 0; i < searchWords.length; i++) {
            this.wordMap.put(searchWords[i], i);
        }
        this.searchWordsCount = new int[searchWords.length];  
        this.wordsPerThread = words.length / threads;
        setStartTime();  
        startThreads();
        setEndTime();
    }

    private void setStartTime() {
        this.startTime = System.nanoTime()/1000;
    }

    private void setEndTime() {
        this.endTime = System.nanoTime()/1000;
        this.time = endTime - startTime;
    }

    private void startThreads(){
        for (int i = 0; i < threads; i++){
            int indice = i;
            Thread.startVirtualThread(() -> {
                searchWords(indice);
            });
        }
    }

    private void searchWords(int indice){
        int threadIndex = indice * wordsPerThread;
        int limit = threadIndex + wordsPerThread;

        if(indice == threads - 1){
            limit = words.length;
        }

        for(int i = threadIndex; i < limit; i++){
            Integer index = wordMap.get(words[i]);
            if (index != null) { 
              //  synchronized (searchWordsCount) {searchWordsCount[index]++;}
               searchWordsCount[index]++;
            }
        }
    }
}
