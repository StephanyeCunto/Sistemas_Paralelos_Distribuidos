package com;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class Servidor implements olaInterface {

  public String digaOla(){
    String ola = "Ola Mundo!";
    System.out.println("Ola");
    return ola;
  }

  public static void main (String args[]){
    try{
        System.setProperty("java.rmi.server.hostname", "192.168.1.2");

        Servidor obj = new Servidor();
        olaInterface stub = (olaInterface) UnicastRemoteObject.exportObject(obj,0);

        Registry registry = LocateRegistry.createRegistry(1092);
        registry.bind("Ola", stub);

        System.err.println("Servidor pronto . . .");
      }catch (Exception e){
        System.err.println("Exceçao no servidor:  " + e.toString());
      e.printStackTrace();
      }
  }
}