import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import model.Car;
import model.Client;
import model.EcoCar;
import model.Employee;
import model.ExeCar;
import model.InterCar;
import model.User;
import server.gateway.GatewayInterface;

public class App {

    private static boolean connection = false;
    private static Scanner in = new Scanner(System.in);
    private static GatewayInterface gateway;
    private static List<Car> listCars = new ArrayList<Car>();
    private static User user;
    //private static boolean isClient = false;

    public static void main(String[] args) throws Exception {
        
    try{
        Registry authR = LocateRegistry.getRegistry(3030);
        gateway = (GatewayInterface) authR.lookup("gateway");
        
        addDefaultCars();
        connection = true;
        while (connection) {
            System.out.println("""
            AUTHENTICATION
            ----------------------------
            1 - LOGIN
            2 - REGISTER
            ----------------------------
            """);
            int option = in.nextInt();
            in.nextLine();
            new ProcessBuilder("clear").inheritIO().start().waitFor();

            if(option == 1){
                
                user = login();
                if(user != null){
                    connection = false;
                    break;
                }
                System.out.println("Invalid user");
                break;
            } else if(option == 2){
                register();
                break;
            } else {
                System.out.println("Invalid option");
            }
        }

        while (!connection) {
            
            if(user.isEmployee){
            System.out.println("""
                CAR STORE
                ----------------------------
                1 - ADD CAR
                2 - EDIT CAR
                3 - DELETE CAR
                4 - LIST CARS
                5 - SEARCH CAR
                6 - BUY CAR
                7 - AMOUNT OF CARS
                8 - LOGOUT
                ----------------------------
            """);
            int option = in.nextInt();
            in.nextLine();
            new ProcessBuilder("clear").inheritIO().start().waitFor();

            switch (option) {
                case 1:
                    addCar();
                    break;

                case 2:
                    editCar();
                    break;

                case 3:
                    deleteCar();
                    break;

                case 4:
                    System.out.println("LIST OF CARS");
                    System.out.println("-------------------------------------------------");
                    System.out.println("""
                            1 - Economic
                            2 - Executive
                            3 - Intermediary
                            4 - All
                            """);
                    int op = in.nextInt();
                    in.nextLine();
                    List<Car> list = new ArrayList<Car>();

                    if (op == 1) {
                        list = gateway.listCarsbyCategory("Economic");
                    } else if (op == 2) {
                        list = gateway.listCarsbyCategory("Executive");
                    } else if (op == 3) {
                        list = gateway.listCarsbyCategory("Intermediary");
                    } else if (op == 4) {
                        list = gateway.listCars();
                    } else {
                        System.out.println("Invalid option");    
                    }

                    for (Car car : list) {
                        System.out.println("-------------------------------------------------");
                        System.out.println("Model: " + car.getModel());
                        System.out.println("Renavam: " + car.getRenavam());
                        System.out.println("Year: " + car.getYear());
                        System.out.println("Price: " + car.getPrice());
                        System.out.println("Category: " + car.getCategory());
                        System.out.println("-------------------------------------------------");
                    }
                    break;

                case 5:
                    System.out.println("""
                        SEARCH CAR
                        ----------------------------
                        1 - By name
                        2 - By renavam
                    """);
                    int op2 = in.nextInt();
                    in.nextLine();
                    if (op2 == 1) {
                        System.out.println("Enter the name: ");
                        String name = in.nextLine();
                        List<Car> list2 = gateway.getCarbyName(name);
                        for (Car car : list2) {
                            System.out.println("Model: " + car.getModel());
                            System.out.println("Renavam: " + car.getRenavam());
                            System.out.println("Year: " + car.getYear());
                            System.out.println("Price: " + car.getPrice());
                            System.out.println("Category: " + car.getCategory());
                            System.out.println("-------------------------------------------------");
                        }
                    } else if (op2 == 2) {
                        System.out.println("Enter the renavam: ");
                        String renavam = in.nextLine();
                        Car car = gateway.getCar(renavam);
                        if (car != null) {
                            System.out.println("Model: " + car.getModel());
                            System.out.println("Year: " + car.getYear());
                            System.out.println("Price: " + car.getPrice());
                            System.out.println("Category: " + car.getCategory());
                            System.out.println("-------------------------------------------------");
                        } else {
                            System.out.println("Car not found");
                        }
                    } else {
                        System.out.println("Invalid option");
                    }
                    break;

                case 6:
                    buyCar();
                    break;

                case 7:
                    System.out.println("AMOUNT OF CARS");
                    System.out.println("-------------------------------------------------");
                    System.out.println("1 - Economic");
                    System.out.println("2 - Executive");
                    System.out.println("3 - Intermediary");
                    System.out.println("4 - All");
                    int op3 = in.nextInt();
                    in.nextLine();

                    if (op3 == 1) {
                        System.out.println("Economic: " + gateway.getQnt("Economic"));
                    } else if (op3 == 2) {
                        System.out.println("Executive: " + gateway.getQnt("Executive"));
                    } else if (op3 == 3) {
                        System.out.println("Intermediary: " + gateway.getQnt("Intermediary"));
                    } else if (op3 == 4) {
                        System.out.println("Economic: " + gateway.getQnt("Economic"));
                        System.out.println("Executive: " + gateway.getQnt("Executive"));
                        System.out.println("Intermediary: " + gateway.getQnt("Intermediary"));
                    } else {
                        System.out.println("Invalid option");
                    }
                    break;
                
                case 8:
                System.out.println("LOGOUT");
                connection = true;
                break;
            
                default:
                    break;
            }
        } else{
            System.out.println("""
                CAR STORE
                ----------------------------
                1 - LIST CARS
                2 - SEARCH CAR
                3 - BUY CAR
                4 - AMOUNT OF CARS
                5 - LOGOUT
                ----------------------------
            """);
            int option = in.nextInt();
            in.nextLine();
            new ProcessBuilder("clear").inheritIO().start().waitFor();

            switch (option) {
                case 1:
                    System.out.println("LIST OF CARS");
                    System.out.println("-------------------------------------------------");
                    System.out.println("""
                            1 - Economic
                            2 - Executive
                            3 - Intermediary
                            4 - All
                            """);
                    int op = in.nextInt();
                    in.nextLine();
                    List<Car> list = new ArrayList<Car>();

                    if (op == 1) {
                        list = gateway.listCarsbyCategory("Economic");
                    } else if (op == 2) {
                        list = gateway.listCarsbyCategory("Executive");
                    } else if (op == 3) {
                        list = gateway.listCarsbyCategory("Intermediary");
                    } else if (op == 4) {
                        list = gateway.listCars();
                    } else {
                        System.out.println("Invalid option");    
                    }

                    for (Car car : list) {
                        System.out.println("-------------------------------------------------");
                        System.out.println("Model: " + car.getModel());
                        System.out.println("Renavam: " + car.getRenavam());
                        System.out.println("Year: " + car.getYear());
                        System.out.println("Price: " + car.getPrice());
                        System.out.println("Category: " + car.getCategory());
                        System.out.println("-------------------------------------------------");
                    }
                    break;

                case 2:
                    System.out.println("""
                        SEARCH CAR
                        ----------------------------
                        1 - By name
                        2 - By renavam
                    """);
                    int op2 = in.nextInt();
                    in.nextLine();
                    if (op2 == 1) {
                        System.out.println("Enter the name: ");
                        String name = in.nextLine();
                        List<Car> list2 = gateway.getCarbyName(name);
                        for (Car car : list2) {
                            System.out.println("Model: " + car.getModel());
                            System.out.println("Renavam: " + car.getRenavam());
                            System.out.println("Year: " + car.getYear());
                            System.out.println("Price: " + car.getPrice());
                            System.out.println("Category: " + car.getCategory());
                            System.out.println("-------------------------------------------------");
                        }
                    } else if (op2 == 2) {
                        System.out.println("Enter the renavam: ");
                        String renavam = in.nextLine();
                        Car car = gateway.getCar(renavam);
                        if (car != null) {
                            System.out.println("Model: " + car.getModel());
                            System.out.println("Year: " + car.getYear());
                            System.out.println("Price: " + car.getPrice());
                            System.out.println("Category: " + car.getCategory());
                            System.out.println("-------------------------------------------------");
                        } else {
                            System.out.println("Car not found");
                        }
                    }
                    break;

                case 3:
                    buyCar();
                    break;
                
                case 4:
                    System.out.println("AMOUNT OF CARS");
                    System.out.println("-------------------------------------------------");
                    System.out.println("1 - Economic");
                    System.out.println("2 - Executive");
                    System.out.println("3 - Intermediary");
                    System.out.println("4 - All");
                    int op3 = in.nextInt();
                    in.nextLine();

                    if (op3 == 1) {
                        System.out.println("Economic: " + gateway.getQnt("Economic"));
                    } else if (op3 == 2) {
                        System.out.println("Executive: " + gateway.getQnt("Executive"));
                    } else if (op3 == 3) {
                        System.out.println("Intermediary: " + gateway.getQnt("Intermediary"));
                    } else if (op3 == 4) {
                        System.out.println("Economic: " + gateway.getQnt("Economic"));
                        System.out.println("Executive: " + gateway.getQnt("Executive"));
                        System.out.println("Intermediary: " + gateway.getQnt("Intermediary"));
                    } else {
                        System.out.println("Invalid option");
                    }
                    break;

                case 5:
                    System.out.println("LOGOUT");
                    connection = true;
                    break;

                }
        }

        }

    } catch(RemoteException | NotBoundException e){
            e.printStackTrace();
        }
    }

    private static void register() throws RemoteException{
        System.out.println("""
            REGISTER
            ----------------------------
                """);
        System.out.println("Enter your name: ");
        String name = in.nextLine();
        System.out.println("Enter your CPF: ");
        String cpf = in.nextLine();
        System.out.println("Enter your password: ");
        String password = in.nextLine();

        System.out.println("1 - Client");
        System.out.println("2 - Employee");
        int option = in.nextInt();
        in.nextLine();

        if(option == 1){
            Client newC = new Client(name, cpf, password);
            gateway.registerUser(newC);
            //isClient = true;
            
        } else if(option == 2){
            Employee newE = new Employee(name, cpf, password);
            gateway.registerUser(newE);
            
        } else {
            System.out.println("Invalid option");
        }
    }

    private static User login() throws RemoteException{
        System.out.println("""
            LOGIN
            ----------------------------
            """);
        System.out.println("Enter your CPF: ");
        String cpf = in.nextLine();
        System.out.println("Enter your password: ");
        String password = in.nextLine();

        User u = gateway.loginUser(cpf, password);
        return u;
    }

    private static void addCar() throws RemoteException{
        System.out.println("""
            REGISTER CAR
            ----------------------------
            """);
        System.out.println("Enter the model: ");
        String model = in.nextLine();
        System.out.println("Enter the renavam: ");
        String renavam = in.nextLine();
        System.out.println("Enter the year: ");
        int year = in.nextInt();
        System.out.println("Enter the price");
        double price = in.nextDouble();
        in.nextLine();
        System.out.println("Enter the category: ");
        System.out.println("""
                1 - Economic
                2 - Executive
                3 - Intermediary
                """);
        int option = in.nextInt();
        in.nextLine();

        switch (option) {
            case 1:
                EcoCar eco = new EcoCar(model, renavam, year, price, "Economic");
                gateway.addCar(eco);
                System.out.println("Economic Car added successfully");
                break;
            case 2:
                ExeCar exe = new ExeCar(model, renavam, year, price, "Executive");
                gateway.addCar(exe);
                System.out.println("Executive Car added successfully");
                break;
            case 3:
                InterCar inter = new InterCar(model, renavam, year, price, "Intermediary");
                gateway.addCar(inter);
                System.out.println("Intermediary Car added successfully");
                break;
        
            default:
                System.out.println("Invalid option");
                break;
        }
        
    }

    private static void editCar() throws RemoteException{
        
        System.out.println("Enter the renavam: ");
        String editRenavam = in.nextLine();

        System.out.println("""
                EDIT CAR
                ----------------------------
                """);
        System.out.println("Enter the model: ");
        String model = in.nextLine();
        System.out.println("Enter the year: ");
        int year = in.nextInt();
        System.out.println("Enter the price");
        double price = in.nextDouble();
        in.nextLine();
        System.out.println("Enter the category: ");
        System.out.println("""
                1 - Economic
                2 - Executive
                3 - Intermediary
                """);
        int option = in.nextInt();
        in.nextLine();

        switch (option) {
            case 1:
                Car eco = new EcoCar(model, editRenavam, year, price, "Economic");
                gateway.editCar(editRenavam, eco);
                System.out.println("Economic Car edited successfully");
                break;
            case 2:
                Car exe = new Car(model, editRenavam, year, price, "Executive");
                gateway.editCar(editRenavam, exe);
                System.out.println("Executive Car edited successfully");
                break;
            case 3:
                Car inter = new Car(model, editRenavam, year, price, "Intermediary");
                gateway.editCar(editRenavam, inter);
                System.out.println("Intermediary Car edited successfully");
                break;
        
            default:
                System.out.println("Invalid option");
                break;
        }
    }

    private static void buyCar() throws RemoteException{
        System.out.println("""
            BUY CAR
            -----------------
        """);
        System.out.println("Enter the renavam: ");
        String renavam = in.nextLine();
        Car buy = gateway.getCar(renavam);

        if(buy != null){
            System.out.println("Model: " + buy.getModel());
            System.out.println("Year: " + buy.getYear());
            System.out.println("Price: " + buy.getPrice());
            System.out.println("Category: " + buy.getCategory());
            System.out.println("--------------------------");
            System.out.println("Do you want to buy this car? (y/n)");
            String option = in.nextLine();

            if(option.charAt(0) == 'y' || option.charAt(0) == 'Y'){
                Car bC = gateway.buyCar(renavam);
                if (bC != null) {
                    System.out.println("Car bought successfully");
                    System.out.println("Model: " + bC.getModel());
                    listCars.add(bC);
                } else System.out.println("Buy canceled!");
            } else{
                System.out.println("Buy canceled!!!");
            }
        }
    }

    private static void deleteCar() throws RemoteException{
        System.out.println("""
            DELETE CAR
            -----------------
        """);
        System.out.println("Enter the renavam if you want to delete: ");
        String renavam = in.nextLine();
        gateway.deleteCar(renavam);
        System.out.println("Car with renavam: " + renavam + "deleted successfully");

    }

    private static void addDefaultCars() throws RemoteException{
        gateway.addCar(new EcoCar("Kwid", "20202020", 2021,  (double) 30000, "Economic"));
        gateway.addCar(new EcoCar("Uno", "20202021", 2012,  (double) 15000, "Economic"));
        gateway.addCar(new EcoCar("Corsa", "20202022", 2011,  (double) 45000, "Economic"));
        gateway.addCar(new ExeCar("Creta", "20202023", 2015,  (double) 80000, "Executive"));
        gateway.addCar(new ExeCar("Prisma", "20202024", 2022,  (double) 100000, "Executive"));
        gateway.addCar(new ExeCar("Civic", "20202025", 2023,  (double) 56000, "Executive"));
        gateway.addCar(new InterCar("Duster", "20202026", 2024,  (double) 30000, "Intermediary"));
        gateway.addCar(new InterCar("Onix", "20202027", 2016,  (double) 20000, "Intermediary"));
        gateway.addCar(new InterCar("Gol", "20202028", 2018,  (double) 50000, "Intermediary"));
        gateway.addCar(new EcoCar("Fusca", "20202029", 2019,  (double) 55000, "Economic"));
        gateway.addCar(new ExeCar("Golf GTI", "20202030", 2020,  (double) 150000, "Executive"));
        gateway.addCar(new InterCar("Spin", "20202031", 2014,  (double) 60000, "Intermediary"));
    }

}
