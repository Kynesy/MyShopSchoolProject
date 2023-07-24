package Model;

import java.util.ArrayList;

public class WishList implements Comparable{
    private String name;
    private String username;
    private int id;
    private ArrayList<Item> items;
    private ArrayList<Service> services;

    public WishList() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public ArrayList<Service> getServices() {
        return services;
    }

    public void setServices(ArrayList<Service> services) {
        this.services = services;
    }

    @Override
    public String toString() {
        return getName();
    }

    public int getNumerOfArticles() {
        return services.size() + items.size();
    }

    @Override
    public int compareTo(Object o) {
        WishList otherList = (WishList) o;
        return getName().compareTo(otherList.getName());
    }


}
