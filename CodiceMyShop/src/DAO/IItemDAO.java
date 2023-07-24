package DAO;

import Model.Item;
import Model.ItemVendor;
import Model.Vendor;

import java.util.ArrayList;

public interface IItemDAO {
    int createItem(Item item);
    boolean itemExist(int id);
    Item getItem(int id);
    ArrayList<Item> getAllItemsByStore(String storeCity); //se passato null, verranno visualizzati tutti i prodotti nel db

    int addItemVendor(ItemVendor itemVendor);
    boolean itemVendorExist(String name);
    ItemVendor getItemVendor (String name);
    ArrayList<Vendor> getAllItemVendors();

    ItemVendor getItemVendor(int itemID);

    int modifyItem(Item item);

    void deleteItemInfo(int itemID);

    void deleteItem(int itemID);

    void removeItemVendor(String itemVendorName);
}
