package com;

import java.io.IOException;
import java.util.List;

import lombok.Getter;

@Getter
public class Paralelo {
    private int[][][] timeParaleloSearchWords;

    public Paralelo(int interations, int[] thread){
        timeParaleloSearchWords=new int[2][thread.length][interations];

        for(int j=0; j<2; j++){
            for(int k=0;k<thread.length; k++){
                for(int i=0;i<interations;i++){
                    long startTime = System.nanoTime();
                    loadProcess(j,thread[k]);
                    long endTime = System.nanoTime() - startTime;
                    int endTimeInt =(int) endTime/100000;
                    timeParaleloSearchWords[j][k][i] = endTimeInt;
                }
            }
        }
    }

    private void loadProcess(int groupWordsSearch, int thread){
        String groupWords = String.valueOf(groupWordsSearch);
        String numberThread = String.valueOf(thread);

        List<String> command = List.of("java", "-cp", "./atividade_avaliativa_copy/demo/target/classes", "com.CountWordsParalelo",groupWords,numberThread);
        try {
            Process process = new ProcessBuilder(command).start();

            process.waitFor();
        } catch (IOException | InterruptedException e) {
            System.err.println("Erro ao executar tarefa: " + e.getMessage());
        }
    }
}
