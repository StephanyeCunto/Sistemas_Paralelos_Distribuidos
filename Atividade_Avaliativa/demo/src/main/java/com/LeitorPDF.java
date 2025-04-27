package com;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;

public class LeitorPDF {
    public static void main(String[] args) {
        try {
            File arquivo = new File("Atividade_Avaliativa/demo/src/main/resources/Uma_analise_de_genero_a_partir_de_dados.pdf");

            PDDocument documento = PDDocument.load(arquivo);

            PDFTextStripper stripper = new PDFTextStripper();
            String texto = stripper.getText(documento);

            System.out.println("Conteúdo do PDF:\n" + texto);

            documento.close();
        } catch (IOException e) {
            System.out.println("Erro ao ler o PDF: " + e.getMessage());
        }
    }
}
