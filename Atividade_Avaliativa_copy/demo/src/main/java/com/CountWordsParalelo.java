package com;

import java.util.HashMap;
import java.util.Map;

public class CountWordsParalelo {
    static String[][] SEARCH_WORDS = {
        { "clarissa", "letter", "lovelace", "virtue", "dear", "miss" },
        { "eita", "bacana", "vixe", "forbidden", "indignation", "oppression" }
    };    

    static String[] words;
    static int[] searchWordsCount;

    static Map<String, Integer> wordMap = new HashMap<>();

    public static void main(String[] args) {
        OpenPDF pdf = new OpenPDF("demo/src/main/resources/Clarissa Harlowe.pdf");
        words = pdf.getWords();
        int groupWords = Integer.parseInt(args[0]);
        int numberThread = Integer.parseInt(args[1]);
        loadHasmap(groupWords);
        startThreads(numberThread);
    }

    private static void loadHasmap(int group){
        for (int i = 0; i < SEARCH_WORDS[group].length; i++) {
            wordMap.put(SEARCH_WORDS[group][i], i);
        }
        searchWordsCount = new int[SEARCH_WORDS[group].length];
    }

    private static void searchWords(int numberThread,int index){
        int wordsPerThread = words.length / numberThread;
        int threadIndex = index * wordsPerThread;
        int limit = threadIndex + wordsPerThread;

        if(index == numberThread - 1)limit = words.length;

        for(int i = threadIndex; i < limit; i++){
            Integer indice = wordMap.get(words[i]);
            if (indice != null) { 
              // synchronized (searchWordsCount) {searchWordsCount[indice]++;}
                searchWordsCount[index]++;
            }
        }
    }

    private static void startThreads(int numberThread){
        Thread[] thread = new Thread[numberThread];

        for (int i = 0; i < numberThread; i++) {
            int index = i;
            thread[i] = new Thread(()->{
                searchWords(numberThread,index);
            });
            thread[i].start();
        }
        
        for (int i = 0; i < numberThread; i++) {
            try {
                thread[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
