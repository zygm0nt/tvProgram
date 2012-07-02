package org.ftang.model;

/**
 * User: marcin
 */
public class Program {
    
    private int number;
    private String image;
    private String name;

    public Program(int number, String image, String name) {
        this.number = number;
        this.image = image;
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }
}
