package controller;

import Service.FanService;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServer {
    public static void main(String[] args) {
        try {
            FanService fanService = new FanService();
            Registry registry = LocateRegistry.createRegistry(5000);
            registry.rebind("FanHubService", fanService);
            System.out.println("FanHub RMI Server ready on port 5000");
        } catch (RemoteException e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}