package com;

public class Main {
    public static void main(String[] args) {
        OpenPDF pdf = new OpenPDF("Atividade_Avaliativa/demo/src/main/resources/Clarissa Harlowe.pdf");
        String[] words = pdf.getWords();
        pdf.closePDF();

        String[] searchWords = {"clarissa", "lovelace", "letter", "dear", "miss", "virtue"};

        for(int i = 0; i< 30; i++){
            Sequencial sequencial = new Sequencial(words,searchWords);
            System.out.println("Tempo de execução: " + sequencial.getTime() + "ms");
            sequencial.print();
        }
    }
}
