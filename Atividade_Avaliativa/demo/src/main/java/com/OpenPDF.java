package com;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class OpenPDF {
    private final String filePath;
    private PDDocument document;
    private String[] words;

    public OpenPDF(String filePath) {
        this.filePath = filePath;
        openPDF();
    }
    
    private void openPDF(){
        try{
            File file = new File(this.filePath);
            document = PDDocument.load(file);
            PDFTextStripper pdfStripper = new PDFTextStripper();
            pdfStripper.setSortByPosition(false); 
            pdfStripper.setAddMoreFormatting(false);
            extractWords(pdfStripper.getText(document));
        }catch (IOException e) {
            System.out.println("Erro ao ler o PDF: " + e.getMessage());
        }
    }

    private void extractWords(String text){
        ArrayList<String> word = new ArrayList<>();
        words = text.replaceAll("[^a-zA-Z]", " ").toLowerCase().split("\\s+");
        for(int i = 0; i < words.length; i++){
            if(!words[i].isEmpty()){
                word.add(words[i]);
            }
        }
        words = word.toArray(new String[0]);
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
