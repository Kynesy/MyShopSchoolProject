package Model;

import java.io.File;
import java.util.ArrayList;

public class
Article {
    private int id;
    private String name;
    private String description;
    private ArrayList<File> fotos;
    protected double price;
    private Category category;

    public Article() {
        fotos = new ArrayList<>();
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public ArrayList<File> getFotos() {
        return fotos;
    }

    public void setFotos(ArrayList<File> newFotos) {
        fotos.clear();
        for(File f : newFotos){
            fotos.add(f);
        };
    }
}
