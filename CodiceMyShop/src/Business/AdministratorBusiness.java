package Business;


import DAO.AdministratorDAO;
import DAO.ManagerDAO;
import DAO.UserDAO;
import Model.*;
import com.mysql.cj.xdevapi.UpdateSpec;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

public class AdministratorBusiness {
    private static AdministratorBusiness instance = new AdministratorBusiness();
    private AdministratorDAO administratorDAO;
    private UserDAO userDAO;
    private AdministratorBusiness(){
        administratorDAO = AdministratorDAO.getInstance();
        userDAO = UserDAO.getInstance();
    }

    public static AdministratorBusiness getInstance() {
        return instance;
    }

    public UploadResponse createStore(Store store){
        UploadResponse uploadResponse = new UploadResponse();
        uploadResponse.setMessage("Punto vendita correttamente creato.");

        if(store.getCity().isBlank()){
           uploadResponse.setMessage("Creazione interrotta: dati inseriti non validi.");
           return uploadResponse;
        }

        if(administratorDAO.storeExist(store.getCity())){
            uploadResponse.setMessage("Creazione interrotta: esiste già un punto vendita con lo stesso nome.");
            return uploadResponse;
        }

        int row = administratorDAO.addNewStore(store);
        if(row!= 1){
            uploadResponse.setMessage("Creazione interrotta. Errore sconosciuto.");
            return uploadResponse;
        }

        return uploadResponse;
    }

    public UploadResponse createManager(Manager manager){
        UploadResponse uploadResponse = new UploadResponse();
        uploadResponse.setMessage("Manager ["+ manager.getUsername() +"] creato con successo.");

        if(manager.getUsername().isBlank() || manager.getPassword().isBlank() || manager.getStoreCity()==null){
            uploadResponse.setMessage("Creazione interrotta: Dati inseriti non validi.");
            return uploadResponse;
        }

        if(userDAO.managerExist(manager.getUsername())){
            uploadResponse.setMessage("Creazione interrotta: Esiste già un manager con quel username.");
            return uploadResponse;
        }

        int row = administratorDAO.addManager(manager);
        if(row!=1){
            uploadResponse.setMessage("Creazione interrotta: Errore sconosciuto.");
            return uploadResponse;
        }

        return uploadResponse;
    }

    public UploadResponse createAdmin(Administrator administrator){
        UploadResponse uploadResponse = new UploadResponse();
        uploadResponse.setMessage("Amministratore ["+ administrator.getUsername() +"] creato con successo.");

        if(administrator.getUsername().isBlank() || administrator.getPassword().isBlank()){
            uploadResponse.setMessage("Creazione interrotta: dati inseriti non validi.");
            return uploadResponse;
        }

        if(userDAO.administratorExist(administrator.getUsername())){
            uploadResponse.setMessage("Creazione interrotta: esiste già un amministratore con lo stesso username.");
            return uploadResponse;
        }

        int row = administratorDAO.addAdmin(administrator);
        if(row!=1){
            uploadResponse.setMessage("Creazione interrotta: errore sconosciuto.");
            return uploadResponse;
        }

        return uploadResponse;
    }

    public ArrayList<User> getAllManagers(){
        ArrayList<User> managers = administratorDAO.getAllManagers();
        for(User user : managers){
            if(ManagerDAO.getInstance().isDisabled(user.getUsername())){
                user.setIsDisabled(1);
            }else {
                user.setIsDisabled(0);
            }
        }
        return managers;
    }

    public Store[] getAllStores() {
        ArrayList<Store> arrayList = administratorDAO.getAllStores();
        arrayList.sort(new Comparator<Store>() {
            @Override
            public int compare(Store o1, Store o2) {
                return o1.getCity().compareTo(o2.getCity());
            }
        });
        return arrayList.toArray(Store[]::new);
    }

    public Manager getManagerByUsername(String username) {
        return administratorDAO.getManagerByUsername(username);
    }

    public UploadResponse removeAdmin(String selectedUsername) {
        UploadResponse uploadResponse = new UploadResponse();
        uploadResponse.setMessage("Amministratore ["+selectedUsername+"] eliminato con successo.");
        AdministratorDAO.getInstance().removeAdmin(selectedUsername);
        int row = UserDAO.getInstance().removeByUsername(selectedUsername);
        if(row!=1){
            uploadResponse.setMessage("Errore: eliminazione account fallita");
            return uploadResponse;
        }
        return uploadResponse;
    }

    public User getAdminByUsername(String username) {
        return administratorDAO.getAdminByUsername(username);
    }

    public UploadResponse updateAdmin(Administrator administrator) {
        UploadResponse uploadResponse = new UploadResponse();
        uploadResponse.setMessage("Admin aggiornato con successo.");
        uploadResponse.setDone(true);

        if(administrator.getName().isBlank() || administrator.getSurname().isBlank() || administrator.getEmail().isBlank()) {
            uploadResponse.setMessage("Dati non validi");
            uploadResponse.setDone(false);
        }

        int row = userDAO.updateUserData(administrator);
        if(row != 1){
            uploadResponse.setMessage("Errore nel db");
            uploadResponse.setDone(false);
            return uploadResponse;
        }
        return uploadResponse;
    }
}
