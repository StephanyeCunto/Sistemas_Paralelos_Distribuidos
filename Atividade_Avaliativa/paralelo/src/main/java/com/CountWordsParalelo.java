package com;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
        intiializeThread();
      //  loadResults();
    }

    private void initialize(String[] args){
        threads = Integer.parseInt(args[args.length - 1]);

        searchWordsCount = new AtomicInteger[args.length - 1];
        for (int i = 0; i < searchWordsCount.length; i++) {
            searchWordsCount[i] = new AtomicInteger();
        }

        for(int i=0; i< args.length-1; i++){
            wordMap.put(args[i], i);
        }

        loadWords();
    }

    private void loadWords(){
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            ArrayList<String> wordsAux = new ArrayList<>();
            String line;
            while((line = reader.readLine()) != null){
                wordsAux.add(line);
            }

            words = wordsAux.toArray(new String[0]);
        }catch(IOException e){
            System.out.println("Erro ao receber dados: "+e);
        }
    }

    private void intiializeThread() {
        Thread[] thread = new Thread[threads];

        int wordsPerThread = words.length/threads;

        for(int i=0; i< threads; i++){
            int index = i;
            thread[i] = new Thread(()->searchWord(index, wordsPerThread));
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

    private void searchWord(int i, int wordsPerThread){
        int wordInitial = i*wordsPerThread;
        int limite = wordInitial + wordsPerThread;

        if(i == threads-1) limite = words.length;

        for(int j=wordInitial; j< limite; j++){
            Integer index = wordMap.get(words[j]);
            if (index != null) searchWordsCount[index].incrementAndGet();
        }
    }

    private void loadResults(){
        for(String key : wordMap.keySet()){
            System.out.println("Key: "+ key+ " : "+ searchWordsCount[wordMap.get(key)].get());
        }
    }
}