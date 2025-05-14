package com;

import java.io.*;
import java.util.*;

import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor
public abstract class Programas {
    private final String[][] SEARCH_WORDS;
    private final int INTERATIONS;
    private final String[] WORDS;

    protected abstract void initialize();

    protected Process initializeProcess(List<String> command){      
        try{
            Process process = new ProcessBuilder(command).start();
            writeWordsToProcess(process.getOutputStream());
            return process;
        }catch (IOException e) {
            System.out.println("Erro ao iniciar "+ e);
        }
        return null;
    }

    protected String[] getResultSearch(Process process){
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))){
            return (reader.lines().toList()).toArray(new String[0]);

        }catch (IOException e) {
            System.out.println("Erro ao receber saída do programa: "+e);
        }
        return null;
    }

    protected String[] getErro(Process process){
        try(BufferedReader readerError = new BufferedReader(new InputStreamReader(process.getErrorStream()))){
            return (readerError.lines().toList()).toArray(new String[0]);

        }catch ( IOException e){
            System.out.println("Erro ao tentar exibir erro: "+e);
        }
        return null;
    }

    protected void closeProcess(Process process){
        try{
            if (!process.waitFor(5, java.util.concurrent.TimeUnit.SECONDS)) process.destroyForcibly();
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
        String jarPath = process.equals("paralelo") ? "paralelo/target/paralelo-1.0-SNAPSHOT.jar"
        : "sequencial/target/sequencial-1.0-SNAPSHOT.jar";
    
        command.addAll(List.of("java", "-jar", jarPath));
    
        for (String word : SEARCH_WORDS[i]) command.add(word);
    
        return command;
    }
    

    private void writeWordsToProcess(OutputStream os){        
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os))) {
            for (String word : WORDS) {
                writer.write(word);
                writer.newLine();
            }
            writer.flush();
        }catch(IOException e){
            System.out.println("Impossível enviar os dados: "+e);
        }
    }                     
}