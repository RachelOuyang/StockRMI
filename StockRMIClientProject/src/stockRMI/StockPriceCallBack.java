/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockRMI;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * This is the first client type. It provides callable methods to the server
 * side and would normally run on the dealer's computer.
 *
 * @author ruijieouyang
 * @version 1.0
 * @since 11/12/2015
 */
public class StockPriceCallBack extends UnicastRemoteObject implements Notifiable {

    /**
     * Constructor with no argument.
     *
     * @throws RemoteException
     */
    public StockPriceCallBack() throws RemoteException {

    }

    /**
     * Pre:Assume unique user name When the main routine of this class runs, it
     * will prompt the user for a user name and then it will perform a lookup on
     * the registry and will call the registerCallBack method on the stock
     * service. At that point, the server will have a remote reference to the
     * client's callback methods.
     *
     * @param arg
     * @throws Exception
     */
    public static void main(String arg[]) throws Exception {
        try {
            System.out.println("CALLBACK SCREEN       CALLBACKSCREEN");

            //prompt the user for a user name
            BufferedReader in
                    = new BufferedReader(
                            new InputStreamReader(System.in));
            System.out.println("Enter user name:");
            String userName = in.readLine();

            //perform a lookup on the registry
            System.out.println("Looking up the server in the registry");
            StockRMI stockService = (StockRMI) Naming.lookup("//localhost/stockService");

            //call the registerCallBalck method on the stock service
            System.out.println("Creating a callback object to handle calls from the server.");
            Notifiable callbackObj = new StockPriceCallBack();
            stockService.registerCallBack(callbackObj, userName);
            //at this point, the server will have a remote reference to the client's callback methods.
            System.out.println("Registering the callback with a name at the server.");
            System.out.println("Callback handler for " + userName + " ready.");
            System.out.println();

        } catch (Exception e) {
            System.out.println("Exception at StockPriceCallBack client:" + e);
        }

    }

    /**
     * The method notify(String stockSym, double price) is called by the server
     * and informs the callback client that a change in the stock price has
     * occurred.
     *
     * @param stockSym the stock name
     * @param price the current stock price
     * @throws RemoteException
     */
    @Override
    public void notify(String stockSym, double price) throws RemoteException {
        String result = "Call back received: Stock: " + stockSym + ", price: " + price;
        System.out.println(result);
    }

    /**
     * The method exit() is also called by the server and tells the callback
     * client that it should cease listening for this client. The user has
     * exited the system.
     *
     * @throws RemoteException
     */
    @Override
    public void exit() throws RemoteException {
        try {
            UnicastRemoteObject.unexportObject(this, true);
            System.out.println("StockPriceCallBack exiting.");
        } catch (Exception e) {
            System.out.println("Exception thrown" + e);
        }
    }

}
