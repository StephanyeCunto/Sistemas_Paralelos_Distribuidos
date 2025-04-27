package com;

import java.util.Arrays;

import org.apache.commons.math3.stat.descriptive.rank.Percentile;

public class Tester {
    private String[] words;
    private String[][] searchWords = {{"clarissa", "lovelace", "letter", "dear", "miss", "virtue"},
    {"eita", "bacana", "vixe", "forbidden", "indignation", "oppression"}};
    private int[] threads = {2,4,8};

    private int interactions = 30;

    private int[][] timeSequencialSearchWords = new int[searchWords.length][interactions];
    private int[][][] timeParaleloSearchWords = new int[searchWords.length][threads.length][interactions];

    private float[] timeSequencialAvarage = new float[searchWords.length];
    private float[][] timeParaleloAvarage = new float[searchWords.length][threads.length];

    public Tester(String[] words) {
        this.words = words;

        testerSearchWords();
        removeWarmUp();
        removeOutliers();
        setAverage();
        print();
    }

    private void testerSearchWords(){
        for(int i = 0; i< searchWords.length; i++){
            for(int j = 0; j< interactions; j++){
                Sequencial sequencial = new Sequencial(words,searchWords[i]);
                timeSequencialSearchWords[i][j] = (int) sequencial.getTime();
            }
        }

        for(int i = 0; i< searchWords.length; i++){
            for(int j = 0; j< threads.length; j++){
                for(int k = 0; k< interactions; k++){
                    Paralelo paralelo = new Paralelo(threads[j],words,searchWords[i]);
                    timeParaleloSearchWords[i][j][k] = (int) paralelo.getTime();
                }
            }
        }
    }

    private void removeWarmUp(){
        for(int i = 0; i< searchWords.length; i++){
            timeSequencialSearchWords[i] = getWarmUp(timeSequencialSearchWords[i]);
            for(int j = 0; j< threads.length; j++){
                timeParaleloSearchWords[i][j] = getWarmUp(timeParaleloSearchWords[i][j]);

            }
        }
    }

    private int[] getWarmUp(int[] time){
        int[] timeWarmUp = new int[interactions - interactions/10];

        for(int i= interactions/10; i< interactions; i++){
            timeWarmUp[i - interactions/10] = time[i];
        }
        return timeWarmUp;
    }

    private void removeOutliers(){
        for(int i = 0; i< searchWords.length; i++){
            timeSequencialSearchWords[i] = getOutliers(timeSequencialSearchWords[i]);
            for(int j = 0; j< threads.length; j++){
                timeParaleloSearchWords[i][j] = getOutliers(timeParaleloSearchWords[i][j]);
            }
        }
    }

    private int[] getOutliers(int[] time){
        double[] doubleArray = Arrays.stream(time).asDoubleStream().toArray();
        
        Percentile percentile = new Percentile();
        percentile.setData(doubleArray);
        double q1 = percentile.evaluate(25);
        double q3 = percentile.evaluate(75);
        
        double iqr = q3 - q1;

        double lowerBound = q1 - (1.5 * iqr);
        double upperBound = q3 + (1.5 * iqr);

        return Arrays.stream(time).filter(times -> times >= lowerBound && times <= upperBound).toArray();
    }

    private void setAverage(){
        for(int i = 0; i< searchWords.length; i++){
            timeSequencialAvarage[i] = getAverage(timeSequencialSearchWords[i]);
            for(int j = 0; j< threads.length; j++){
                timeParaleloAvarage[i][j] = getAverage(timeParaleloSearchWords[i][j]);
            }
        }
    }
    private float getAverage(int[] time){
        float sum = 0;
        for(int i=0; i< time.length; i++){
            sum += time[i];
        }

        return sum/(time.length);
    }

    private void print() {
        System.out.println("Tempo Sequencial: ");
        for(int i = 0; i< searchWords.length; i++){
            System.out.println("Palavras: " + Arrays.toString(searchWords[i]));
            System.out.println("Tempo: " + Arrays.toString(timeSequencialSearchWords[i]));
            System.out.println("Média: " + timeSequencialAvarage[i]+" μs(Microsegundos)");
        }

        for(int i = 0; i< searchWords.length; i++){
            for(int j = 0; j< threads.length; j++){
                System.out.println("Palavras: " + Arrays.toString(searchWords[i]) + " Threads: " + threads[j]);
                System.out.println("Tempo: " + Arrays.toString(timeParaleloSearchWords[i][j]));
                System.out.println("Média: " + timeParaleloAvarage[i][j] +" μs(Microsegundos)");
        }
    }

   /*  private void print() {
        System.out.println("Tempo Sequencial: ");
        for(int i = 0; i< 30; i++){
            System.out.print(timeSequencialSearchWords[i] + " ");
        }
        System.out.println();

        System.out.println("Tempo Paralelo 2: ");
        for(int i = 0; i< 30; i++){
            System.out.print(timeParalelo2SearchWords[i] + " ");
        }
        System.out.println();

        System.out.println("Tempo Paralelo 4: ");
        for(int i = 0; i< 30; i++){
            System.out.print(timeParalelo4SearchWords[i] + " ");
        }
        System.out.println();

        System.out.println("Tempo Paralelo 8: ");
        for(int i = 0; i< 30; i++){
            System.out.print(timeParalelo8SearchWords[i] + " ");
        }
        System.out.println();
    }

    private void testerSearchWords() {
        for(int i = 0; i< interactions; i++){
            Sequencial sequencial = new Sequencial(words,searchWords);
            timeSequencialSearchWords[i] = (int) sequencial.getTime();
        }

        for(int i = 0; i< interactions; i++){
            Paralelo paralelo = new Paralelo(2,words,searchWords);
            timeParalelo2SearchWords[i] = (int) paralelo.getTime();
        }

        for(int i = 0; i< interactions; i++){
            Paralelo paralelo = new Paralelo(4,words,searchWords);
            timeParalelo4SearchWords[i] = (int) paralelo.getTime();
        }

        for(int i = 0; i< interactions; i++){
            Paralelo paralelo = new Paralelo(8,words,searchWords);
            timeParalelo8SearchWords[i] = (int) paralelo.getTime();
        }
    }

    private void testerSearchWords2() {
        for(int i = 0; i< interactions; i++){
            Sequencial sequencial = new Sequencial(words,searchWords2);
            timeSequencialSearchWords2[i] = (int) sequencial.getTime();
        }

        for(int i = 0; i< interactions; i++){
            Paralelo paralelo = new Paralelo(2,words,searchWords2);
            timeParalelo2SearchWords2[i] = (int) paralelo.getTime();
        }

        for(int i = 0; i< interactions; i++){
            Paralelo paralelo = new Paralelo(4,words,searchWords2);
            timeParalelo4SearchWords2[i] = (int) paralelo.getTime();
        }

        for(int i = 0; i< interactions; i++){
            Paralelo paralelo = new Paralelo(8,words,searchWords2);
            timeParalelo8SearchWords2[i] = (int) paralelo.getTime();
        }
    }

    private int[] getWarmUp(int[] time){
        int[] timeWarmUp = new int[interactions/10];

        for(int i= 0; i< interactions/10; i++){
            timeWarmUp[i] = time[i];
        }

        return timeWarmUp;
    }

    private int getAverage(int[] time){
        int sum = 0;
        for(int i=interactions/10; i< interactions; i++){
            sum += time[i];
        }

        return sum/(interactions - interactions/10);
    }

*/
    }
}
