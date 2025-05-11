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
        OpenPDF pdf = new OpenPDF("./sequencial/src/main/resources/Clarissa_Harlowe.pdf");
        words = pdf.getWords();

        Sequencial sequencial = new Sequencial(SEARCH_WORDS, INTERATIONS,words);
        timeSequencial = sequencial.getTimeSequencial();

        Paralelo paralelo = new Paralelo(SEARCH_WORDS, THREAD, INTERATIONS,words);
        timeParalelo = paralelo.getTimeParalelo();

        ProcessData processData = new ProcessData(timeSequencial, timeParalelo, INTERATIONS, THREAD);
        processData.print();
    }
}