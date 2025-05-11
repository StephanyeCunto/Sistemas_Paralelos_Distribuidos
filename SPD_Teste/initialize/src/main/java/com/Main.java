package com;

public class Main {
    private static String[][] SEARCH_WORDS = {
        { "clarissa", "letter", "lovelace", "virtue", "dear", "miss" },
        { "eita", "bacana", "vixe", "forbidden", "indignation", "oppression" }
    };    

    private static int[] THREAD = {2,4,8};

    private static final int INTERATIONS = 1;

    private int[][] timeSequencial;
    private int[][][] timeParalelo;


    public static void main(String[] args) {

        Sequencial sequencial = new Sequencial(SEARCH_WORDS, INTERATIONS);
        int[][] timeSequencial = sequencial.getTimeSequencial();

        Paralelo paralelo = new Paralelo(SEARCH_WORDS, THREAD, INTERATIONS);
        int[][][] timeParalelo = paralelo.getTimeParalelo();

        for(int i = 0; i < SEARCH_WORDS.length; i++){
            for(int k = 0; k < INTERATIONS; k++){
                System.out.println(timeSequencial[i][k]);
            }
        }
        
        for(int i = 0; i < SEARCH_WORDS.length; i++){
            for(int j = 0; j < THREAD.length; j++){
                for(int k = 0; k < INTERATIONS; k++){
                    System.out.println(timeParalelo[i][j][k]);
                }
            }
        }
    }


}