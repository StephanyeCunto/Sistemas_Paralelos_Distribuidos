package com;

import java.io.*;
import java.util.*;

import lombok.Getter;

@Getter
public abstract class Programas {
    protected abstract void initialize();

    protected Process initializeProcess(List<String> command) {  
        try {
            Process process = new ProcessBuilder(command).start();
            System.out.println(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            String line;
            while ((line = reader.readLine()) != null) System.out.println("[OUT] " + line);
            while ((line = errorReader.readLine()) != null) System.err.println("[ERR] " + line);

            return process;

        } catch (IOException e) {
            System.out.println("Erro ao iniciar processo: " + e);
        }
        return null;
    }

    protected void closeProcess(Process process) {
        try {
            if (process != null) {
                int saida = process.waitFor(); 
                System.out.println("Saída: "+ saida); 

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                    String linha;
                    while ((linha = reader.readLine()) != null) {
                        System.out.println("Erro: " + linha);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Execução interrompida: " + e);
            if (process.isAlive()) {
                process.destroyForcibly();
                System.out.println("Processo forçado a fechar");
            }
        }
    }

    protected List<String> createCommand(int i, String process) {
        List<String> command = new ArrayList<>();
        String basePath = "/Users/stephanye/Documents/SPD/Tarefa_Final/";
        //String basePath = "/home/azureuser/Tarefa_Final/";


        String jarPath = switch (process) {
            case "sequencial" -> basePath + "sequencial/target/sequencial-1.0-SNAPSHOT-jar-with-dependencies.jar";
            case "simples" -> basePath + "threadsimples/target/threadsimples-1.0-SNAPSHOT-jar-with-dependencies.jar";
            case "pool" -> basePath + "threadpool/target/threadpool-1.0-SNAPSHOT-jar-with-dependencies.jar";
            case "forkjoin" -> basePath + "forkjoin/target/forkjoin-1.0-SNAPSHOT-jar-with-dependencies.jar";
            case "virtual" -> basePath + "threadvirtual/target/threadvirtual-1.0-SNAPSHOT-jar-with-dependencies.jar";
            default -> null;
        };

        if (jarPath != null) {
            command.addAll(List.of("java", "-jar", jarPath));
            command.add(String.valueOf(i));
        }
        return command;
    }            
}