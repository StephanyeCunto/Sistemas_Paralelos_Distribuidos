package com;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import lombok.Getter;

@Getter
public class Sequencial {
    private final String[] words;    
    private final Map<String, Integer> wordMap;

    private int[] searchWordsCount;
    private long startTime;
    private long endTime;
    private long time;
    private CountDownLatch latch;

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
        try{
        Process process = new ProcessBuilder("java -cp ./Atividade_Avaliativa/de,p/main/java com.CountWords").start();
        process.waitFor();
        }catch(IOException | InterruptedException e){
            System.out.println(" Erro no processo "+e);
        }
        for (String word : words) {
            Integer index = wordMap.get(word);
            if (index != null) searchWordsCount[index]++;
        }
    }
}