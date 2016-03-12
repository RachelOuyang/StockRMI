/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockRMI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

/**
 * This is the servant class.
 *
 * @author ruijieouyang
 * @version 1.0
 * @since 11/12/2015
 */
public class StockRMIServant extends UnicastRemoteObject implements StockRMI {
    /* Given a stock, get price of stock and a list of users that are interested in that stock. */

    private static Map stocks = new TreeMap();
    /* Given a user, get the remote object reference to its callback method. */
    private static Map users = new TreeMap();

    /**
     * Constructor with no argument.
     *
     * @throws RemoteException
     */
    public StockRMIServant() throws RemoteException {

    }

    /**
     * The second client will call this method to register interest in a
     * particular stock.
     *
     * @param user the client name, user name.
     * @param stockSym the stock name
     * @return true if user successfully subscribe the stock.false other wise.
     * @throws RemoteException
     */
    @Override
    public boolean subscribe(String user, String stockSym) throws RemoteException {
        //if the stock already exists in the stocks map,always add the user at the first position of index, 
        //which directly follows the stock price in the LinkedList.
        if (stocks.containsKey(stockSym)) {
            LinkedList<String> stockInfo = (LinkedList) stocks.get(stockSym);
            //only allow users to subscribe once
            if (!stockInfo.contains(user)) {
                stockInfo.add(1, user);
                System.out.println(user + " subscribes to stock " + stockSym);
                return true;
            }
            return false;

        } //if the stock does not in the stocks map at this point, set the price of stock to -1, add user to stocks map.
        else {
            LinkedList<String> stockValue = new LinkedList<>();
            stockValue.add(0, "-1");
            stockValue.add(1, user);
            stocks.put(stockSym, stockValue);
            System.out.println(user + " subscribes to stock " + stockSym);
            return true;
        }

    }

    /**
     * The second client will call this method to remove interest from a
     * particular stock.
     *
     * @param user the client name, user name.
     * @param stockSym the stock name
     * @return true if user successfully unsubscribe the stock.false other wise.
     * @throws RemoteException
     */
    @Override
    public boolean unSubscribe(String user, String stockSym) throws RemoteException {
        //if the stock with name stockSym exists in the server, then remove user's interests on this stock
        if (stocks.containsKey(stockSym)) {
            LinkedList<String> stockValue = (LinkedList) stocks.get(stockSym);
            System.out.println(user + " unsubscribes to stock " + stockSym);
            return stockValue.remove(user);
        }
        //if the stockSym does not exists, return false, cannot unsubscribe the stock.
        return false;

    }

    /**
     * the stockUpdate method will be calling the notify method on all clients
     * that have registered interest in that particular stock.
     *
     * @param stockSym name of the stock
     * @param price price of the stock
     * @throws RemoteException
     */
    @Override
    public void stockUpdate(String stockSym, double price) throws RemoteException {
        //if the stock and its info alreay exists in stocks map,then update its original price to new price
        if (stocks.containsKey(stockSym)) {
            LinkedList<String> stockInfo = (LinkedList<String>) stocks.get(stockSym);
            String prePrice = stockInfo.set(0, "" + price);
            System.out.println("price of this stock " + stockSym + " updated.");
            //call back to the first client.
            doCallBack(stockSym);
        } //if the stock and its info do not exist in the stocks map, then add the new price
        else {
            LinkedList<String> stockValue = new LinkedList<>();
            stockValue.add(0, "" + price);
            stocks.put(stockSym, stockValue);
            System.out.println("New stock " + stockSym + " and new price received.");
        }

    }

    /**
     * A third type of client will be calling the registerCallBack() method so
     * that it may receive call backs from the service when stock prices change.
     *
     * @param remoteClient the remote client reference
     * @param user the remote client's name
     * @throws RemoteException
     */
    @Override
    public void registerCallBack(Notifiable remoteClient, String user) throws RemoteException {
        //store the callback object into the users map
        if (!(users.containsKey(user))) {
            users.put(user, remoteClient);
            System.out.println("Registered new client:" + user);
        }

    }

    /**
     * the second client will also call deRegisterCallBack() when it terminates.
     * If the second client exits, then the associated callback process
     * terminate automatically by calling remote client reference's method
     * exit().
     *
     * @param user client name, user name
     * @throws RemoteException
     */
    @Override
    public void deRegisterCallBack(String user) throws RemoteException {
        Notifiable remoteClient = (Notifiable) users.get(user);
        remoteClient.exit();
        System.out.println("Client " + user + " exits");
    }

    /**
     * This is the method will call the notify method on all clients that have
     * registered interest in that particular stock.
     *
     * @param stockSym name of stock
     * @throws RemoteException
     */
    private void doCallBack(String stockSym) throws RemoteException {
        LinkedList<String> stockInfo = (LinkedList<String>) stocks.get(stockSym);
        double price = Double.parseDouble(stockInfo.getFirst());
        if (stockInfo.size() > 1) { //other than the price, there is user string in the stockInfo list.
            for (int i = 1; i < stockInfo.size(); i++) {
                String user = (String) stockInfo.get(i);
                Notifiable remoteClient = (Notifiable) users.get(user);

                remoteClient.notify(stockSym, price);
            }
        }
    }

}
