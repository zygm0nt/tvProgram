package org.ftang.model;

/**
 * User: marcin
 */
public class Program implements Comparable<Program> {
    
    private String number;
    private String image;
    private String name;

    public Program(String number, String image, String name) {
        this.number = number;
        this.image = image;
        this.name = name;
    }

    public Program(String line) {
        String[] tokens = line.split(",");
        String image = "placeholder";
        if (tokens.length == 4) 
            image = tokens[3];
        init(padding(tokens[2], 2), tokens[1], image);
    }

    private void init(String number, String name, String image) {
        this.number = number;
        this.image = image;
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    private String padding(String token, int position) {
        return String.format("%0" + position + "d", Integer.parseInt(token));
    }

    @Override
    public int compareTo(Program program) {
        return Integer.parseInt(getNumber()) - Integer.parseInt(program.getNumber());
    }
}
