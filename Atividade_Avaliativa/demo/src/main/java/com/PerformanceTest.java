package com;

import java.util.List;
import java.util.Map;

public class PerformanceTest {
    private final String[] words;
    private final int[] threads = {2, 4, 8};
    private final int iterations = 100;
    
    private final String[][] searchWords = {
            { "clarissa", "letter", "lovelace", "virtue", "dear", "miss" },
            { "eita", "bacana", "vixe", "forbidden", "indignation", "oppression" }
    };

    private DataProcessor dataProcessor ;

    public PerformanceTest(String[] words) {
        this.words = words;
    }

    public void runTests() {
        TestExecutor testExecutor = new TestExecutor(this.words, this.searchWords, this.iterations, this.threads);
        int[][] timeSequentialSearchWords = testExecutor.runTestsSequencial();
        int[][][] timeParallelSearchWords = testExecutor.runTestsParalelo();
        int[][][] timeParallelVirtualSearchWords = testExecutor.runTestsParaleloVirtual();

        Map<String, List<Integer>> wordMap = testExecutor.getWordsCount();

        for (Map.Entry<String, List<Integer>> entry : wordMap.entrySet()) {
            System.out.println("Palavras: " +  entry.getKey()+ " "+entry.getValue().get(1));
        }

       dataProcessor = new DataProcessor(timeSequentialSearchWords, timeParallelSearchWords, this.iterations, this.searchWords.length, this.threads, timeParallelVirtualSearchWords);
    new ChartGenerator(this.searchWords,this.threads,dataProcessor.getTimeSequentialAverage(), dataProcessor.getTimeParallelAverage(),   
            dataProcessor.getTimeSequentialStdDev(),dataProcessor.getTimeParallelStdDev(),dataProcessor.getTimeParallelVirtualAverage(),dataProcessor.getTimeParallelVirtualStdDev(), dataProcessor.getSpeedup(),dataProcessor.getEfficiency(),
            dataProcessor.getSpeedupVirtual(),dataProcessor.getEfficiencyVirtual(),wordMap
        ); 
    }

    public void print() {
        System.out.println("============================================== RESULTADOS DE PERFORMANCE ===============================================");
        
        for (int i = 0; i < this.searchWords.length; i++) {
            String[] currentWordSet = this.searchWords[i];
            System.out.println("🔍 CONJUNTO DE PALAVRAS " + (i+1) + ": " + 
                              String.join(", ", currentWordSet));
            
            System.out.printf("│ SEQUENCIAL    │ Tempo médio: %8.2f μs │ Desvio padrão: %8.2f μs │\n", 
                            dataProcessor.getTimeSequentialAverage()[i], 
                            dataProcessor.getTimeSequentialStdDev()[i]);
            
           
                            System.out.println("├───────────────┼─────────────────────────┼──────────────────────────┼───────────────────┼──────────────────────────────┤");
                            System.out.println("│   PARALELO    │       TEMPO MÉDIO       │      DESVIO PADRÃO       │      SPEEDUP      │          EFICIÊNCIA          │");
                            System.out.println("├───────────────┼─────────────────────────┼──────────────────────────┼───────────────────┼──────────────────────────────┤");

            for (int j = 0; j < this.threads.length; j++) {
                System.out.printf("│ %2d Threads    │ %8.2f μs             │ %8.2f μs              │ %6.2fx           │  %6.2f%%                     │\n", 
                              this.threads[j],
                              dataProcessor.getTimeParallelAverage()[i][j],
                              dataProcessor.getTimeParallelStdDev()[i][j],
                              dataProcessor.getSpeedup()[i][j],
                              dataProcessor.getEfficiency()[i][j] * 100);
            }
            
            System.out.println("└───────────────┴─────────────────────────┴──────────────────────────┴───────────────────┴──────────────────────────────┘");
            System.out.println();

            System.out.println("================================================= THREAD VIRTUAL =======================================================");


            System.out.println("├───────────────┼─────────────────────────┼──────────────────────────┼───────────────────┼──────────────────────────────┤");
            System.out.println("│   PARALELO    │       TEMPO MÉDIO       │      DESVIO PADRÃO       │      SPEEDUP      │          EFICIÊNCIA          │");
            System.out.println("├───────────────┼─────────────────────────┼──────────────────────────┼───────────────────┼──────────────────────────────┤");
            
            for (int j = 0; j < this.threads.length; j++) {
                System.out.printf("│ %2d Threads    │ %8.2f μs             │ %8.2f μs              │ %6.2fx           │  %6.2f%%                     │\n", 
                              this.threads[j],
                              dataProcessor.getTimeParallelVirtualAverage()[i][j],
                              dataProcessor.getTimeParallelVirtualStdDev()[i][j],
                              dataProcessor.getSpeedupVirtual()[i][j],
                              dataProcessor.getEfficiencyVirtual()[i][j] * 100);
            }
            
            System.out.println("└───────────────┴─────────────────────────┴──────────────────────────┴───────────────────┴──────────────────────────────┘");
            System.out.println();


        }
        
        System.out.println("=======================================================================================================================");
    }
}
