package com;

import java.util.Arrays;

public class Tester {
    private String[] words;
    private String[] searchWords = {"clarissa", "lovelace", "letter", "dear", "miss", "virtue"};
    private String[] searchWords2 = {"eita", "bacana", "vixe", "forbidden", "indignation", "oppression"};
    private int interactions = 30;

    private int[] timeSequencialSearchWords = new int[interactions];
    private int[] timeParalelo2SearchWords = new int[interactions];
    private int[] timeParalelo4SearchWords = new int[interactions];
    private int[] timeParalelo8SearchWords = new int[interactions];

    private int[] timeSequencialSearchWords2 = new int[interactions];
    private int[] timeParalelo2SearchWords2 = new int[interactions];
    private int[] timeParalelo4SearchWords2 = new int[interactions];
    private int[] timeParalelo8SearchWords2 = new int[interactions];

    public Tester(String[] words) {
        this.words = words;

        testerSearchWords();
        testerSearchWords2();  
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


public int[] removeOutliers(int[] times) {
    int[] sortedTimes = times.clone();
    Arrays.sort(sortedTimes);
    
    int n = sortedTimes.length;
    double q1Index = 0.25 * (n + 1);
    double q3Index = 0.75 * (n + 1);
    
    double q1 = getQuartileValue(sortedTimes, q1Index);
    double q3 = getQuartileValue(sortedTimes, q3Index);
    
    double iqr = q3 - q1;
    double lowerBound = q1 - (1.5 * iqr);
    double upperBound = q3 + (1.5 * iqr);
    
    int count = 0;
    for (int time : times) {
        if (time >= lowerBound && time <= upperBound) {
            count++;
        }
    }
    int[] result = new int[count];
    int index = 0;
    for (int time : times) {
        if (time >= lowerBound && time <= upperBound) {
            result[index++] = time;
        }
    }
    
    return result;
}

private double getQuartileValue(int[] sortedArray, double index) {
    if (index % 1 == 0) {
        return sortedArray[(int)index - 1];
    } else {
        int lowerIndex = (int)Math.floor(index) - 1;
        int upperIndex = (int)Math.ceil(index) - 1;
        
        lowerIndex = Math.max(0, lowerIndex);
        upperIndex = Math.min(sortedArray.length - 1, upperIndex);
        
        double fraction = index - Math.floor(index);
        return sortedArray[lowerIndex] + fraction * (sortedArray[upperIndex] - sortedArray[lowerIndex]);
    }
}
}
