package com;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Main {
    private static final int INTERATIONS = 30;
    public static void main(String[] args) {
        sequencial();
       

    }

    private static void sequencial(){
        for(int i=0; i< INTERATIONS; i++){
            long startTime = System.currentTimeMillis();
    
            List<String> command = List.of("java", "-jar", "sequencial/target/sequencial-1.0-SNAPSHOT.jar");
            try{
                Process process =  new ProcessBuilder(command).start();
               try(BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))){
    
               }
    
            /*    String line;
                while ((line = reader.readLine())!=null){
                    System.out.println(line);
                }
            */
                process.waitFor(); 
    
                if (process.isAlive()) {
                    process.destroyForcibly();
                }
    
    
            /*     BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                String errLine;
                while ((errLine = errorReader.readLine()) != null) {
                    System.err.println("ERR: " + errLine);
                }
    
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine())!=null){
                    System.out.println(line);
                }
    */
            }catch(InterruptedException | IOException e){
                System.out.println("Erro ao iniciar "+e);
            }
    
            long endTime = System.currentTimeMillis() - startTime;
    
            System.out.println(endTime);
        }
    }
}