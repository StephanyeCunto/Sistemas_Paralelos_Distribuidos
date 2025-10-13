package com;

public class Main {

    private static final int[] THREAD = {2, 4, 8};
    private static final int INTERATIONS = 3;

    private static int[] timeSequencial;
    private static int[][] timeSimples;
    private static int[][] timeForkJoin;
    private static int[][] timePool;
    private static int[][] timeVirtual;

    public static void main(String[] args) {
        loadTime();
        ProcessData data = new ProcessData(timeSequencial, timeSimples, timePool, timeForkJoin, timeVirtual, INTERATIONS, THREAD);
        data.print();
    }

    private static void loadTime() {
        Sequencial sequencial = new Sequencial(INTERATIONS);
        timeSequencial = sequencial.getTimeSequencial();

        Paralelos simples = new Paralelos(INTERATIONS, THREAD, "simples");
        timeSimples = simples.getTimeParalelo();

       Paralelos pool = new Paralelos(INTERATIONS, THREAD, "pool");
        timePool = pool.getTimeParalelo();

        Paralelos virtual = new Paralelos(INTERATIONS, THREAD, "virtual");
        timeVirtual = virtual.getTimeParalelo();
       
        Paralelos forkjoin = new Paralelos(INTERATIONS, THREAD, "forkjoin");
        timeForkJoin = forkjoin.getTimeParalelo();
  
    }
}
