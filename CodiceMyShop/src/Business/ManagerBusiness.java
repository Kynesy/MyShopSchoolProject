package Business;

import DAO.ManagerDAO;
import DAO.UserDAO;
import Model.Manager;
import Model.Store;
import Model.UploadResponse;
import Model.User;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;

public class ManagerBusiness {
    private static ManagerBusiness instance = new ManagerBusiness();
    private ManagerDAO managerDAO;
    private UserDAO userDAO;
    private ManagerBusiness(){
        managerDAO = ManagerDAO.getInstance();
        userDAO = UserDAO.getInstance();
    }

    public static ManagerBusiness getInstance(){
        return instance;
    }

    public UploadResponse sendMail(String message, String subject, File file, ArrayList<User> users) {
        UploadResponse uploadResponse = new UploadResponse();
        uploadResponse.setMessage("Mail inviate con successo.");
        uploadResponse.setDone(true);
        MailUtils mailUtils = MailUtils.getInstance();
        for(User u : users){
            int i =mailUtils.send(u.getEmail(), subject, message, file);
            if(i==1){
                uploadResponse.setMessage("C'è stato un errore nell'invio di alcune mail.");
                System.out.println("Errore invio mail per l'utente: " + u);
                uploadResponse.setDone(false);
            }
            if(i==2){
                uploadResponse.setDone(true);
                uploadResponse.setMessage("Mail inviate ma il file dell'ordine non è stato rimosso correttamente.");
                System.out.println("File ["+ file.getAbsolutePath() +"] relativo all'utente " + u + " non è stato rimosso correttamente.");
            }
        }
        return uploadResponse;
    }

    public UploadResponse sendMail(String message, String subject, ArrayList<User> users) {
        UploadResponse uploadResponse = new UploadResponse();
        uploadResponse.setMessage("Mail inviate con successo.");
        uploadResponse.setDone(true);
        MailUtils mailUtils = MailUtils.getInstance();
        for(User u : users){
            int i = mailUtils.send(u.getEmail(), subject, message);
            if(i==1){
                uploadResponse.setMessage("C'è stato un errore nell'invio di alcune mail.");
                System.out.println("Errore invio mail per l'utente: " + u);
            }
        }
        return uploadResponse;
    }

    public UploadResponse sendMail(String message, String subject, File file, String email) {
        UploadResponse uploadResponse = new UploadResponse();
        uploadResponse.setMessage("Mail inviata con successo.");
        uploadResponse.setDone(true);
        MailUtils mailUtils = MailUtils.getInstance();
        int i = mailUtils.send(email, subject, message, file);
        if(i==1){
            uploadResponse.setMessage("C'è stato un errore nell'invio della mail.");
            System.out.println("Errore invio mail: " + email);
            uploadResponse.setDone(false);
        }
        if(i==2){
            uploadResponse.setDone(true);
            uploadResponse.setMessage("Mail inviata ma il file dell'ordine non è stato rimosso correttamente.");
            System.out.println("File ["+ file.getAbsolutePath() +"] relativo all'email' " + email + " non è stato rimosso correttamente.");
        }
        return uploadResponse;
    }

    public UploadResponse sendMail(String message, String subject, String email) {
        UploadResponse uploadResponse = new UploadResponse();
        uploadResponse.setMessage("Mail inviata con successo.");
        uploadResponse.setDone(true);
        MailUtils mailUtils = MailUtils.getInstance();
        int i = mailUtils.send(email, subject, message);
        if(i==1){
            uploadResponse.setMessage("C'è stato un errore nell'invio della mail.");
            System.out.println("Errore invio mail: " + email);
        }
        return uploadResponse;
    }

    public UploadResponse disableUser(String username){
        UploadResponse uploadResponse = new UploadResponse();
        uploadResponse.setMessage("L'utente ["+ username +"] è stato disabilitato con successo.");

        if(username == null){
            uploadResponse.setMessage("Nessun utente valido selezionato");
            return uploadResponse;
        }

        if(managerDAO.isDisabled(username)){
            uploadResponse.setMessage("L'utente ["+ username +"] è  già disabilitato.");
            return uploadResponse;
        }

        int row = managerDAO.disableByUsername(username);
        if(row !=1){
            uploadResponse.setMessage("Utente non disabilitato. Errore sconosciuto.");
            return uploadResponse;
        }
        return uploadResponse;
    }

    public ArrayList<User> getUsersByManagerUsername(String managerUsername){
        Store store = managerDAO.getManagerStore(managerUsername);
        return managerDAO.getAllUsersByStore(store.getCity());
    }

    public UploadResponse activateAccount(String username){
        UploadResponse uploadResponse = new UploadResponse();
        uploadResponse.setMessage("L'utente ["+ username +"] è stato riabilitato con successo.");

        if(username==null){
            uploadResponse.setMessage("L'utente selezionato non è valido.");
            return uploadResponse;
        }
        if(!managerDAO.isDisabled(username)){
            uploadResponse.setMessage("L'utente ["+ username +"] è già abilitato.");
            return uploadResponse;
        }

        int row = managerDAO.activateByUsername(username);
        if(row!=1){
            uploadResponse.setMessage("L'utente non è stato riabilitato: errore sconosciuto");
            return uploadResponse;
        }
        return uploadResponse;
    }

    public UploadResponse updateItemAvailability(int itemID, int quantity){
        UploadResponse uploadResponse = new UploadResponse();
        uploadResponse.setMessage("Quantità aggiornata con successo.");
        uploadResponse.setDone(true);

        if(itemID == -1) {
            uploadResponse.setMessage("Nessun prodotto valido selezionato.");
            uploadResponse.setDone(true);
            return uploadResponse;
        }

        if(quantity<0){
            uploadResponse.setMessage("La quantità deve essere positiva.");
            uploadResponse.setDone(false);
            return uploadResponse;
        }

        int row = managerDAO.changeQuantity(itemID, quantity);
        if(row != 1){
            uploadResponse.setDone(true);
            uploadResponse.setMessage("Errore sconosciuto.");
            return uploadResponse;
        }
        return uploadResponse;
    }

    public ArrayList<User> getAllUsersByStore(String storeCity) {
        return managerDAO.getAllUsersByStore(storeCity);
    }

    public UploadResponse removeManager(String selectedUsername) {
        UploadResponse uploadResponse = new UploadResponse();
        uploadResponse.setMessage("Eliminazione manager ["+selectedUsername+"] avvenuta con successo.");

        managerDAO.removeManager(selectedUsername);
        managerDAO.activateByUsername(selectedUsername);
        int row = userDAO.removeByUsername(selectedUsername);
        if(row!=1){
            uploadResponse.setMessage("Errore: eliminazione profilo interrotta.");
            return uploadResponse;
        }
        return uploadResponse;
    }

    public UploadResponse updateManager(Manager manager) {
        UploadResponse uploadResponse = new UploadResponse();
        uploadResponse.setMessage("Manager aggiornato con successo.");
        uploadResponse.setDone(true);

        if(manager.getName().isBlank() || manager.getSurname().isBlank() || manager.getEmail().isBlank()) {
            uploadResponse.setMessage("Dati non validi");
            uploadResponse.setDone(false);
        }

        int row = userDAO.updateUserData(manager);
        if(row != 1){
            uploadResponse.setMessage("Errore nel db");
            uploadResponse.setDone(false);
            return uploadResponse;
        }
        return uploadResponse;
    }
}
