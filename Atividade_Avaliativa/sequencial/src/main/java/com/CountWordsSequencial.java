package com;

import java.io.*;
import java.util.*;

public class CountWordsSequencial {
    String[] words;
    int[] searchWordsCount;

    Map<String, Integer> wordMap = new HashMap<>();
    
    public static void main(String[] args) {
        new CountWordsSequencial().run(args);
    }

    private void run(String[] args){
        long startTime = System.currentTimeMillis();

        initialize(args);

        long endTime = System.currentTimeMillis();
        System.out.println((endTime - startTime));

        loadResults();
    }

    private void initialize(String[] args){
        searchWordsCount = new int[args.length];

        for(int i=0; i< args.length; i++) wordMap.put(args[i], i);

        loadWords();
    }

   private void loadWords(){
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String line;
            while((line = reader.readLine()) != null) searchWord(line);
        }catch(IOException e){
            System.out.println("Erro ao receber dados: "+e);
        }
    }

    private void searchWord(String word){
        Integer index = wordMap.get(word);
        if (index != null) searchWordsCount[index]++;
    }

    private void loadResults(){
        for(String key : wordMap.keySet()){
            System.out.println("Key: "+ key+ " : "+ searchWordsCount[wordMap.get(key)]);
        }
    }     
}