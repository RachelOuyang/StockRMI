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
 * This is the second client class that has a main method that interacts with a
 * user at the keyboard. The user enters lines of text in the following format:
 * <stockSym> <price> or
 * <!> to quit. No user name associated with this client. This is an external
 * source of event.
 *
 * @author ruijieouyang
 * @version 1.0
 * @since 11/12/2015
 */
public class StockRMIClientPriceUpdate {

    /**
     * Pre: Assume user's input are correct format.
     * main method that interacts with a user at the keyboard. The user enters
     * lines of text in the following format: <stockSym> <price> or
     * <!> to quit.
     *
     * @param args
     * @throws Exception
     */
    public static void main(String args[]) throws Exception {
        System.out.println("PRICE UPDATE TERMINAL                        PRICE UPDATE TERMINAL");
        System.out.println("Looking up the server in registry.");
        StockRMI stockService = (StockRMI) Naming.lookup("//localhost/stockService");
        System.out.println("Sending new prices to the server");
        System.out.println("Enter stock symbol and price or ! to quit.");
        BufferedReader in
                = new BufferedReader(
                        new InputStreamReader(System.in));

        while (true) {
            try {
                //prompt the user
                System.out.print("update>");
                //each time the user enters a line of text
                String line = in.readLine();
                //if user enters "!", exit
                if (line.equals("!")) {
                    break;
                } //else then server is called and the stock symbol and price is passed to a remote method named stockUpdate.
                //stockUpdate method makes remote calls to all of the dealder computers that have registered an interest in that stock.
                //In other words, the stockUpdate method will be calling the notify method on all clients that have registered interest in that particular stock.
                else {
                    String[] info = line.split(" ");
                    String stockSym = info[0];
                    double price = Double.parseDouble(info[1]);

                    stockService.stockUpdate(stockSym, price);
                    System.out.println("Read " + stockSym + " " + price);
                    System.out.println("Sending updates of " + stockSym + " to " + price);
                }

            } catch (RemoteException e) {
                System.out.println("Exception at StockRMIClientPriceUpdate: " + e.getMessage());
            }

        }
    }

}
