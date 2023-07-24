package Model;

import java.io.File;
import java.util.ArrayList;

public class Item extends Article implements ItemInterface{
    private ItemVendor itemVendor;
    private String cityStore;
    private int avaiability;
    private int line;
    private int shelf;
    private int wishlistQuantity;

    private ArrayList<File> fotos;

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public int getID() {
        return getId();
    }

    public ItemVendor getItemVendor() {
        return itemVendor;
    }

    public int getWishlistQuantity() {
        return wishlistQuantity;
    }

    public void setWishlistQuantity(int wishlistQuantity) {
        this.wishlistQuantity = wishlistQuantity;
    }

    public void setItemVendor(ItemVendor itemVendor) {
        this.itemVendor = itemVendor;
    }

    public String getCityStore() {
        return cityStore;
    }

    public void setCityStore(String cityStore) {
        this.cityStore = cityStore;
    }

    public int getLine() {
        return line;
    }

    public int getShelf() {
        return shelf;
    }

    public int getAvaiability() {
        return avaiability;
    }

    public void setAvaiability(int avaiability) {
        this.avaiability = avaiability;
    }

    public void setPosition(int line, int shelf) {
        this.line = line;
        this.shelf = shelf;
    }

    @Override
    public ArrayList<File> getFotos() {
        return fotos;
    }

    @Override
    public void setFotos(ArrayList<File> fotos) {
        this.fotos = fotos;
    }

    @Override
    public String toString() {
        return  "[ID:"+getId()+ " - Nome:" + getName() + " - Prezzo:" + getPrice() + "€]";
    }


}
