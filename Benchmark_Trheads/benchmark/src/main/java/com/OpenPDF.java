package com;

import java.io.*;
import java.util.*;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import lombok.Getter;

@Getter
public class OpenPDF {
    private static OpenPDF instance; 
    private String[] words;

    public static OpenPDF getInstance() {
        if (instance == null) {
            instance = new OpenPDF();
        }
        return instance;
    }
    
    private OpenPDF(){
        File file = new File("benchmark/src/main/resources/com/pdf/Clarissa Harlowe.pdf");
        try(PDDocument document = PDDocument.load(file)){
            PDFTextStripper pdfTextStripper = new PDFTextStripper();
            extractWords(pdfTextStripper.getText(document));
        }catch(IOException e){
            System.out.println("Erro ao abrir o arquivo: "+e);
        }
    }

    private void extractWords(String text){
        words = Arrays.stream(text.replaceAll("[^a-zA-Z]", " ").toLowerCase()
        .split("\\s+")).filter(s-> !s.isEmpty()).toArray(String[]::new);
    }
}
