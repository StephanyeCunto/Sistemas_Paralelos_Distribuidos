package com;

import java.util.*;
import java.io.*;
import lombok.Getter;

@Getter
public class Sequencial {
    private int[][] timeSequencialSearchWords;
    public Sequencial(int interations){
        timeSequencialSearchWords = new int[2][interations];
        for(int j=0; j<2; j++){
            for(int i=0;i<interations;i++){
                long startTime = System.nanoTime();
                loadProcess(j);
                long endTime = System.nanoTime() - startTime;
                int endTimeInt =(int) endTime/100000;
                timeSequencialSearchWords[j][i] = (int) endTimeInt;
            }
        }
    }

    private void loadProcess(int groupWordsSearch){
        String groupWords = String.valueOf(groupWordsSearch);
        List<String> command = List.of("java", "-cp", "./atividade_avaliativa_copy/demo/target/classes", "com.CountWordsSequencial",groupWords);
        try {
            Process process = new ProcessBuilder(command).start();

            process.waitFor();
        } catch (IOException | InterruptedException e) {
            System.err.println("Erro ao executar tarefa: " + e.getMessage());
        }
    }
}

