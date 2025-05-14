package com;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class CountWordsParalelo {
    String[] words;
    AtomicInteger[] searchWordsCount;
    int threads;

    Map<String, Integer> wordMap = new HashMap<>();
    
    public static void main(String[] args) {
        new CountWordsParalelo().run(args);
    }

    private void run(String[] args){
        initialize(args);
        initializeThread();
    //    loadResults();
    }

    private void initialize(String[] args){
        threads = Integer.parseInt(args[args.length - 1]);

        searchWordsCount = new AtomicInteger[args.length - 1];

        for(int i=0; i< args.length-1; i++){
            wordMap.put(args[i], i);
            searchWordsCount[i] = new AtomicInteger();
        }

        loadWords();
    }

    private void loadWords(){
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            words = reader.lines().toArray(String[]::new);
        }catch(IOException e){
            System.out.println("Erro ao receber dados: "+e);
        }
    }

    private void initializeSearch(int wordInitial, int limite){
        for(int i = wordInitial; i < limite;i++){
            searchWord(words[i]);
        }
    }

    private void searchWord(String word){
        Integer index = wordMap.get(word);
        if (index != null) searchWordsCount[index].incrementAndGet();
    }

    private void initializeThread() {
        Thread[] thread = new Thread[threads];

        int wordsPerThread = words.length/threads;


        for(int i=0; i< threads; i++){
            int index = i;

            int wordInitial = index * wordsPerThread;

            int limite = (index == threads - 1) ? words.length : (index + 1) * wordsPerThread;

            thread[i] = new Thread(()-> initializeSearch(wordInitial,limite));

            thread[i].start();
        }

        for(int i=0; i< threads; i++){
            try{
            thread[i].join();
            }catch(InterruptedException e){
                System.out.println("Erro ao finalizar a thread "+i+", "+ e);
            }
        }
    }

    private void loadResults(){
        for(String key : wordMap.keySet()){
            System.out.println("Key: "+ key+ " : "+ searchWordsCount[wordMap.get(key)].get());
        }
    }
}