package com;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CountWordsSequencial {
    String[] words;
    int[] searchWordsCount;

    Map<String, Integer> wordMap = new HashMap<>();

    int timeRead;
    
    public static void main(String[] args) {
        new CountWordsSequencial().run(args);
    }

    private void run(String[] args){
        initialize(args);
        searchWords();
        loadResults();
    }

    private void initialize(String[] args){
        searchWordsCount = new int[args.length];
        
        for(int i=0; i< args.length; i++){
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

    private void searchWords() {
        for (String word : words) {
            Integer index = wordMap.get(word);
            if (index != null) searchWordsCount[index]++;
        }
    }

    private void loadResults(){
        for(String key : wordMap.keySet()){
            System.out.println("Key: "+ key+ " : "+ searchWordsCount[wordMap.get(key)]);
        }
    }
        
}