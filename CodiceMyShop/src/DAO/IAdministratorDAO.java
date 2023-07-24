package DAO;

import Model.*;

import java.util.ArrayList;


public interface IAdministratorDAO {
    int addAdmin(Administrator administrator);
    Administrator getAdminByUsername(String username);

    int addManager(Manager manager);
    Manager getManagerByUsername(String username);
    ArrayList<User> getAllManagers();

    int addNewStore(Store store);
    int removeStore(String city);
    boolean storeExist(String city);
    ArrayList<Store> getAllStores();

    int removeAdmin(String selectedUsername);

}
