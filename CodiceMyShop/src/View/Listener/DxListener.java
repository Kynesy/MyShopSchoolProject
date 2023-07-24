package View.Listener;

import Business.*;

import Model.*;
import View.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class DxListener implements ActionListener {
    private DefaultFrame frame;
    public enum CommandKeyDx {
        SHOW_LOGIN, START_LOGIN, LOGOUT, START_REGISTRATION, SHOW_PERSONAL_DATA,
        DELETE_PERSONAL_ACCOUNT, UPDATE_DATA, SHOW_REGISTRATION
    }

    public DxListener(DefaultFrame frame){
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        CommandKeyDx cmd = CommandKeyDx.valueOf(e.getActionCommand());
        System.out.println("Azionato comando:" + cmd);

        switch (cmd){

            case SHOW_REGISTRATION : {
                Store[] stores = AdministratorBusiness.getInstance().getAllStores();
                if(stores == null || stores.length ==0) {
                    JOptionPane.showMessageDialog(null, "Impossibile registrarsi poichè il DB non contiene alcun punto vendita." +
                            " Attendi che un amministratore ne crei uno prima.", "Errore", JOptionPane.ERROR_MESSAGE);
                    break;
                }

                JPanel registrationPanel = new RegistrationPanel(this, stores);
                frame.setMainPanel(registrationPanel);
                break;
            }

            case SHOW_LOGIN : {
                JPanel loginPanel = new LoginPanel(this);
                frame.setMainPanel(loginPanel);
                break;
            }

            case START_LOGIN: {
                LoginPanel panel = (LoginPanel) frame.getMainPanel();
                String username = panel.getUsername();
                String password = panel.getPassword();
                LoginResponse loginResponse = UserBusiness.getInstance().login(username, password);
                System.out.println(loginResponse.getMessage());

                if (loginResponse.getUser() != null) {
                    SessionManager.getInstance().getSession().put(SessionManager.LoginStatus.ONLINE, loginResponse.getUser());
                }

                JOptionPane.showMessageDialog(frame, loginResponse.getMessage(), "Info", JOptionPane.INFORMATION_MESSAGE);

                frame.getSxPanel().refresh();
                frame.getDxPanel().refresh();
                try {
                    frame.setMainPanel(new StartingPanel(new File("MyshopGif.gif")));
                } catch (IOException ioException) {
                    JPanel tmpPanel= new JPanel();
                    tmpPanel.add(new JLabel("Errore nel caricamento dell'immagine.(Path error)"));
                    frame.setMainPanel(tmpPanel);
                }
                break;
            }

            case LOGOUT: {
                SessionManager.getInstance().getSession().remove(SessionManager.LoginStatus.ONLINE);
                frame.getSxPanel().refresh();
                frame.getDxPanel().refresh();
                try {
                    frame.setMainPanel(new StartingPanel(new File("MyshopGif.gif")));
                } catch (IOException ioException) {
                    JPanel tmpPanel= new JPanel();
                    tmpPanel.add(new JLabel("Errore nel caricamento dell'immagine.(Path error)"));
                    frame.setMainPanel(tmpPanel);
                }
                break;
            }

            case START_REGISTRATION: {
                RegistrationPanel registationPanel = (RegistrationPanel) frame.getMainPanel();

                if (!registationPanel.comparePasswords()) {
                    JOptionPane.showMessageDialog(frame, "Le password non corrispondono", "Errore", JOptionPane.ERROR_MESSAGE);
                    break;
                }
                Customer customer = new Customer();
                customer.setUsername(registationPanel.getUsername());
                customer.setPassword(registationPanel.getPassword1());
                customer.setName(registationPanel.getName());
                customer.setSurname(registationPanel.getSurname());
                customer.setEmail(registationPanel.getEmail());
                customer.setOccupation(registationPanel.getOccupation());
                customer.setPhoneNumber(registationPanel.getPhoneNumber());
                customer.setBirthYear(registationPanel.getBirthYear());
                customer.setStoreCity(registationPanel.getStoreCity().getCity());
                customer.setAddress(registationPanel.getAddress());

                UploadResponse uploadResponse = UserBusiness.getInstance().registrate(customer);
                if(uploadResponse.isDone()){
                    SessionManager.getInstance().getSession().put(SessionManager.LoginStatus.ONLINE, customer);
                    frame.getSxPanel().refresh();
                    frame.getDxPanel().refresh();
                    try {
                        frame.setMainPanel(new StartingPanel(new File("MyshopGif.gif")));
                    } catch (IOException ioException) {
                        JPanel tmpPanel= new JPanel();
                        tmpPanel.add(new JLabel("Errore nel caricamento dell'immagine.(Path error)"));
                        frame.setMainPanel(tmpPanel);
                    }                }
                JOptionPane.showMessageDialog(null, uploadResponse.getMessage(), "Info", JOptionPane.INFORMATION_MESSAGE);
                break;
            }

            case SHOW_PERSONAL_DATA:{
                User u = SessionManager.getInstance().getSession().get(SessionManager.LoginStatus.ONLINE);
                UserBusiness.UserPrivilege userPrivilege = UserBusiness.getInstance().getUserPrivilege(u);

                User user = null;
                if(UserBusiness.UserPrivilege.ADMINISTRATOR == userPrivilege){
                    user = AdministratorBusiness.getInstance().getAdminByUsername(u.getUsername());
                } else if (UserBusiness.UserPrivilege.MANAGER == userPrivilege){
                    user = AdministratorBusiness.getInstance().getManagerByUsername(u.getUsername());
                }else if (UserBusiness.UserPrivilege.USER == userPrivilege){
                    user = CustomerBusiness.getInstance().getByUsername(u.getUsername());
                }
                Store[] stores = AdministratorBusiness.getInstance().getAllStores();

                JPanel personalDataPanel = new PersonalDataPanel(this , userPrivilege, user, stores);
                frame.setMainPanel( personalDataPanel );
                break;
            }

            case DELETE_PERSONAL_ACCOUNT: {
                int choose = JOptionPane.showConfirmDialog(frame, "Sei sicuro di voler eliminare il tuo account?", "Eliminazione account", JOptionPane.YES_NO_OPTION);
                if(choose == JOptionPane.YES_OPTION){
                    User user = SessionManager.getInstance().getSession().get(SessionManager.LoginStatus.ONLINE);

                    System.out.println("deleting account: " + user.getUsername());
                    UploadResponse uploadResponse = UserBusiness.getInstance().removeByUsername(user.getUsername());

                    SessionManager.getInstance().getSession().remove(SessionManager.LoginStatus.ONLINE);
                    JOptionPane.showMessageDialog(null, uploadResponse.getMessage(), "Info", JOptionPane.INFORMATION_MESSAGE);
                    frame.getSxPanel().refresh();
                    frame.getDxPanel().refresh();
                    try {
                        frame.setMainPanel(new StartingPanel(new File("MyshopGif.gif")));
                    } catch (IOException ioException) {
                        JPanel tmpPanel= new JPanel();
                        tmpPanel.add(new JLabel("Errore nel caricamento dell'immagine.(Path error)"));
                        frame.setMainPanel(tmpPanel);
                    }                }
                break;
            }

            case UPDATE_DATA: {
                User actualUser = SessionManager.getInstance().getSession().get(SessionManager.LoginStatus.ONLINE);
                PersonalDataPanel personalDataPanel = (PersonalDataPanel) frame.getMainPanel();
                User updatedUser = null;
                UploadResponse uploadResponse;
                switch (UserBusiness.getInstance().getUserPrivilege(actualUser)) {
                    case USER: {
                        updatedUser = new Customer();
                        updatedUser.setUsername(actualUser.getUsername());
                        updatedUser.setName(personalDataPanel.getName());
                        updatedUser.setSurname(personalDataPanel.getSurname());
                        updatedUser.setEmail(personalDataPanel.getEmail());
                        ((Customer) updatedUser).setAddress(personalDataPanel.getAddress());
                        ((Customer) updatedUser).setBirthYear(personalDataPanel.getBirthYear());
                        ((Customer) updatedUser).setOccupation(personalDataPanel.getOccupation());
                        ((Customer) updatedUser).setPhoneNumber(personalDataPanel.getPhoneNumber());
                        ((Customer) updatedUser).setStoreCity(personalDataPanel.getStoreCity().getCity());
                        uploadResponse = CustomerBusiness.getInstance().updateCustomer(((Customer) updatedUser));
                        break;
                    }
                    case MANAGER: {
                        updatedUser = new Manager();
                        updatedUser.setUsername(actualUser.getUsername());
                        updatedUser.setName(personalDataPanel.getName());
                        updatedUser.setSurname(personalDataPanel.getSurname());
                        updatedUser.setEmail(personalDataPanel.getEmail());
                        uploadResponse = ManagerBusiness.getInstance().updateManager((Manager) updatedUser);
                        break;
                    }
                    case ADMINISTRATOR: {
                        updatedUser = new Administrator();
                        updatedUser.setUsername(actualUser.getUsername());
                        updatedUser.setName(personalDataPanel.getName());
                        updatedUser.setSurname(personalDataPanel.getSurname());
                        updatedUser.setEmail(personalDataPanel.getEmail());
                        uploadResponse = AdministratorBusiness.getInstance().updateAdmin((Administrator) updatedUser);
                        break;
                    }
                    default:{
                        uploadResponse = new UploadResponse();
                        uploadResponse.setDone(false);
                        uploadResponse.setMessage("Errore riconoscimento privilegio utente");
                    }
                }

                if (uploadResponse.isDone()) {
                    SessionManager.getInstance().getSession().remove(SessionManager.LoginStatus.ONLINE);
                    SessionManager.getInstance().getSession().put(SessionManager.LoginStatus.ONLINE, updatedUser);
                }
                JOptionPane.showMessageDialog(null, uploadResponse.getMessage(), "Info", JOptionPane.INFORMATION_MESSAGE);
                break;
            }
        }

        frame.invalidate();
        frame.revalidate();
        frame.repaint();
    }

}
