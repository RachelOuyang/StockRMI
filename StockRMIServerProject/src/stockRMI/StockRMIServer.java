/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockRMI;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * This is the server class.
 * @author ruijieouyang
 * @version 1.0
 * @since 11/12/2015
 */
public class StockRMIServer {

    public static void main(String args[]) {
        try {
            System.out.println("SERVER      SERVER     STOCKRMISERVER");
            //create the servant
            StockRMIServant stockService = new StockRMIServant();
            System.out.println("Create StockRMIServant object");
            System.out.println("Placing in registry");
            //start up the rmiregistry and bing the servant to the registry
            Registry registry = LocateRegistry.createRegistry(1099);
            Naming.rebind("stockService", stockService);
            System.out.println("StockRMIServant object ready");
            System.out.println();
        }catch(Exception e) {
            System.out.println("CalculatorServer error main " + e.getMessage());
        }

    }

}
