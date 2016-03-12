/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockRMI;

import java.io.*;
import java.rmi.Naming;
import java.rmi.RemoteException;

/**
 * This it the third class that exists in the client side project. It prompts
 * for a user to subscribe or unsubscribe a stock. 
 *
 * @author ruijieouyang
 * @version 1.0
 * @since 11/12/2015
 */
public class StockRMIClientSubscription {

    //Assume users input are correct format

    public static void main(String args[]) throws Exception {
        try {
            System.out.println("USER INTERACTION SCREEN         USER INTERACTION SCREEN ");

            BufferedReader in
                    = new BufferedReader(
                            new InputStreamReader(System.in));
            //prompt user name
            System.out.print("Enter user name:");
            String userName = in.readLine();
            System.out.println("Looking up the server in registry.");
            StockRMI stockService = (StockRMI) Naming.lookup("//localhost/stockService");
            System.out.println("Will pass user name and the stock of interests to server");
            System.out.println("Enter S for subscribe or U for unsubscribe followed by the stock symbol of interest."
                    + "Enter ! to quit.");

            while (true) {
                System.out.print("client>");
                String line = in.readLine();
                //if client subscribe the stock, this client will begin to receives updates on those stocks
                //as the stock price arrive from the extrenal source of events.
                if (line.charAt(0) == 'S') {
                    String stockSym = line.substring(2);
                    //if the client succesfully subscribe the stock
                    if (stockService.subscribe(userName, stockSym)) {
                        System.out.println("Calling server with S : " + stockSym);
                    }
                    //if the client fails to subscribe the stock
                    else {
                        System.out.println("Failed subscribe,user already subscribed to the stock");
                    }
                } //when unsubscribing, the updates stop arriving.
                else if (line.charAt(0) == 'U') {
                    String stockSym = line.substring(2);
                    if (stockService.unSubscribe(userName, stockSym)) {
                        System.out.println("Calling server with U : " + stockSym);
                    } else {
                        System.out.println("Failed unsubscribe");
                    }


                } //when client exits(with '!'), server is informed by a client side call on the server's deRegisterCallBack() method.
                //Server then calls the client side exit() method and the client no longer receives call backs from the server.
                else {
                    stockService.deRegisterCallBack(userName);
                    break;
                }

            }
        } catch (RemoteException e) {
            System.out.println("Exception at StockRMIClientSubscription: " + e.getMessage());
        }
    }

}
