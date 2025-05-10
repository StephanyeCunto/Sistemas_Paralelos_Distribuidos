package com;

import java.util.HashMap;
import java.util.Map;

public class CountWordsSequencial {
    static String[][] SEARCH_WORDS = {
        { "clarissa", "letter", "lovelace", "virtue", "dear", "miss" },
        { "eita", "bacana", "vixe", "forbidden", "indignation", "oppression" }
    };    

    static String[] words;
    static int[] searchWordsCount;

    static Map<String, Integer> wordMap = new HashMap<>();
    
    public static void main(String[] args) {
        OpenPDF pdf = new OpenPDF("./sequencial/src/main/resources/Clarissa_Harlowe.pdf");
        words = pdf.getWords();
        int groupWords =Integer.parseInt("0");

        loadHasmap(groupWords);
        searchWords();

        for(String key : wordMap.keySet()){
            System.out.println("Key: "+ key+ " : "+ searchWordsCount[wordMap.get(key)]);
        }
    }

    private static void loadHasmap(int group){
        for (int i = 0; i < SEARCH_WORDS[group].length; i++) {
            wordMap.put(SEARCH_WORDS[group][i], i);
        }
        searchWordsCount = new int[SEARCH_WORDS[group].length];
    }

    private static void searchWords() {
        for (String word : words) {
            Integer index = wordMap.get(word);
            if (index != null) searchWordsCount[index]++;
        }
    }
}
