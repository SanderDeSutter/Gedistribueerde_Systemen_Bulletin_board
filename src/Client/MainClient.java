package Client;

import Common.BulletinBoard;
import Common.Value;
import Common.ValueTagPair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Random;
import java.util.Scanner;
import java.util.UUID;


public class MainClient {

    public static void main(String[] args) throws IOException, NotBoundException {
        Scanner scanner = new Scanner(System.in);
        int currentIdx;
        String currentTag;
        System.out.print("Enter the startindex for sending: ");
        currentIdx = Integer.parseInt(scanner.nextLine());
        System.out.println("Enter the starttag for sending: ");
        currentTag = scanner.nextLine();


        System.out.println("We maken verbinding met de server");

        // fire to localhost port 1099
        Registry myRegistry = LocateRegistry.getRegistry("localhost", 1099);

        // search for CounterService
        BulletinBoard impl = (BulletinBoard) myRegistry.lookup("BulletinBoardService");

        //impl.sendMessage(5,new ValueTagPair(new Value(),"test"));
        //uncomment voor client1
        //new ClientThread(impl,1 ,"b" ).start();

        //uncomment voor client2
        new ClientThread(impl,0,"a").start();

        boolean continueSending = true;

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
                impl.sendMessage(tempCurrentIndex, new ValueTagPair(value, tempCurrentTag));
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

