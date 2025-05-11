package com;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class CountWordsParalelo {
    static String[] words;
    static AtomicInteger[] searchWordsCount;
    static int threads;

    static int timeRead;

    static Map<String, Integer> wordMap = new HashMap<>();
    
    public static void main(String[] args) {
        initialize(args);
        intiializeThread();
        loadResults();
    }

    private static void initialize(String[] args){
        threads = Integer.parseInt(args[args.length - 1]);

        searchWordsCount = new AtomicInteger[args.length - 1];
        for (int i = 0; i < searchWordsCount.length; i++) {
            searchWordsCount[i] = new AtomicInteger();
        }

        for(int i=0; i< args.length-1; i++){
            wordMap.put(args[i], i);
        }

        int timeStart = (int) System.currentTimeMillis();
        OpenPDF pdf = new OpenPDF("./sequencial/src/main/resources/Clarissa_Harlowe.pdf");
        words = pdf.getWords();
        timeRead = (int) System.currentTimeMillis() - timeStart;

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
            Integer index = wordMap.get(words[j]);
            if (index != null) searchWordsCount[index].incrementAndGet();
        }
    }

    private static void loadResults(){
        System.out.println(timeRead);

        for(String key : wordMap.keySet()){
            System.out.println("Key: "+ key+ " : "+ searchWordsCount[wordMap.get(key)].get());
        }
    }
}
