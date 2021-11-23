package Common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BulletinBoard extends Remote {

    void sendMessage(int idx, ValueTagPair valuetagpair) throws RemoteException;

    Value receive(int idx, String hashTag) throws RemoteException;
}
