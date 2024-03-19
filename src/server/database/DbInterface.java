package server.database;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import model.Car;

public interface DbInterface extends Remote{
    
    public void addCar(Car addCar) throws RemoteException;                          //add car
    public void editCar(String renavam, Car editCar) throws RemoteException;        //edit car
    public List<Car> listCars() throws RemoteException;                             //list all cars
    public List<Car> listCarsbyCategory(String category) throws RemoteException;    //list cars by category
    public List<Car> getCarbyName(String name) throws RemoteException;              //search by name
    public void deleteCar(String renavam) throws RemoteException;                   //delete car
    public Car getCar(String renavam) throws RemoteException;                       //search by renavam
    public Car buyCar(String renavam) throws RemoteException;                       //buy car
    public int getQnt(String category) throws RemoteException;                      //get quantity of cars by category
}
