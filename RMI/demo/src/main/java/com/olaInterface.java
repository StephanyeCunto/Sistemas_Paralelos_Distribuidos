package com;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface olaInterface extends Remote{
    String digaOla() throws RemoteException;
}
