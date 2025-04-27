package com;

import java.util.HashMap;
import java.util.Map;

public class Paralelo {
    private int threads;
    private int wordsPerThread;
    private String[] words;
    private int[] searchWordsCount;
    private Map<String, Integer> wordMap;
    private long startTime;
    private long endTime;
    private long time;

    public Paralelo(int threads,String[] words,String[] searchWords) {
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
        this.startTime = System.nanoTime()/10000;
    }

    private void setEndTime() {
        this.endTime = System.nanoTime()/10000;
        this.time = endTime - startTime;
    }

    private void startThreads(){
        Thread[] thread = new Thread[threads];

        for (int i = 0; i < threads; i++) {
            int index = i;
            thread[i] = new Thread(()->{
                searchWords(index);
            });
        }

        for (int i = 0; i < threads; i++) {
            thread[i].start();
        }
        
        for (int i = 0; i < threads; i++) {
            try {
                thread[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
