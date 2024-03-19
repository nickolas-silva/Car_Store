package server.database;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.channels.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import model.Car;
import model.EcoCar;
import model.ExeCar;
import model.InterCar;

public class DbServer implements DbInterface{
    
    private String path = "src/server/database/garage.txt";
    private static HashMap<String, Car> mapCars;
    private static ObjectInputStream input;
    private static ObjectOutputStream output;
    private static int qntEco, qntExe, qntInter;

    public DbServer(){
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

        mapCars = getCars();
    }

    private static HashMap<String, Car> getCars(){
        boolean eof = false;

        if (mapCars == null) {
            mapCars = new HashMap<String, Car>();
        }

        try {
            while (!eof) {
                Car registredCar = (Car) input.readObject();
                mapCars.put(registredCar.getRenavam(), registredCar);
            }

        } catch (IOException e) {
            eof = true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return mapCars;
    }

    @Override
    public Car buyCar(String renavam){
        Car car = mapCars.get(renavam);
        System.out.println("The car " + car.getModel() + " was bought successfully");
        mapCars.remove(renavam);
        return car;
    }

    private void updateServer(){

        try{
            output = new ObjectOutputStream(new FileOutputStream(path));
            qntEco = 0;
            qntExe = 0;
            qntInter = 0;

            for (Entry<String, Car> car : mapCars.entrySet()){
                output.writeObject(car.getValue());
                
                if(car.getValue().getCategory().equals("Economic")){
                    qntEco++;
                    break;
                } else if (car.getValue().getCategory().equals("Executive")){
                    qntExe++;
                    break;
                } else if (car.getValue().getCategory().equals("Intermediary")){
                    qntInter++;
                    break;
                }

            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public int getQnt(String category) throws RemoteException{
        mapCars = getCars();
        updateServer();

        if(category.equals("Economic")){
            EcoCar.setQnt(qntEco);
            return qntEco;
        } else if (category.equals("Executive")){
            ExeCar.setQnt(qntExe);
            return qntExe;
        } else if (category.equals("Intermediary")){
            InterCar.setQnt(qntInter);
            return qntInter;
        } else {
            EcoCar.setQnt(qntEco);
            ExeCar.setQnt(qntExe);
            InterCar.setQnt(qntInter);
            return qntEco + qntExe + qntInter;
        }
    }

    @Override
    public void addCar(Car addCar){
        mapCars = getCars();
        mapCars.put(addCar.getRenavam(), addCar);
        updateServer();
        System.out.println("The car " + addCar.getModel() + " was added successfully");
    }

    @Override
    public void editCar(String renavam, Car editCar){
        mapCars = getCars();
        Car car = getCar(renavam);

        if(car.getModel() != null){
            car.setModel(editCar.getModel());
        }
        if(editCar.getCategory().equals("Economic")){
            qntEco--;
        } else if (editCar.getCategory().equals("Executive")){
            qntExe--;
        } else if (editCar.getCategory().equals("Intermediary")){
            qntInter--;
        }

        if(car.getCategory().equals("Economic")){
            qntEco++;
        } else if (car.getCategory().equals("Executive")){
            qntExe++;
        } else if (car.getCategory().equals("Intermediary")){
            qntInter++;
        }
        car.setCategory(editCar.getCategory());
        car.setPrice(editCar.getPrice());
        car.setYear(editCar.getYear());

        System.out.println("The car with renavam: " + car.getRenavam() + " was edited successfully");

        updateServer();
    }

    @Override
    public void deleteCar(String renavam){
        mapCars = getCars();
        Car deleteCar = getCar(renavam);

        if(deleteCar != null){
            mapCars.remove(renavam, deleteCar);
            System.out.println("The car with renavam: " + deleteCar.getRenavam() + " was deleted successfully");
        } else {
            System.out.println("The car with renavam: " + renavam + " was not found");
        }
        updateServer();
    }

    @Override
    public Car getCar(String renavam){
        mapCars = getCars();
        
        Car searched = null;

        for(Entry<String, Car> car : mapCars.entrySet()){
            if(renavam.equals(car.getKey()) && renavam.equals(car.getValue().getRenavam())){
                System.out.println("The car with renavam: " + car.getValue().getRenavam() + " was found");
                searched = car.getValue();
                break;
            }
        }
        return searched;
    }

    @Override
    public List<Car> getCarbyName(String name){
        mapCars = getCars();
        List<Car> searched = new ArrayList<Car>();

        for(Entry<String, Car> car : mapCars.entrySet()){
            if(name.equalsIgnoreCase(car.getValue().getModel())){
                System.out.println("The car with renavam: " + car.getValue().getModel() + " - " + car.getValue().getModel() + " was found");
                searched.add(car.getValue());
            }
        }
        return searched;    
    }

    public static void main(String[] args) {
        DbServer db = new DbServer();

        try{
            DbInterface server = (DbInterface) UnicastRemoteObject.exportObject(db, 0);
            LocateRegistry.createRegistry(3032);
            Registry registry = LocateRegistry.getRegistry("127.0.0.2", 3032);
            registry.rebind("Database", server);

            System.out.println("The Database Server is running");
        } catch (RemoteException | AlreadyBoundException e){
            e.printStackTrace();
        }
    }

    @Override
    public List<Car> listCars() {
        mapCars = getCars();
        List<Car> cars = new ArrayList<Car>();

        for(Entry<String, Car> car : mapCars.entrySet()){
            cars.add(car.getValue());
        }

        Comparator<Car> comparator = Comparator.comparing(Car::getModel);
        Collections.sort(cars, comparator);

        return cars;
    }

    @Override
    public List<Car> listCarsbyCategory(String category){
        mapCars = getCars();
        List<Car> cars = new ArrayList<Car>();

        for(Entry<String, Car> car : mapCars.entrySet()){
            if(category.equals(car.getValue().getCategory())){
                cars.add(car.getValue());
            }
        }

        Comparator<Car> comparator = Comparator.comparing(Car::getModel);
        Collections.sort(cars, comparator);

        return cars;
    }

}
