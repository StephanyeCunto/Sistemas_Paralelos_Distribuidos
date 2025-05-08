package com;
import java.io.*;
import java.util.*;
import lombok.Getter;

@Getter
public class ProcessBenchmark {
    private long time;
    private static final String[][] SEARCH_WORDS = {
        { "clarissa", "letter", "lovelace", "virtue", "dear", "miss" },
        { "eita", "bacana", "vixe", "forbidden", "indignation", "oppression" }
    };
    private int[] results;

    public ProcessBenchmark(String groupIndex, String[] words){
        long start = System.currentTimeMillis();
        runProcess(groupIndex,words);
        time = System.currentTimeMillis() - start;
    }

    private void runProcess(String groupIndex,String[] words) {
        int groupNumber = Integer.parseInt(groupIndex);
        String[] wordsToSearch = SEARCH_WORDS[groupNumber];
        List<String> command = List.of("java", "-cp", "./benchmark/target/classes", "com.CountWords");
        try {
            Process process = new ProcessBuilder(command).start();
            writeWordsToProcess(process.getOutputStream(), wordsToSearch, words);
            String output = readOutputFromProcess(process.getInputStream());
            String[] lines = output.split("\n");
            results = new int[lines.length];
            for (int i = 0; i < lines.length; i++) {
                results[i] = Integer.parseInt(lines[i]);
            }
           // printResults(wordsToSearch, output.split("\n"));
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            System.err.println("Erro ao executar tarefa: " + e.getMessage());
        }
    }

    private void writeWordsToProcess(OutputStream os, String[] wordsToSearch, String[] words) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os))) {
            writer.write(String.valueOf(wordsToSearch.length));
            writer.newLine();
            for(String search : wordsToSearch){
                writer.write(search);
                writer.newLine();
            }
            for (String word : words) {
                writer.write(word);
                writer.newLine();
            }
            writer.flush();
        }
    }

    private String readOutputFromProcess(InputStream is) throws IOException {
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        }
        return output.toString().trim();
    }

    private void printResults(String[] searchWords, String[] results) {
        for (int i = 0; i < searchWords.length && i < results.length; i++) {
            System.out.println(searchWords[i] + ": " + results[i]);
        }
    }
}