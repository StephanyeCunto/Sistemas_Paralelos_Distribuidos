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

    private static int[] THREAD = {1};

    private static final int INTERATIONS = 1;

    public static void main(String[] args) {
        for(int i=0; i<SEARCH_WORDS.length; i++){
    //        sequencial(i);
        }

        for(int i=0; i<SEARCH_WORDS.length; i++){
            paralelo(i);
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
    
            System.out.println(endTime + " ms");
        }
    }

    private static void paralelo(int groupWords){
        for(int i=0; i< INTERATIONS; i++){
            for(int thread: THREAD){
                long startTime = System.currentTimeMillis();

                List<String> command = new ArrayList<>();
        
                command.addAll(List.of("java", "-jar", "paralelo/target/paralelo-1.0-SNAPSHOT.jar", String.valueOf(thread)));

                for (String word : SEARCH_WORDS[groupWords]) {
                    command.add(word);
                }

                try{
                    Process process =  new ProcessBuilder(command).start();
                    try(BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))){
                        getResults(reader);
                    }

                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

                    String line;

                    while((line=reader.readLine())!=null){
                        System.out.println(line);
                    }
                
                    process.waitFor(); 
        
                    if (process.isAlive()) {
                        process.destroyForcibly();
                    }
        
                }catch(InterruptedException | IOException e){
                    System.out.println("Erro ao iniciar "+e);
                }
        
                long endTime = System.currentTimeMillis() - startTime;
        
                System.out.println(endTime + " ms");
            }
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