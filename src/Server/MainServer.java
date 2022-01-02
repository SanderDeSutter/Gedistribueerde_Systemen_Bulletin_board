package Server;

import javax.naming.spi.NamingManager;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.spi.LocaleNameProvider;

public class MainServer {
    static Registry registry;
    private void startServer(){
// create on port 1099
// create a new service named CounterService
        try {
            System.setProperty("java.rmi.server.hostname", "78.22.186.196");

            BulletinBoardImpl bulletinBoard = new BulletinBoardImpl();
            //UnicastRemoteObject.exportObject(bulletinBoard, 1100);
            registry = LocateRegistry.createRegistry(1099);
            registry.rebind("//78.22.186.196:1099/BulletinBoardImpl", bulletinBoard);

            Naming.rebind("//192.168.104.108:1099/BulletinBoardImpl", bulletinBoard);

        }catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("system is ready");
    }

    public static void main(String[] args) throws RemoteException, MalformedURLException {
        MainServer main = new MainServer();
        main.startServer(); }
}
