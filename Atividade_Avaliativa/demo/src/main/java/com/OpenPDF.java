package com;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class OpenPDF {
    private String filePath;
    private String[] words;
    private PDDocument document;

    public OpenPDF(String filePath) {
        this.filePath = filePath;
        openPDF();
    }

    private void openPDF(){
        try{
            File file = new File(this.filePath);
            document = PDDocument.load(file);
            PDFTextStripper pdfStripper = new PDFTextStripper();
            extractWords(pdfStripper.getText(document));
        }catch (IOException e) {
            System.out.println("Erro ao ler o PDF: " + e.getMessage());
        }
    }

    private void extractWords(String text){
        words = text.replaceAll("[^a-zA-Z]", " ").split(" ");
        for(int i = 0; i < words.length; i++){
            words[i] = words[i].toLowerCase();
        }
    }

    public void closePDF(){
        try{
            document.close();
        }catch (IOException e) {
            System.out.println("Erro ao fechar o PDF: " + e.getMessage());
        }
    }

    public String[] getWords(){
        return words;
    }
}
