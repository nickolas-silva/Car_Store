package model;

import java.io.Serializable;

public class CarCategory implements Serializable{
    private String name;

    public CarCategory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
