package com;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Cliente{
  public static void main(String[] args){
    String host = "192.168.1.2";
    try{
      Registry registry = LocateRegistry.getRegistry(host,1092);
      olaInterface stub = (olaInterface) registry.lookup("Ola");
      String resposta = stub.digaOla();
      System.out.println("resposta: " + resposta);
    } catch (Exception e) {
      System.err.println("Exceção no cliente" + e.toString());
      e.printStackTrace();
    }

    try{
      Thread.sleep(20000); 
    }catch (InterruptedException e) {
    System.out.println("Thread was interrupted while sleeping.");
    Thread.currentThread().interrupt(); // Re-interrupt the current thread
}
  }
}