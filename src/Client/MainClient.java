package Client;

import Common.BulletinBoard;
import Common.Value;
import Common.ValueTagPair;
import Encryption.KDF;
import javafx.scene.layout.Border;
import javafx.scene.layout.Pane;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
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
import java.util.Scanner;
import java.util.UUID;
import java.util.List;


public class MainClient {

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

