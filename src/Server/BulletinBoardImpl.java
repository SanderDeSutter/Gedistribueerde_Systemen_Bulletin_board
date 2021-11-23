package Server;

import Common.Value;
import Common.ValueTagPair;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
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
    public synchronized Value receive(int idx, String hashTag){
        while (checkValue(idx, hashTag)==null) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println(e.toString());
                System.out.println("fout gelopen eh ja hier");
            }
            Value value = checkValue(idx,hashTag);
            System.out.println(value);
            return new Value(value.getMessage(),value.getNextTag(),value.getNextIdx());
        }
        return null;
    }

    //Checks if there is a new message at the given index.
    Value checkValue(int idx, String hashTag) {
        for (int i = 0; i < cells[idx].size(); i++) {
            ValueTagPair temp = cells[idx].get(i);
            if (temp.getTag().equals(hashTag)) {
                return temp.getValue();
            }
        }
        return null;

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
