package com;

import java.util.List;
import lombok.Getter;

@Getter
public class Sequencial extends Programas{
    private int[][] timeSequencial;

    public Sequencial(String[][] SEARCH_WORDS, int INTERATIONS,String[] WORDS){
        super(SEARCH_WORDS, INTERATIONS, WORDS);
        this.timeSequencial = new int[SEARCH_WORDS.length][INTERATIONS];

        initialize();
    }

    @Override
    protected void initialize(){
        for(int i = 0; i < super.getSEARCH_WORDS().length; i++){
            for(int j = 0; j < super.getINTERATIONS(); j++){

                List<String> command = super.createCommand(i,"sequencial");

                Process process = super.initializeProcess(command);

                if(process!= null){ 
                    String[] resultSearch = super.getResultSearch(process);
                    super.closeProcess(process);
                    timeSequencial[i][j] = super.getTime(resultSearch);

  /*                   for(String result : resultSearch){
                        System.out.println(result);
                    }
     */                
                }

            }
        }
    }
}