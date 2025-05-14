package com;
public class Main {
    private String[][] SEARCH_WORDS = {
        { "clarissa", "letter", "lovelace", "virtue", "dear", "miss" },        
      { "eita", "bacana", "vixe", "forbidden", "indignation", "oppression" }
    };    

    private final int[] THREAD = {2,4,8};

    private final int INTERATIONS = 30;

    private String[] words;

    private int[][] timeSequencial;
    private int[][][] timeParalelo;

    public static void main(String[] args) {
       new Main().run();
    }

    private void run(){
        long startTime = System.currentTimeMillis();
        OpenPDF pdf = new OpenPDF("./initialize/src/main/resources/Clarissa_Harlowe.pdf");
        words = pdf.getWords();
        System.out.println("Tempo de leitura: "+ (System.currentTimeMillis() - startTime) + "ms, "+(System.currentTimeMillis() - startTime)/1000 + "s");

        Sequencial sequencial = new Sequencial(SEARCH_WORDS, INTERATIONS,words);
        timeSequencial = sequencial.getTimeSequencial();

        Paralelo paralelo = new Paralelo(SEARCH_WORDS, THREAD, INTERATIONS,words);
        timeParalelo = paralelo.getTimeParalelo();

        startTime = System.currentTimeMillis();
        ProcessData processData = new ProcessData(timeSequencial, timeParalelo, INTERATIONS, THREAD);
        System.out.println("Tempo de processamento: "+ (System.currentTimeMillis() - startTime) + "ms");
        processData.print();
    }
}