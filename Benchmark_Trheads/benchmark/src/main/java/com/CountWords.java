package com;

import java.io.*;
import java.util.*;

public class CountWords {
    static HashMap<String, Integer> wordMap = new HashMap<>();


    public static void main(String[] args) {
        ArrayList<String> words = new ArrayList<>();
        int numberWordsSearchs =0;
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String line;
            numberWordsSearchs = Integer.parseInt(reader.readLine());
            for(int i=0; i<numberWordsSearchs;i++){
                wordMap.put(reader.readLine(),i);
            }
            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                words.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    searchWords(words,numberWordsSearchs);  
    }

    public static void searchWords(ArrayList<String> words,int numberWordsSearchs) {
        int[] searchWordsCount = new int[numberWordsSearchs];  

        for (String word : words) {
            Integer index = wordMap.get(word);
            if (index != null) {
                searchWordsCount[index]++;
            }
        }

        for (int i = 0; i < numberWordsSearchs; i++) {
            System.out.println(searchWordsCount[i]);
        }

    }

}
