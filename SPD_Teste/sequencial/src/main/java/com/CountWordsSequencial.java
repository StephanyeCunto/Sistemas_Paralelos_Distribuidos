package com;

import java.util.HashMap;
import java.util.Map;

public class CountWordsSequencial {
    static String[] words;
    static int[] searchWordsCount;

    static Map<String, Integer> wordMap = new HashMap<>();
    
    public static void main(String[] args) {
        initialize(args);
        searchWords();
        loadResults();
    }

    private static void initialize(String[] args){
        searchWordsCount = new int[args.length];
        
        for(int i=0; i< args.length; i++){
            wordMap.put(args[i], i);
        }

        OpenPDF pdf = new OpenPDF("./sequencial/src/main/resources/Clarissa_Harlowe.pdf");
        words = pdf.getWords();
    }

    private static void searchWords() {
        for (String word : words) {
            Integer index = wordMap.get(word);
            if (index != null) searchWordsCount[index]++;
        }
    }

    private static void loadResults(){
        for(String key : wordMap.keySet()){
            System.out.println("Key: "+ key+ " : "+ searchWordsCount[wordMap.get(key)]);
        }
    }
        
}
