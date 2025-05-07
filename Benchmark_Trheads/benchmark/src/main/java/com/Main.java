package com;

public class Main {

    public static void main(String[] args) {
        OpenPDF openPDF = OpenPDF.getInstance();
        String[] words = openPDF.getWords(); 
         for(int i=0; i<30; i++){
            ProcessBenchmark sequencial = new ProcessBenchmark("1",words);
            System.out.println(sequencial.getTime()+" ms");
        }
            
    }
}