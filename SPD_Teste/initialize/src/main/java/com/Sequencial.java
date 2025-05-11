package com;

import java.util.List;
import lombok.Getter;

@Getter
public class Sequencial extends Programas{
    private int[][] timeSequencial;

    public Sequencial(String[][] SEARCH_WORDS, int INTERATIONS){
        super(SEARCH_WORDS, INTERATIONS);
        this.timeSequencial = new int[SEARCH_WORDS.length][INTERATIONS];

        initialize();
    }

    @Override
    protected void initialize(){
        for(int i = 0; i < super.getSEARCH_WORDS().length; i++){
            for(int j = 0; j < super.getINTERATIONS(); j++){
                int startTime=(int) System.currentTimeMillis();

                List<String> command = super.createCommand(i,"sequencial");

                Process process = super.initializeProcess(command);

                if(process!= null){ 
                    String[] resultSearch = super.getResultSearch(process);
                    super.closeProcess(process);
                    timeSequencial[i][j] = super.getTime(startTime,resultSearch[0]);

                    for(String result : resultSearch){
                        System.out.println(result);
                    }
                       
                }

            }
        }
    }
}
