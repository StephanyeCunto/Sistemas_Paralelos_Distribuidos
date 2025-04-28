package com;

import org.apache.commons.math3.stat.descriptive.rank.Percentile;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.jfree.chart.*;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.*;

import java.awt.*;
import java.io.*;
import java.io.IOException;

import javax.swing.*;

import java.util.*;
import java.util.List;

public class Tester {
    private String[] words;
    
    private String[][] searchWords = {
            { "clarissa", "letter", "lovelace", "virtue", "dear", "miss" },
            { "eita", "bacana", "vixe", "forbidden", "indignation", "oppression" }
    };
    private int[] threads = { 2, 4, 8 };
    private int iterations = 30;
    
    private int[][] timeSequentialSearchWords = new int[searchWords.length][iterations];
    private int[][][] timeParallelSearchWords = new int[searchWords.length][threads.length][iterations];
    
    private double[] timeSequentialAverage = new double[searchWords.length];
    private double[][] timeParallelAverage = new double[searchWords.length][threads.length];
    
    private double[] timeSequentialStdDev = new double[searchWords.length];
    private double[][] timeParallelStdDev = new double[searchWords.length][threads.length];
    
    private double[][] speedup = new double[searchWords.length][threads.length];
    private double[][] efficiency = new double[searchWords.length][threads.length];
    
    private Map<String, List<Integer>> wordMap = new HashMap<>();


    public Tester(String[] words) {
        this.words = words;
        runTests();
        removeWarmUp();
        removeOutliers();
        calculateStatistics();
        printResults();
        generateGraphs();
        exportResultsToCSV();
    }

    private void runTests() {        
        for (int i = 0; i < searchWords.length; i++) {
            for (int j = 0; j < iterations; j++) {
                Sequencial sequencial = new Sequencial(words, searchWords[i]);
                timeSequentialSearchWords[i][j] = (int) sequencial.getTime();

                if (j == iterations - 1) {
                    sequencial.getWordMap().forEach((key, value) -> {
                        List<Integer> list = new ArrayList<>();
                        list.add(value);
                        list.add(sequencial.getSearchWordsCount()[value]);
                        wordMap.put(key, list);
                    });
                }
            }
        }
        
        for (int i = 0; i < searchWords.length; i++) {
            for (int j = 0; j < threads.length; j++) {
                for (int k = 0; k < iterations; k++) {
                    Paralelo paralelo = new Paralelo(threads[j], words, searchWords[i]);
                    timeParallelSearchWords[i][j][k] = (int) paralelo.getTime();
                }
            }
        }
    }

    private void removeWarmUp() {
        int warmupCount = iterations / 10; 
        int newSize = iterations - warmupCount;
        
        for (int i = 0; i < searchWords.length; i++) {
            int[] newSequential = new int[newSize];
            System.arraycopy(timeSequentialSearchWords[i], warmupCount, newSequential, 0, newSize);
            timeSequentialSearchWords[i] = newSequential;
            
            for (int j = 0; j < threads.length; j++) {
                int[] newParallel = new int[newSize];
                System.arraycopy(timeParallelSearchWords[i][j], warmupCount, newParallel, 0, newSize);
                timeParallelSearchWords[i][j] = newParallel;
            }
        }
        
        iterations = newSize;
    }

    private void removeOutliers() {
        for (int i = 0; i < searchWords.length; i++) {
            timeSequentialSearchWords[i] = removeOutliersFromArray(timeSequentialSearchWords[i]);
            
            for (int j = 0; j < threads.length; j++) {
                timeParallelSearchWords[i][j] = removeOutliersFromArray(timeParallelSearchWords[i][j]);
            }
        }
    }
    private int[] removeOutliersFromArray(int[] times) {
        double[] doubleArray = Arrays.stream(times).asDoubleStream().toArray();
        Percentile percentile = new Percentile();
        percentile.setData(doubleArray);
        
        double q1 = percentile.evaluate(25);
        double q3 = percentile.evaluate(75);
        double iqr = q3 - q1;
        double lowerBound = q1 - (1.5 * iqr);
        double upperBound = q3 + (1.5 * iqr);
        
        return Arrays.stream(times)
                     .filter(time -> time >= lowerBound && time <= upperBound)
                     .toArray();
    }

    private void calculateStatistics() {
        StandardDeviation stdDev = new StandardDeviation();
        
        for (int i = 0; i < searchWords.length; i++) {
            double[] sequentialDouble = Arrays.stream(timeSequentialSearchWords[i])
                                             .asDoubleStream().toArray();

            timeSequentialAverage[i] = calculateAverage(sequentialDouble);
            timeSequentialStdDev[i] = stdDev.evaluate(sequentialDouble);
            
            for (int j = 0; j < threads.length; j++) {
                double[] parallelDouble = Arrays.stream(timeParallelSearchWords[i][j])
                                               .asDoubleStream().toArray();
                timeParallelAverage[i][j] = calculateAverage(parallelDouble);
                timeParallelStdDev[i][j] = stdDev.evaluate(parallelDouble);
                
                speedup[i][j] = timeSequentialAverage[i] / timeParallelAverage[i][j];
                efficiency[i][j] = speedup[i][j] / threads[j];
            }
        }
    }

    private double calculateAverage(double[] values) {
        double sum = 0;
        for (double value : values) {
            sum += value;
        }
        return sum / values.length;
    }

    private void printResults() {
        System.out.println("\n=== RESULTADOS DOS TESTES ===\n");
        
        System.out.println("### Busca Sequencial ###");
        for (int i = 0; i < searchWords.length; i++) {
            System.out.println("\nConjunto de palavras: " + Arrays.toString(searchWords[i]));
            System.out.printf("Média: %.2f μs | Desvio Padrão: %.2f μs\n", 
                            timeSequentialAverage[i], timeSequentialStdDev[i]);
        }
        
        System.out.println("\n### Busca Paralela ###");
        for (int i = 0; i < searchWords.length; i++) {
            System.out.println("\nConjunto de palavras: " + Arrays.toString(searchWords[i]));
            
            for (int j = 0; j < threads.length; j++) {
                System.out.printf("Threads: %d | Média: %.2f μs | Desvio Padrão: %.2f μs\n", 
                            threads[j], timeParallelAverage[i][j], timeParallelStdDev[i][j]);
                System.out.printf("Speedup: %.2fx | Eficiência: %.2f%%\n", 
                            speedup[i][j], efficiency[i][j] * 100);
            }
        }
        
        System.out.println("\n### Contagem de Palavras ###");
        for (Map.Entry<String, List<Integer>> entry : wordMap.entrySet()) {
            System.out.printf("Palavra: %-15s | Ocorrências: %d\n", 
                            entry.getKey(), entry.getValue().get(1));
        }
    }

    private void generateGraphs() {
        JFrame frame = new JFrame("Análise de Desempenho");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.setLayout(new GridLayout(2, 2));
        
        frame.add(createTimeComparisonChart());
        frame.add(createWordCountChart());
        frame.add(createSpeedupChart());
        frame.add(createEfficiencyChart());
        frame.setVisible(true);
    }

    private ChartPanel createTimeComparisonChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        for (int i = 0; i < searchWords.length; i++) {
            String wordSetLabel = "Conjunto " + (i+1);
            dataset.addValue(timeSequentialAverage[i], "Sequencial", wordSetLabel);
            
            for (int j = 0; j < threads.length; j++) {
                dataset.addValue(timeParallelAverage[i][j], 
                               threads[j] + " Threads", wordSetLabel);
            }
        }
        
        JFreeChart chart = ChartFactory.createBarChart(
            "Tempo de Execução por Tipo de Busca",
            "Conjunto de Palavras",
            "Tempo (μs)",
            dataset,
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        );
        
        customizeChart(chart);
        return new ChartPanel(chart);
    }

    private ChartPanel createWordCountChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        for (Map.Entry<String, List<Integer>> entry : wordMap.entrySet()) {
            dataset.addValue(entry.getValue().get(1), "Ocorrências", entry.getKey());
        }
        
        JFreeChart chart = ChartFactory.createBarChart(
            "Contagem de Palavras",
            "Palavras",
            "Ocorrências",
            dataset,
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        );
        
        customizeChart(chart);
        return new ChartPanel(chart);
    }

    private ChartPanel createSpeedupChart() {
        XYSeriesCollection dataset = new XYSeriesCollection();
        
        for (int i = 0; i < searchWords.length; i++) {
            XYSeries series = new XYSeries("Conjunto " + (i+1));
            
            for (int j = 0; j < threads.length; j++) {
                series.add(threads[j], speedup[i][j]);
            }
            
            dataset.addSeries(series);
        }
        
        JFreeChart chart = ChartFactory.createXYLineChart(
            "Speedup vs. Número de Threads",
            "Número de Threads",
            "Speedup",
            dataset,
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        );
        
        customizeChart(chart);
        return new ChartPanel(chart);
    }

    private ChartPanel createEfficiencyChart() {
        XYSeriesCollection dataset = new XYSeriesCollection();
        
        for (int i = 0; i < searchWords.length; i++) {
            XYSeries series = new XYSeries("Conjunto " + (i+1));
            
            for (int j = 0; j < threads.length; j++) {
                series.add(threads[j], efficiency[i][j] * 100);
            }
            
            dataset.addSeries(series);
        }
        
        JFreeChart chart = ChartFactory.createXYLineChart(
            "Eficiência vs. Número de Threads",
            "Número de Threads",
            "Eficiência (%)",
            dataset,
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        );
        
        customizeChart(chart);
        return new ChartPanel(chart);
    }

    private void customizeChart(JFreeChart chart) {
        chart.setBackgroundPaint(Color.white);
        chart.getTitle().setFont(new Font("SansSerif", Font.BOLD, 16));
        chart.getLegend().setItemFont(new Font("SansSerif", Font.PLAIN, 12));
    }

    private void exportResultsToCSV() {
        try (FileWriter writer = new FileWriter("resultados_teste.csv")) {
            writer.append("Tipo,Conjunto,Threads,Tempo Médio (μs),Desvio Padrão,Speedup,Eficiência\n");
            
            for (int i = 0; i < searchWords.length; i++) {
                writer.append(String.format("Sequencial,%d,0,%.2f,%.2f,1.00,100.00\n",
                    i+1, timeSequentialAverage[i], timeSequentialStdDev[i]));
            }
            
            for (int i = 0; i < searchWords.length; i++) {
                for (int j = 0; j < threads.length; j++) {
                    writer.append(String.format("Paralelo,%d,%d,%.2f,%.2f,%.2f,%.2f\n",
                        i+1, threads[j], timeParallelAverage[i][j], timeParallelStdDev[i][j],
                        speedup[i][j], efficiency[i][j] * 100));
                }
            }
            
            System.out.println("\nResultados exportados para 'resultados_teste.csv'");
        } catch (IOException e) {
            System.err.println("Erro ao exportar resultados: " + e.getMessage());
        }
    }
}