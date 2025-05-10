package com;

import java.util.HashMap;
import java.util.Map;

public class CountWordsParalelo {
    static String[] words;
    static int[] searchWordsCount;
    static int threads;

    static Map<String, Integer> wordMap = new HashMap<>();
    
    public static void main(String[] args) {
        initialize(args);
        intiializeThread();
        loadResults();
    }

    private static void initialize(String[] args){
        threads = Integer.parseInt(args[0]);

        searchWordsCount = new int[args.length - 1];

        for(int i=1; i< args.length; i++){
            wordMap.put(args[i], i-1);
        }

        OpenPDF pdf = new OpenPDF("./paralelo/src/main/resources/Clarissa_Harlowe.pdf");
        words = pdf.getWords();
    }

    private static void intiializeThread() {
        Thread[] thread = new Thread[threads];

        int wordsPerThread = words.length/threads;

        for(int i=0; i< threads; i++){
            int index = i;
            thread[i] = new Thread(()->searchWord(index, wordsPerThread));
            thread[i].start();
        }

        for(int i=0; i< threads;i++){
            try{
            thread[i].join();
            }catch(InterruptedException e){
                System.out.println("Erro ao finalizar a thread "+i+", "+ e);
            }
        }
    }

    private static void searchWord(int i, int wordsPerThread){
        int wordInitial = i*wordsPerThread;
        int limite = wordInitial + wordsPerThread;

        if(i == threads-1) limite = words.length;

        for(int j=wordInitial; j< limite; j++){
            for (String word : words) {
                Integer index = wordMap.get(word);
                if (index != null) searchWordsCount[index]++;
            }
        }
    }


    private static void loadResults(){
        for(String key : wordMap.keySet()){
            System.out.println("Key: "+ key+ " : "+ searchWordsCount[wordMap.get(key)]);
        }
    }
}
