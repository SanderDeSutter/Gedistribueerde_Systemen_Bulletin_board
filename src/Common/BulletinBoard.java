package Common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BulletinBoard extends Remote {

    void sendMessage(int idx, ValueTagPair valueTagPair) throws RemoteException;

    Value receive(int idx, String hashTag) throws RemoteException;

    void removeVTP(String tag, int idx) throws RemoteException;
}
