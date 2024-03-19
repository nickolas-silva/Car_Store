package server.auth;

import java.rmi.Remote;
import java.rmi.RemoteException;

import model.User;

public interface AuthInterface extends Remote {
    
    User loginUser(String cpf, String password) throws RemoteException;
    void registerUser(User u) throws RemoteException;
}
