package Model;

import java.util.ArrayList;

public class Category {
    private int id;
    private String name;
    private ArrayList<Category> sottocategorie;
    private ArrayList<Category> categoryTree;
    private int parentId;

    public Category(){
        sottocategorie = new ArrayList<>();
    }

    public ArrayList<Category> getSottocategorie() {
        return sottocategorie;
    }

    public int getParentId() {
        return parentId;
    }

    public ArrayList<Category> getCategoryTree() {
        return categoryTree;
    }

    public void setCategoryTree(ArrayList<Category> categoryTree) {
        this.categoryTree = categoryTree;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSottocategorie(ArrayList<Category> sottocategorie) {
        this.sottocategorie = sottocategorie;
    }



    @Override
    public String toString() {
        return getName();
    }
}
