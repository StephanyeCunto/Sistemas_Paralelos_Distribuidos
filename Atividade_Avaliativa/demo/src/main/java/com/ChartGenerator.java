package com;

import java.io.*;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;

import org.jfree.chart.*;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.*;

import java.awt.*;

public class ChartGenerator {
    private String[][] searchWords;
    private int[] threads;
    private double[] timeSequentialAverage;
    private double[][] timeParallelAverage;
    private double[] timeSequentialStdDev;
    private double[][] timeParallelStdDev;
    private double[][] speedup;
    private double[][] efficiency;
    private Map<String, List<Integer>> wordMap;
;
    public ChartGenerator(String[][] searchWords, int[] threads,double[] timeSequentialAverage, double[][] timeParallelAverage,double[] timeSequentialStdDev, double[][] timeParallelStdDev,double[][] speedup, double[][] efficiency,Map<String, List<Integer>> wordMap) {
        this.searchWords = searchWords;
        this.threads = threads;
        this.timeSequentialAverage = timeSequentialAverage;
        this.timeParallelAverage = timeParallelAverage;
        this.timeSequentialStdDev = timeSequentialStdDev;
        this.timeParallelStdDev = timeParallelStdDev;
        this.speedup = speedup;
        this.efficiency = efficiency;
        this.wordMap = wordMap;

        generateGraphs();

        exportResultsToCSV();
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
            writer.append("Tipo;Conjunto;Threads;Tempo Médio (μs);Desvio Padrão;Speedup;Eficiência\n");
    
            for (int i = 0; i < searchWords.length; i++) {
                writer.append(String.format("Sequencial;%d;1;%.2f;%.2f;;\n",
                    i + 1,
                    timeSequentialAverage[i],
                    timeSequentialStdDev[i]
                ));
            }
                for (int i = 0; i < searchWords.length; i++) {
                for (int j = 0; j < threads.length; j++) {
                    writer.append(String.format("Paralelo;%d;%d;%.2f;%.2f;%.2f;%.2f\n",
                        i + 1,
                        threads[j],
                        timeParallelAverage[i][j],
                        timeParallelStdDev[i][j],
                        speedup[i][j],
                        efficiency[i][j] * 100
                    ));
                }
            }
    
        } catch (IOException e) {
            System.err.println("Erro ao exportar resultados: " + e.getMessage());
        }
    }
    
}