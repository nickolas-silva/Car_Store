package server.auth;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map.Entry;

import model.User;

public class AuthServer implements AuthInterface{
    private static String path = "src/server/auth/users.txt";
    private static HashMap<String, User> users;
    private static ObjectInputStream input;
    private static ObjectOutputStream output;

    public AuthServer(){
        try{
            input = new ObjectInputStream(new FileInputStream(path));
        } catch (IOException e) {

            try{
                output = new ObjectOutputStream(new FileOutputStream(path));
                input = new ObjectInputStream(new FileInputStream(path));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        users = getUsers();
    }

    private void updateServer(){
        try{
            output = new ObjectOutputStream(new FileOutputStream(path));

            for (Entry<String, User> user : users.entrySet()) {
                output.writeObject(user.getValue());
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void registerUser(User u){
        users = getUsers();
        users.put(u.getCpf(), u);
        updateServer();

        System.out.println("The User was registered successfully");
    }
    
    public User loginUser(String cpf, String password){
        for(Entry<String, User> user : users.entrySet()){
            if(cpf.equals(user.getKey()) && password.equals(user.getValue().getPassword())){
                System.out.println("The User was logged in successfully");
                System.out.println("Welcome " + user.getValue().getName());

                return user.getValue();
            }
        }
        return null;
    }

    private static HashMap<String, User> getUsers() {
        boolean flag = false;
        if (users == null) {
            users = new HashMap<String, User>();
        }

        try{
            while (!flag) {
                User u = (User) input.readObject();
                users.put(u.getCpf(), u);
            }
        } catch (IOException e) {
            flag = true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return users;
    }

    public static void main(String[] args) {
        AuthServer auth = new AuthServer();

        try{
            AuthInterface stub = (AuthInterface) UnicastRemoteObject.exportObject(auth, 0);
            LocateRegistry.createRegistry(3031);
            Registry registry = LocateRegistry.getRegistry(3031);
            registry.bind("Authentication", stub);

            System.out.println("Auth Server is running...");
        } catch (RemoteException | AlreadyBoundException e){
            e.printStackTrace();
        }
    }

}


