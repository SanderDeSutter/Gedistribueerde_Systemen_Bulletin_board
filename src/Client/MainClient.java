package Client;

import Common.BulletinBoard;
import Common.Value;
import Common.ValueTagPair;
import Encryption.KDF;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.util.UUID;


public class MainClient {

    public static void main(String[] args) throws IOException, NotBoundException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Scanner scanner = new Scanner(System.in);
        int currentIdx;
        String currentTag;
        System.out.print("Enter the startindex for sending: ");
        currentIdx = Integer.parseInt(scanner.nextLine());
        System.out.println("Enter the starttag for sending: ");
        currentTag = scanner.nextLine();
        KDF kdf = new KDF();
        SecretKey sendingKey= new SecretKey(){
            @Override
            public String getAlgorithm() {
                return null;
            }

            @Override
            public String getFormat() {
                return null;
            }

            @Override
            public byte[] getEncoded() {
                return new byte[0];
            }
        };
        System.out.print("Geef key: ");
        String key = scanner.nextLine();
        sendingKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), 0, key.getBytes(StandardCharsets.UTF_8).length, "AES");
        System.out.println(sendingKey);

        System.out.println("We maken verbinding met de server");

        // fire to localhost port 1099
        Registry myRegistry = LocateRegistry.getRegistry("localhost", 1099);

        // search for CounterService
        BulletinBoard impl = (BulletinBoard) myRegistry.lookup("BulletinBoardService");

        //impl.sendMessage(5,new ValueTagPair(new Value(),"test"));
        //uncomment voor client1
        new ClientThread(impl,1 ,"b", "poepsnoeppoepsno" ).start();

        //uncomment voor client2
        //new ClientThread(impl,0,"a","appelappelappela").start();

        boolean continueSending = true;
        Cipher cipher = Cipher.getInstance("AES");

        while(continueSending){
            //System.out.println("Bericht te verzenden ");
            //String message = scanner.nextLine();

            BufferedReader stdIn =
                    new BufferedReader(new InputStreamReader(System.in));
            String message = stdIn.readLine();
            if(message.equals("exit")) continueSending = false;
            else {
                //all nieuwe index en tag genereren en meegeven
                String nextTag = randomString();
                int nextIdx = randomInteger();

                int tempCurrentIndex = currentIdx;
                String tempCurrentTag = currentTag;

                currentTag = nextTag;
                currentIdx = nextIdx;

                Value value = new Value(message, nextTag, nextIdx);
                byte[] valueToBytes = value.toByteArray();
                byte[] tempValueToBytes = new byte[8];
                System.arraycopy(valueToBytes,0,tempValueToBytes, 0, 8);

                System.out.println(sendingKey);
                cipher.init(Cipher.ENCRYPT_MODE, sendingKey);
                System.out.println(valueToBytes.length);
                byte[] encryptedValueToBytes = cipher.doFinal(valueToBytes);
                //TODO encrpt valueToBytes

                impl.sendMessage(tempCurrentIndex, new ValueTagPair(encryptedValueToBytes , tempCurrentTag));

                //Generating a new key
                byte[] keyToByteArray = sendingKey.getEncoded();
                byte[] newKey = kdf.hkdfExpand(keyToByteArray,tempValueToBytes,128);
                sendingKey = new SecretKeySpec(newKey, 0, 16, "AES");

            }
        }
    }

    public static String randomString(){
        String random = UUID.randomUUID().toString().replace("-","").substring(0,8);
        return random;
    }
  /*
    public static String randomString(){
        return "tag";
    }
    */
    public static int randomInteger(){
        double randNumber = Math.random();
        double d = randNumber * 19;
        return (int)d;
    }




}

