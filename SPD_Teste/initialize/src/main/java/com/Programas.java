package com;

import java.io.*;
import java.util.*;

import lombok.Getter;

@Getter
public abstract class Programas {
    private String[][] SEARCH_WORDS;
    private int INTERATIONS;

    protected Programas(String[][] SEARCH_WORDS, int INTERATIONS){
        this.SEARCH_WORDS = SEARCH_WORDS;
        this.INTERATIONS = INTERATIONS;
    }

    protected abstract void initialize();

    protected Process initializeProcess(List<String> command){      
        try{
            Process process = new ProcessBuilder(command).start();
            return process;
        }catch (IOException e) {
            System.out.println("Erro ao iniciar "+ e);
        }
        return null;
    }

    protected String[] getResultSearch(Process process){
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))){
            List<String> lines = reader.lines().toList();
            String[] results = lines.toArray(new String[0]);
            return results;
        }catch (IOException e) {
            System.out.println("Erro ao receber saída do programa: "+e);
        }

        return null;
    }

    protected String[] getErro(Process process){
        try(BufferedReader readerError = new BufferedReader(new InputStreamReader(process.getErrorStream()))){
            List<String> lines = readerError.lines().toList();
            String[] results = lines.toArray(new String[0]);
            return results;
        }catch ( IOException e){
            System.out.println("Erro ao tentar exibir erro: "+e);
        }
        return null;
    }

    protected void closeProcess(Process process){
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

    protected List<String> createCommand(int i, String process){
        List<String> command = new ArrayList<>();
        if(process == "paralelo"){
            command.addAll(List.of("java", "-jar", "paralelo/target/paralelo-1.0-SNAPSHOT.jar"));
            for (String word : SEARCH_WORDS[i]) {
                command.add(word);
            }
    
            return command;
        }
        command.addAll(List.of("java", "-jar", "sequencial/target/sequencial-1.0-SNAPSHOT.jar"));


        for (String word : SEARCH_WORDS[i]) {
            command.add(word);
        }

        return command;
    }

    protected int getTimeReader(String resultSearch){
        return Integer.parseInt(resultSearch);
    }                       

    protected int getTime(int startTime,String resultSearch){
        return (int)System.currentTimeMillis() - startTime - getTimeReader(resultSearch);
    }
}
