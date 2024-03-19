package model;

public class InterCar extends Car{

    public static int qnt;

    public InterCar(String model, String renavam, Integer year, Double price, String category) {
        super(model, renavam, year, price, category);
    }

    public static int getQnt() {
        return qnt;
    }

    public static void setQnt(int qnt) {
        InterCar.qnt = qnt;
    }
}
