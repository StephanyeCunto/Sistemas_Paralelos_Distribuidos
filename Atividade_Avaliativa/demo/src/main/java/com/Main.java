package com;

public class Main {
    public static void main(String[] args) throws InterruptedException  {
        OpenPDF pdf = new OpenPDF("Atividade_Avaliativa/demo/src/main/resources/Clarissa Harlowe.pdf");
        String[] words = pdf.getWords();
        pdf.closePDF();

        PerformanceTest performanceTest = new PerformanceTest(words);
        performanceTest.runTests();
        performanceTest.print();
    }
}
