package com;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public class Paralelo {
    private String[][] SEARCH_WORDS;
    private int[] THREAD;
    private int INTERATIONS;

    private int[][][] timeParalelo;

    public Paralelo(String[][] SEARCH_WORDS, int[] THREAD,int INTERATIONS){
        this.SEARCH_WORDS = SEARCH_WORDS;
        this.THREAD = THREAD;
        this.INTERATIONS = INTERATIONS;
        timeParalelo = new int[this.SEARCH_WORDS.length][this.THREAD.length][this.INTERATIONS];
        
        initialize();
    }

    private void initialize(){
        for(int i = 0; i < SEARCH_WORDS.length; i++){
            for(int j = 0; j < THREAD.length; j++){
                for(int k = 0; k < INTERATIONS; k++){
                    int startTime=(int) System.currentTimeMillis();

                    List<String> command = createCommand(i,j);

                    Process process = initializeProcess(command);
                    if(process!= null){ 
                        String[] resultSearch = getResultSearch(process);
                        int timeReader =Integer.parseInt(resultSearch[0]);
                        closeProcess(process);
                        int endTime=(int) System.currentTimeMillis();
                        timeParalelo[i][j][k] =(int)endTime - startTime - timeReader;
                    /*  for(String result : resultSearch){
                            System.out.println(result);
                        }
                    */        
                    }
                } 

            }
        }
    }

    private List<String> createCommand(int i, int j){
        List<String> command = new ArrayList<>();
        command.addAll(List.of("java", "-jar", "paralelo/target/paralelo-1.0-SNAPSHOT.jar", String.valueOf(THREAD[j])));


        for (String word : SEARCH_WORDS[i]) {
            command.add(word);
        }

        return command;
    }

    private Process initializeProcess(List<String> command){
       try{
            Process process = new ProcessBuilder(command).start();
            return process;
        }catch (IOException e) {
            System.out.println("Erro ao iniciar "+ e);
        }
        return null;
    }

    private String[] getResultSearch(Process process){
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))){
            List<String> lines = reader.lines().toList();
            String[] results = lines.toArray(new String[0]);
            return results;
        }catch (IOException e) {
            System.out.println("Erro ao receber saída do programa: "+e);
        }

        return null;
    }

    private void closeProcess(Process process){
        try{
            process.waitFor(); 
        }catch(InterruptedException e){
            System.out.println("Não foi possível fechar o processo: "+e);
            if (process.isAlive()) {
                process.destroyForcibly();
                System.out.println("Processo forçado a fechar");
            }
        }

    }
}
