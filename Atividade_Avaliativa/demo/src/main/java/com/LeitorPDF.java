package com;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;

public class LeitorPDF {
    public static void main(String[] args) {
            File arquivo = new File("Atividade_Avaliativa/demo/src/main/resources/Clarissa Harlowe.pdf");

        /*    PDDocument documento = PDDocument.load(arquivo);

            PDFTextStripper stripper = new PDFTextStripper();
            String texto = stripper.getText(documento);
            String[] palavra = texto.replaceAll("[^a-zA-Z]", " ").split(" ");
            */

            int clarissaCount = 0;
            int lovelaceCount = 0;
            int letterCount = 0;
            int dearCount = 0;
            int missCount = 0;
            int virtueCount = 0;

            OpenPDF pdf = new OpenPDF("Atividade_Avaliativa/demo/src/main/resources/Clarissa Harlowe.pdf");
            String[] palavras = pdf.getWords();

            for(String s : palavras) {
                if(s.equals("clarissa")) {
                    System.out.println("A palavra Clarissa foi encontrada no PDF.");
                    clarissaCount++;
                }else if(s.equals("lovelace")) {
                    System.out.println("A palavra lovelace foi encontrada no PDF.");
                    lovelaceCount++;
                }else if(s.equals("letter")) {
                    System.out.println("A palavra letter foi encontrada no PDF.");
                    letterCount++;
                }else if(s.equals("dear")){
                    System.out.println("A palavra dear foi encontrada no PDF.");
                    dearCount++;
                }else if(s.equals("miss")){
                    System.out.println("A palavra miss foi encontrada no PDF.");
                    missCount++;
                }else if(s.equals("virtue")){
                    System.out.println("A palavra virtue foi encontrada no PDF.");
                    virtueCount++;
                }
            }

            System.out.println("A palavra Clarissa foi encontrada " + clarissaCount + " vezes.");
            System.out.println("A palavra lovelace foi encontrada " + lovelaceCount + " vezes.");
            System.out.println("A palavra letter foi encontrada " + letterCount + " vezes.");
            System.out.println("A palavra dear foi encontrada " + dearCount + " vezes.");
            System.out.println("A palavra miss foi encontrada " + missCount + " vezes.");
            System.out.println("A palavra virtue foi encontrada " + virtueCount + " vezes.");
            System.out.println("Total de palavras: " + palavras.length);

        //    documento.close();

            pdf.closePDF();
    }
}
