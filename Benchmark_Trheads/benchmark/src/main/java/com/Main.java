package com;

public class Main {

    public static void main(String[] args) {
        OpenPDF openPDF = OpenPDF.getInstance();
        String[] words = openPDF.getWords(); 

        String groupIndex = "0";
         for(int i=0; i<30; i++){
            long start = System.currentTimeMillis();
            sequencial(groupIndex,words);
            long time = System.currentTimeMillis() - start;
            System.out.println("tempo paralelo: "+time+ "ms");
        }  
        
        for(int i=0; i<30; i++){
            long start = System.currentTimeMillis();
            paralelo(groupIndex,words,8);
            long time = System.currentTimeMillis() - start;
            System.out.println("tempo thread: "+time+ "ms");
        }
    }

    private static void sequencial(String groupIndex, String[] words){
        ProcessBenchmark sequencial = new ProcessBenchmark(groupIndex,words);
       // System.out.println("Sequencial :"+sequencial.getTime()+" ms");
    }

    private static void paralelo(String groupIndex, String[] words, int numberThread){
        int wordPerThread = words.length/numberThread;
        String[] wordsThreads = new String[wordPerThread];

        for(int i=0; i< numberThread; i++){
            int index= i*wordPerThread;
            for(int j=index; j <index + wordPerThread; j++){
                    wordsThreads[j-index] = words[j];
                
            }
        }

        Thread[] thread = new Thread[numberThread];

        for (int i = 0; i < numberThread; i++) {
            thread[i] = new Thread(()->{
                ProcessBenchmark paralelo = new ProcessBenchmark(groupIndex,wordsThreads);
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