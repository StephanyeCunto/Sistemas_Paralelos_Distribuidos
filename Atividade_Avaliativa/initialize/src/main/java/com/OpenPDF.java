package com;

import java.io.*;

import java.util.*;
import lombok.Getter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

@Getter
public class OpenPDF {
    private String[] words;

    public OpenPDF(String filePath) {
        long startTime = System.currentTimeMillis();
        File file = new File(filePath);
        openPDF(file);
    }
    
    private void openPDF(File file){
        try(PDDocument document = PDDocument.load(file)){
            PDFTextStripper pdfStripper = new PDFTextStripper();
            extractWords(pdfStripper.getText(document));
        }catch (IOException e) {
            System.out.println("Erro ao ler o PDF: " + e.getMessage());
        }
    }

    private void extractWords(String text){
        words = Arrays.stream(text.split("\\s+"))
        .map(word -> word.replaceAll("[^a-zA-Z]", " "))
        .map(String::trim)
        .map(String::toLowerCase)
        .filter(word -> !word.isEmpty()).toArray(String[]:: new);
    }
}