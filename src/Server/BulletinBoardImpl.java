package Server;

import Common.Value;
import Common.ValueTagPair;
import Encryption.KDF;

import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.LinkedList;

public class BulletinBoardImpl extends UnicastRemoteObject implements Common.BulletinBoard {
    private LinkedList<ValueTagPair>[] cells;

    public BulletinBoardImpl() throws RemoteException{
        this.cells = new LinkedList[20];
        for (int i = 0; i<cells.length;i++) {
            this.cells[i] = new LinkedList();
        }
    }
    @Override
    public synchronized void sendMessage(int idx, ValueTagPair valueTagPair){
        cells[idx].add(valueTagPair);
        notifyAll();
    }

    @Override
    public synchronized byte[] receive(int idx, int hashTag){

        while (true) {
            //System.out.println("while loop");
            try {
                //System.out.println("waiting");
                wait();
            } catch (InterruptedException e) {
                System.out.println(e.toString());
                System.out.println("fout gelopen eh ja hier");
            }
            //System.out.println("quit waiting");
            Value value = checkValue(idx, hashTag);
            if(value!=null) {
                System.out.println("SERVER: " + new String(value, StandardCharsets.UTF_8));
                notifyAll();
                return value;
            }
        }
    }

    //Checks if there is a new message at the given index.
    byte[] checkValue(int idx, int hashTag) {
        for (int i = 0; i < cells[idx].size(); i++) {
            ValueTagPair temp = cells[idx].get(i);
            if (temp.getTag().hashCode()==hashTag) {
                System.out.println("hash2: "+temp.getTag().hashCode());
                return temp.getValue();
            }
        }
        return null;
    }
    public void removeVTP(String tag, int idx){
        //System.out.println("voor remove: "+cells[idx]);
        for (int i = 0; i < cells[idx].size(); i++) {
            ValueTagPair temp = cells[idx].get(i);
            if (temp.getTag().equals(tag)) {
               cells[idx].remove(i);
            }
        }
        //System.out.println("na remove: "+cells[idx]);
    }

    /*

    @Override
    public synchronized void sendMessage(String clientname , String message) throws RemoteException {
        cells[0].add(clientname + " " +message);
        notifyAll();
    }
    @Override
    public String receive() throws RemoteException {
        String message = this.cells[0].get(0);
        return message;
    }

     */

//    public String get(int i, String b){
//
//
//    }
}
