package com;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static String[][] SEARCH_WORDS = {
        { "clarissa", "letter", "lovelace", "virtue", "dear", "miss" },
        { "eita", "bacana", "vixe", "forbidden", "indignation", "oppression" }
    };    

    private static int[] THREAD = {2,4,8};

    private static final int INTERATIONS = 1;

    private static long[] timeSequencial = new long[INTERATIONS];

    public static void main(String[] args) {
        for(int i=0; i<SEARCH_WORDS.length; i++){
    //       sequencial(i);
        }

        Paralelo paralelo = new Paralelo(SEARCH_WORDS, THREAD, INTERATIONS);
        int[][][] timeParalelo = paralelo.getTimeParalelo();
        
        for(int i = 0; i < SEARCH_WORDS.length; i++){
            for(int j = 0; j < THREAD.length; j++){
                for(int k = 0; k < INTERATIONS; k++){
                    System.out.println(timeParalelo[i][j][k]);
                }
            }
        }
    }

    private static void sequencial(int groupWords){
        for(int i=0; i< INTERATIONS; i++){
            long startTime = System.currentTimeMillis();

            List<String> command = new ArrayList<>();
    
            command.addAll(List.of("java", "-jar", "sequencial/target/sequencial-1.0-SNAPSHOT.jar"));

            for (String word : SEARCH_WORDS[groupWords]) {
                command.add(word);
            }

            try{
                Process process =  new ProcessBuilder(command).start();
                try(BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))){
                    getResults(reader);
                }
            
                process.waitFor(); 
    
                if (process.isAlive()) {
                    process.destroyForcibly();
                }
    
            }catch(InterruptedException | IOException e){
                System.out.println("Erro ao iniciar "+e);
            }
    
            long endTime = System.currentTimeMillis() - startTime;
            timeSequencial[i] = endTime;
    
            System.out.println(endTime + " ms");
        }
    }


    private static void getResults(BufferedReader reader){
        String line;
        try{
            while ((line = reader.readLine())!=null){
                System.out.println(line);
            }
        }catch(IOException e){
            System.out.println("Erro encontrado: "+e);
        }
    }
}