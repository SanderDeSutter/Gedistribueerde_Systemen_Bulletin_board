package Server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class MainServer {

    private void startServer(){
// create on port 1099
// create a new service named CounterService
        try {
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("BulletinBoardService", new BulletinBoardImpl());
        }catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("system is ready");
    }

    public static void main(String[] args) {
        MainServer main = new MainServer();
        main.startServer(); }
}
