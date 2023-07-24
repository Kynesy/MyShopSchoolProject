package Business;

import Business.TableFactory.TableUsersFactory;
import Business.TableFactory.TableUsersType.TableUser;
import DAO.CustomerDAO;
import DAO.ManagerDAO;
import DAO.UserDAO;
import Model.Customer;
import Model.LoginResponse;
import Model.UploadResponse;
import Model.User;

import javax.swing.*;
import java.util.ArrayList;

public class UserBusiness {
    private static UserBusiness instance = new UserBusiness();

    public User getUser(String username) {
        User user = UserDAO.getInstance().getByUsername(username);
        if(ManagerDAO.getInstance().isDisabled(user.getUsername())){
            user.setIsDisabled(1);
        }else {
            user.setIsDisabled(0);
        }
        return user;
    }

    public UploadResponse removeByUsername(String selectedUsername) {
        UploadResponse uploadResponse;
        if(UserDAO.getInstance().managerExist(selectedUsername)){
            uploadResponse = ManagerBusiness.getInstance().removeManager(selectedUsername);
            return uploadResponse;
        }

        if(UserDAO.getInstance().administratorExist(selectedUsername)){
            uploadResponse = AdministratorBusiness.getInstance().removeAdmin(selectedUsername);
            return uploadResponse;
        }

        if(UserDAO.getInstance().userExist(selectedUsername)){
            uploadResponse = CustomerBusiness.getInstance().removeCustomer(selectedUsername);
            return uploadResponse;
        }


        uploadResponse = new UploadResponse();
        uploadResponse.setMessage("Errore identificazione tipo account.");
        return uploadResponse;
    }

    public UploadResponse registrate(Customer customer) {
        UploadResponse uploadResponse = new UploadResponse();
        uploadResponse.setMessage("Registrazione avvenuta con successo!");
        uploadResponse.setDone(true);

        if(customer.getUsername().isBlank() || customer.getName().isBlank() || customer.getSurname().isBlank()
                || customer.getEmail().isBlank() || customer.getStoreCity()==null){
                    uploadResponse.setMessage("I campi contrassegnati da (*) sono obbligatori.");
                    uploadResponse.setDone(false);
                    return uploadResponse;
                }
                if(customer.getBirthYear()==0){
                    uploadResponse.setMessage("Anno di nascita non valido.");
                    uploadResponse.setDone(false);
                    return uploadResponse;
                }

                if(customer.getPhoneNumber() != 0){
                    if(Long.toString(customer.getPhoneNumber()).length() != 10){
                        uploadResponse.setMessage("Il numero di telefono non contiene 10.");
                        uploadResponse.setDone(false);
                        return uploadResponse;
                    }
                }else{
                    uploadResponse.setMessage("Il numero di telefono contiene simboli proibiti.");
                    uploadResponse.setDone(false);
                    return uploadResponse;
                }
                if (UserDAO.getInstance().userExist(customer.getUsername())) {
                    uploadResponse.setMessage("Nome utente non disponibile");
                    uploadResponse.setDone(false);
                    return uploadResponse;
                }
                int row = CustomerDAO.getInstance().addNewCustomer(customer);
                if(row!=1){
                    uploadResponse.setMessage("Errore sconosciuto nel DB.");
                    uploadResponse.setDone(false);
                    return uploadResponse;
                }
                return uploadResponse;
    }

    public enum UserPrivilege {USER, MANAGER, ADMINISTRATOR}

    private UserBusiness(){
    }

    public static UserBusiness getInstance() {
        return instance;
    }

    public LoginResponse login(String username, String password){
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setMessage("Errore sconosciuto.");

        UserDAO userDAO = UserDAO.getInstance();

        if( !userDAO.userExist(username) ){
            loginResponse.setMessage("Il nome utente non esiste.");
            return loginResponse;
        }

        if(!userDAO.credentialsOk(username, password)){
            loginResponse.setMessage("Password errata.");
            return loginResponse;
        }

        if(ManagerDAO.getInstance().isDisabled(username)){
            loginResponse.setMessage("L'Account è disabilitato. Contattare l'amministrazione");
            return loginResponse;
        }

        User user = userDAO.getByUsername(username);
        loginResponse.setUser(user);
        loginResponse.setMessage("Benvenuto " + user.getName() + " " + user.getSurname());
        return loginResponse;
    }

    public UserPrivilege getUserPrivilege(User u){
        UserDAO userDAO = UserDAO.getInstance();
        if(u == null){
            return null;
        }
        if(userDAO.managerExist(u.getUsername())){
            return UserPrivilege.MANAGER;
        }

        if(userDAO.administratorExist(u.getUsername())){
            return UserPrivilege.ADMINISTRATOR;
        }
        if(userDAO.userExist(u.getUsername())){
            return UserPrivilege.USER;
        }
        return null;
    }

    public JTable getUsersTable(User loggedUser){
        TableUsersFactory tableUsersFactory = new TableUsersFactory();
        String[][] data;
        JTable jTable = null;
        ArrayList<User> users;

        switch (getUserPrivilege(loggedUser)){
            case ADMINISTRATOR:{
                users = AdministratorBusiness.getInstance().getAllManagers();
                data = tableUsersFactory.getData(TableUsersFactory.TableTypeUser.MANAGER_TYPE, users);
                jTable = tableUsersFactory.create(TableUsersFactory.TableTypeUser.MANAGER_TYPE, data);
                break;
            }

            case MANAGER:{
                users = ManagerBusiness.getInstance().getUsersByManagerUsername(loggedUser.getUsername());
                data = tableUsersFactory.getData(TableUsersFactory.TableTypeUser.USER_TYPE, users);
                jTable = tableUsersFactory.create(TableUsersFactory.TableTypeUser.USER_TYPE, data);
                break;
            }
            default:{
                System.out.println("Errore creazione tabella utente");
                break;
            }
        }
        return jTable;
    }

}
