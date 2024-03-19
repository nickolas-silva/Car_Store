package model;

public class Employee extends User{
    
    public Employee(String name, String cpf, String password) {
        super.setCpf(cpf);
        super.setName(name);;
        super.setPassword(password);;
    }
    
    public Car addCar(Car newCar){
        return newCar;
    }

    public Car editCar(Car updatedCar){
        return updatedCar;
    }

    public boolean deleteCar(String renavam){
        return false;

    }
    
}
