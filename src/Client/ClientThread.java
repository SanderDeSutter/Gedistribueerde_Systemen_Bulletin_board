package Client;

import Common.BulletinBoard;
import Common.Value;
import Encryption.KDF;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;


public class ClientThread extends Thread {
    private String nextTag;
    private int nextIdx;
    private KDF kdf;
    private SecretKey receivingKey;
    private LinkedList<String> messages;






    private BulletinBoard bulletinBoard;

    public ClientThread(BulletinBoard bulletinBoard, int startIdx, String startTag,String key) {
        this.bulletinBoard = bulletinBoard;
        nextTag = startTag;
        nextIdx = startIdx;
        this.kdf = new KDF();
        this.receivingKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), 0, key.getBytes(StandardCharsets.UTF_8).length, "AES");
        //System.out.println("receiving key: "+ receivingKey);
        messages = new LinkedList<>();
    }

    public void run() {
        byte[] value;
        Value objectValue = new Value();
        try {
            Cipher cipher = Cipher.getInstance("AES");

            while (true) {
                try {
                    int hashNextTag = nextTag.hashCode();
                    //System.out.println("hash: " + hashNextTag);
                    value = bulletinBoard.receive(nextIdx, hashNextTag);
                    if (value != null) {
                        bulletinBoard.removeVTP(nextTag, nextIdx);
                        //Value decrypteren
                        cipher.init(Cipher.DECRYPT_MODE, receivingKey);
                        byte[] valueDecrypted = cipher.doFinal(value);
                        byte[] keyToByteArray = receivingKey.getEncoded();
                        byte[] tempValueDecrypted = new byte[8];
                        System.arraycopy(valueDecrypted,0,tempValueDecrypted, 0, 8);
                        
                        byte[] newKey = kdf.hkdfExpand(keyToByteArray,tempValueDecrypted, 128);
                        receivingKey = new SecretKeySpec(newKey, 0, 16, "AES");
                        objectValue = Value.fromByteArray(valueDecrypted);
                        System.out.println(objectValue.getMessage());
                        messages.add(objectValue.getMessage());
                        nextTag = objectValue.getNextTag();
                        nextIdx = objectValue.getNextIdx();
                    }
                } catch (RemoteException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
                    e.printStackTrace();
                }
                //System.out.println("\u001B[33m" + objectValue.getMessage() + "\033[0;97m");
            }
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
        e.printStackTrace();
    }
    }

    public List<String> getNewMessages() {
        List<String> messagesToReturn = new LinkedList<>(messages);
        messages.clear();
        return messagesToReturn;

    }


}