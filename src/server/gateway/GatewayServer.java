package server.gateway;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import model.Car;
import model.User;
import server.auth.AuthInterface;
import server.database.DbInterface;

public class GatewayServer implements GatewayInterface{
    
    static DbInterface dbServer;
    static AuthInterface authServer;
    public static Registry registry;

    @Override
    public void registerUser(User user){
        try{
            authServer.registerUser(user);
        } catch (RemoteException e){
            e.printStackTrace();
        }
    }

    @Override
    public User loginUser(String cpf, String password){
        try{
            return authServer.loginUser(cpf, password);
        } catch (RemoteException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void addCar(Car newCar) throws RemoteException{
        dbServer.addCar(newCar);
        System.out.println("Car added successfully!");
    }

    @Override
    public void editCar(String renavam, Car editCar) throws RemoteException{
        dbServer.editCar(renavam, editCar);
        System.out.println("Car edited successfully!");
    }

    @Override
    public void deleteCar(String renavam) throws RemoteException{
        dbServer.deleteCar(renavam);
        System.out.println("Car deleted successfully!");
    }

    @Override
    public List<Car> listCars() throws RemoteException{
        System.out.println("Cars listed successfully!");
        return dbServer.listCars();
    }

    @Override
    public List<Car> listCarsbyCategory(String category) throws RemoteException{
        System.out.println("Cars listed successfully!");
        return dbServer.listCarsbyCategory(category);
    }

    @Override
    public List<Car> getCarbyName(String name) throws RemoteException{
        System.out.println("Cars listed successfully!");
        return dbServer.getCarbyName(name);
    }

    @Override
    public Car getCar(String renavam) throws RemoteException{
        System.out.println("Car found successfully!");
        return dbServer.getCar(renavam);
    }

    @Override
    public Car buyCar(String renavam) throws RemoteException{
        System.out.println("Car with renavam: " + renavam + " bought successfully!");
        return dbServer.buyCar(renavam);
    }

    @Override
    public int getQnt(String category) throws RemoteException{
        return dbServer.getQnt(category);
    }


    public static void main(String[] args) {
        GatewayServer gateway = new GatewayServer();
        try{
            //AUTHENTICATION
            Registry auth = LocateRegistry.getRegistry("127.0.0.1", 3031);
            authServer = (AuthInterface) auth.lookup("localhost");

            //DATABASE
            Registry db = LocateRegistry.getRegistry("127.0.0.2", 3032);
            dbServer = (DbInterface) db.lookup("database");

            //GATEWAY
            GatewayInterface p = (GatewayInterface) UnicastRemoteObject.exportObject(gateway, 0);

            LocateRegistry.createRegistry(3030);
            registry = LocateRegistry.getRegistry(3030);
            registry.bind("Gateway", p);

            System.out.println("GatewayServer is running...");

        } catch (RemoteException | AlreadyBoundException | NotBoundException e){
            e.printStackTrace();
        }
    }
}
