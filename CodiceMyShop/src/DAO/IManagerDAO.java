package DAO;

import Model.Store;
import Model.User;

import java.io.File;
import java.util.ArrayList;


public interface IManagerDAO {
    int changeQuantity(int itemID, int productQuantity);
    int disableByUsername(String username);
    boolean isDisabled(String username);
    int activateByUsername(String username);
    Store getManagerStore(String manager);
    ArrayList<User> getAllUsersByStore(String storeCity);

    void removeManager(String selectedUsername);
}
