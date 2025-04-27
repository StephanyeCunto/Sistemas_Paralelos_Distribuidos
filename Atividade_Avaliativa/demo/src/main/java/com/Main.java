package com;

public class Main {
    public static void main(String[] args) {
        OpenPDF pdf = new OpenPDF("Atividade_Avaliativa/demo/src/main/resources/Clarissa Harlowe.pdf");
        String[] words = pdf.getWords();
        pdf.closePDF();

        Tester tester = new Tester(words);
    }
}