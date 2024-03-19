package model;

public class EcoCar extends Car{
    public static int qnt; 

    public EcoCar(String model, String renavam, Integer year, Double price, String category) {
        super(model, renavam, year, price, category);
    }

    public static int getQnt() {
        return qnt;
    }

    public static void setQnt(int qnt) {
        EcoCar.qnt = qnt;
    }
}
