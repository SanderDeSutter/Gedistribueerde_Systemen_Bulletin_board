package Client;

import Common.BulletinBoard;
import Common.Value;
import Common.ValueTagPair;
import Encryption.KDF;
import javafx.scene.layout.Border;
import javafx.scene.layout.Pane;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.UUID;
import java.util.List;


public class MainClient {

    //Import GUI needs
    String name;
    JFrame frame = new JFrame("Chatter");
    JTextField textField = new JTextField(50);
    JButton bSend = new JButton("Send");
    JButton bReceive = new JButton("Receive");
    String currentTag;
    int currentIdx;
    Scanner scanner = new Scanner(System.in);
    KDF kdf = new KDF();
    ClientThread clientThread;
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


    JTextArea messageArea = new JTextArea(16, 50);
    //JTextArea onlineClients = new JTextArea(40, 16);


    public MainClient() throws RemoteException, NotBoundException, NoSuchPaddingException, NoSuchAlgorithmException {
        this.frame=new JFrame();
        Panel textAndSendPanel = new Panel();

        //Make textfield
        textField.setEditable(true);
        textField.setBackground(Color.lightGray);

        //Make send message button
        bSend.setPreferredSize(new Dimension(100,20));
        bSend.setBackground(Color.GRAY);

        //Make receive messages button
        bReceive.setPreferredSize(new Dimension(100,20));
        bReceive.setBackground(Color.CYAN);

        //Add everything to the panel
        textAndSendPanel.add(textField);
        textAndSendPanel.add(bSend);
        textAndSendPanel.add(bReceive);
        bSend.setBounds(100,0,50,30);
        bSend.setVisible(true);

        //onlineClients.setEditable(false);

        //Add everything to the frame
        frame.getContentPane().add(textAndSendPanel, BorderLayout.SOUTH);
        frame.getContentPane().add(new JScrollPane(messageArea), BorderLayout.CENTER);
        this.setInitialValues();

        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setVisible(true);
        //set the initial index, tag and key

        System.out.println("We maken verbinding met de server");

        // fire to localhost port 1099
        Registry myRegistry = LocateRegistry.getRegistry("localhost", 1099);

        // search for CounterService
        BulletinBoard impl = (BulletinBoard) myRegistry.lookup("BulletinBoardService");

        //impl.sendMessage(5,new ValueTagPair(new Value(),"test"));
        //uncomment voor client1
        //ClientThread thread = new ClientThread(impl,1 ,"b", "poepsnoeppoepsno" );

        //uncomment voor client2
        ClientThread thread = new ClientThread(impl,0,"a","appelappelappela");
        thread.start();

        boolean continueSending = true;
        Cipher cipher = Cipher.getInstance("AES");

        bReceive.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> messages = thread.getNewMessages();
                for (String string : messages) {
                    messageArea.append("<html>Text color: <font color='green'>" + string + "</font></html>"  + "\n");
                }
            }
        });
        //frame.getContentPane().add(new JScrollPane(onlineClients), BorderLayout.WEST);
        bSend.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String message = textField.getText();
                messageArea.append(  "<html>Text color: <font color='red'>" + message+ "</font></html>"  + "\n");
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
                System.arraycopy(valueToBytes, 0, tempValueToBytes, 0, 8);

                System.out.println(sendingKey);
                try {
                    cipher.init(Cipher.ENCRYPT_MODE, sendingKey);
                } catch (InvalidKeyException invalidKeyException) {
                    invalidKeyException.printStackTrace();
                }
                System.out.println(valueToBytes.length);
                byte[] encryptedValueToBytes = new byte[0];
                try {
                    encryptedValueToBytes = cipher.doFinal(valueToBytes);
                } catch (IllegalBlockSizeException | BadPaddingException illegalBlockSizeException) {
                    illegalBlockSizeException.printStackTrace();
                }
                //TODO encrpt valueToBytes

                try {
                    impl.sendMessage(tempCurrentIndex, new ValueTagPair(encryptedValueToBytes, tempCurrentTag));
                } catch (RemoteException remoteException) {
                    remoteException.printStackTrace();
                }

                //Generating a new key
                byte[] keyToByteArray = sendingKey.getEncoded();
                byte[] newKey = kdf.hkdfExpand(keyToByteArray, tempValueToBytes, 128);
                sendingKey = new SecretKeySpec(newKey, 0, 16, "AES");
                textField.setText("");
            }
        });
        frame.pack();
        frame.setVisible(true);

    }

    public void setInitialValues(){
        System.out.print("Enter the startindex for sending: ");
        currentIdx = Integer.parseInt(scanner.nextLine());
        System.out.println("Enter the starttag for sending: ");
        currentTag = scanner.nextLine();
        sendingKey= new SecretKey(){
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
        System.out.println("Sending key: "+sendingKey);
    }

    public static void main(String[] args) throws IOException, NotBoundException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        //init frame
        MainClient client = new MainClient();

    }

    public static String randomString(){
        String random = UUID.randomUUID().toString().replace("-","").substring(0,8);
        return random;
    }

    public static int randomInteger(){
        double randNumber = Math.random();
        double d = randNumber * 19;
        return (int)d;
    }
}

