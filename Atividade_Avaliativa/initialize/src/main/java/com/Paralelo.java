package com;

import java.util.List;
import lombok.Getter;

@Getter
public class Paralelo extends Programas{
    private int[] THREAD;

    private int[][][] timeParalelo;

    public Paralelo(String[][] SEARCH_WORDS, int[] THREAD,int INTERATIONS,String[] WORDS){
        super(SEARCH_WORDS, INTERATIONS,WORDS);
        this.THREAD = THREAD;
        timeParalelo = new int[super.getSEARCH_WORDS().length][this.THREAD.length][super.getINTERATIONS()];
        
        initialize();
    }

    @Override
    protected void initialize(){
        for(int i = 0; i < super.getSEARCH_WORDS().length; i++){
            for(int j = 0; j < THREAD.length; j++){
                for(int k = 0; k < super.getINTERATIONS(); k++){
                    long startTime= System.currentTimeMillis();

                    List<String> command = super.createCommand(i,"paralelo");
                    command.addLast(String.valueOf(THREAD[j]));

                    Process process = super.initializeProcess(command);
                    if(process!= null){ 
                        String[] resultSearch = super.getResultSearch(process);
                        super.closeProcess(process);

                        timeParalelo[i][j][k] = (int) (System.currentTimeMillis() - startTime);
                          
                         for(String result : resultSearch){
                            System.out.println(result);
                        }
                    
                    }
                } 
            }
        }
    }
}