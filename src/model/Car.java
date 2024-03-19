package model;

import java.io.Serializable;

public class Car implements Serializable{

    private String model;
    private String renavam;
    private Integer year;
    private Double price;

    public Car(String model, String renavam, Integer year, Double price) {
        this.model = model;
        this.renavam = renavam;
        this.year = year;
        this.price = price;
        
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getRenavam() {
        return renavam;
    }

    public void setRenavam(String renavam) {
        this.renavam = renavam;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void update(Car car){
        this.model = car.getModel();
        this.renavam = car.getRenavam();
        this.year = car.getYear();
        this.price = car.getPrice();
    }
}
