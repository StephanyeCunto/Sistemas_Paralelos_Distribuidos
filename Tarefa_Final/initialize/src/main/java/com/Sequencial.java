package com;

import java.util.List;
import lombok.Getter;

@Getter
public class Sequencial extends Programas{
    private int[] timeSequencial;
    private final int INTERATIONS;

    public Sequencial(int INTERATIONS){
        this.INTERATIONS= INTERATIONS;
        this.timeSequencial = new int [INTERATIONS];

        initialize();
    }

    @Override
    protected void initialize(){
        for(int i=0; i<INTERATIONS; i++){
            long startTime = System.currentTimeMillis();

            List<String> command = super.createCommand(0,"sequencial");
            Process process = super.initializeProcess(command);
            super.closeProcess(process);

            long endTime = System.currentTimeMillis();
            timeSequencial[i] =Math.toIntExact(endTime - startTime);

        }
    }

    public int[] getTimeSequencial(){
        return timeSequencial;
    }
}