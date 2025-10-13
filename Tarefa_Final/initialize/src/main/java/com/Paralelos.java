package com;

import java.util.List;
import lombok.Getter;

@Getter
public class Paralelos extends Programas{
    private int[][] timeParalelo;
    private final int INTERATIONS;
    private final int[] THREAD;
    private final String PARALELO;

    public Paralelos(int INTERATIONS, int[] THREAD, String PARALELO){
        this.INTERATIONS= INTERATIONS;
        this.THREAD = THREAD;
        this.PARALELO = PARALELO;
        this.timeParalelo = new int [THREAD.length][INTERATIONS];

        initialize();
    }

    @Override
    protected void initialize(){
        for(int i=0; i<THREAD.length; i++){
            for(int j=0; j < INTERATIONS; j++){
            long startTime = System.currentTimeMillis();

                List<String> command = super.createCommand(THREAD[i],PARALELO);
                Process process = super.initializeProcess(command);
                super.closeProcess(process);

                long endTime = System.currentTimeMillis();
                timeParalelo[i][j] = Math.toIntExact(endTime - startTime);
            }
        }
    }

    public int[][] getTimeParalelo(){
        return timeParalelo;
    }
}