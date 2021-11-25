package Client;
import Common.BulletinBoard;
import Common.Value;
import Common.ValueTagPair;


import javax.swing.*;
import java.rmi.RemoteException;


public class ClientThread extends Thread {
    private String nextTag;
    private int nextIdx;


    private BulletinBoard bulletinBoard;

    public ClientThread(BulletinBoard bulletinBoard, int startIdx, String startTag) {
        this.bulletinBoard = bulletinBoard;
        nextTag = startTag;
        nextIdx = startIdx;
    }

    public void run() {
        Value value = new Value();
        while(true) {
            try {
                value = bulletinBoard.receive(nextIdx, nextTag);
                if(value!=null) {
                    bulletinBoard.removeVTP(nextTag, nextIdx);
                    nextTag = value.getNextTag();
                    nextIdx = value.getNextIdx();
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            System.out.println("\u001B[33m" + value.getMessage()+"\033[0;97m");
        }
    }
}