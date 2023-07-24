package View.Listener;

import Business.*;
import Model.Order;
import Model.Prenotation;
import Model.UploadResponse;
import Model.User;
import View.*;
import com.itextpdf.text.DocumentException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class ManagerListener implements ActionListener {
    private DefaultFrame frame;
    public enum CommandKeyManager{SEND_MAIL, DISABLE_ACCOUNT, ABILITATE_ACCOUNT, DELETE_ACCOUNT, SET_PAID, DELETE_ORDER, APPROVE_PRENOTATION,

    }

    public ManagerListener(DefaultFrame frame){
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ManagerListener.CommandKeyManager cmd = ManagerListener.CommandKeyManager.valueOf(e.getActionCommand());
        System.out.println("Azionato comando:" + cmd);
        switch (cmd){
            case DELETE_ORDER:{
                OrdersPanel ordersPanel = (OrdersPanel) frame.getMainPanel();
                Order order = ordersPanel.getSelectedOrder();

                UploadResponse uploadResponse = OrderBusiness.getInstance().deleteOrder(order);
                JOptionPane.showMessageDialog(null, uploadResponse.getMessage(), "Info", JOptionPane.INFORMATION_MESSAGE);

                ArrayList<Order> orders = OrderBusiness.getInstance().getAllOrders();
                frame.setMainPanel(new OrdersPanel(this, orders));

                break;
            }

            case APPROVE_PRENOTATION:{
                PrenotationsPanel prenotationsPanel = (PrenotationsPanel) frame.getMainPanel();
                Prenotation prenotation = prenotationsPanel.getSelectedPrenotation();
                UploadResponse uploadResponse = OrderBusiness.getInstance().approvePrenotation(prenotation);
                JOptionPane.showMessageDialog(null, uploadResponse.getMessage(), "Info", JOptionPane.INFORMATION_MESSAGE);
                //riaggiornare le prenotazioni
                ArrayList<Prenotation> prenotations = OrderBusiness.getInstance().getAllPrenotations();
                frame.setMainPanel(new PrenotationsPanel(this, prenotations));
                break;
            }



            case SET_PAID:{
                OrdersPanel ordersPanel = (OrdersPanel) frame.getMainPanel();
                Order order = ordersPanel.getSelectedOrder();
                UploadResponse uploadResponse = new UploadResponse();

                if(order != null){
                    uploadResponse.setMessage("Errore sconosciuto (listener del manager)");
                    try {
                        uploadResponse = OrderBusiness.getInstance().generateReceipt(order.getUsername(), order.getOrderID());
                    } catch (DocumentException | FileNotFoundException documentException) {
                        documentException.printStackTrace();
                    }
                    ArrayList<Order> orders = OrderBusiness.getInstance().getAllOrders();
                    frame.setMainPanel(new OrdersPanel(this, orders));
                } else{
                    uploadResponse.setMessage("Selezionare un ordine prima.");
                }
                JOptionPane.showMessageDialog(null, uploadResponse.getMessage(), "Info", JOptionPane.INFORMATION_MESSAGE);
                break;
            }

            case SEND_MAIL:{
                SendMailPanel sendMailPanel = (SendMailPanel) frame.getMainPanel();
                String subject = sendMailPanel.getSubject();
                String message = sendMailPanel.getMail();
                UploadResponse uploadResponse;
                if(sendMailPanel.sendingToAllUsers()){
                    ArrayList<User> userArrayList = sendMailPanel.getUserArrayList();
                    uploadResponse = ManagerBusiness.getInstance().sendMail(message, subject, userArrayList);
                }else{
                    String email = sendMailPanel.getSelectedUser();
                    if(email==null){
                        uploadResponse= new UploadResponse();
                        uploadResponse.setMessage("Utente non valido");
                    }else {
                        uploadResponse = ManagerBusiness.getInstance().sendMail(message, subject, email);
                    }
                }
                JOptionPane.showMessageDialog(null, uploadResponse.getMessage(), "Informazione",
                        JOptionPane.INFORMATION_MESSAGE);
                break;
            }

            case DISABLE_ACCOUNT:{
                RemoveUserPanel removeUserPanel = (RemoveUserPanel) frame.getMainPanel();
                String selectedUsername = removeUserPanel.getSelectedUser();

                UploadResponse uploadResponse = ManagerBusiness.getInstance().disableUser(selectedUsername);
                JOptionPane.showMessageDialog(frame, uploadResponse.getMessage(), "Informazione", JOptionPane.INFORMATION_MESSAGE);

                User loggedUser = SessionManager.getInstance().getSession().get(SessionManager.LoginStatus.ONLINE);
                JTable jTable = UserBusiness.getInstance().getUsersTable(loggedUser);
                frame.setMainPanel(new RemoveUserPanel(this, jTable));
                break;
            }

            case ABILITATE_ACCOUNT:{
                RemoveUserPanel removeUserPanel = (RemoveUserPanel) frame.getMainPanel();
                String selectedUsername = removeUserPanel.getSelectedUser();
                UploadResponse uploadResponse = ManagerBusiness.getInstance().activateAccount(selectedUsername);
                JOptionPane.showMessageDialog(null, uploadResponse.getMessage(), "Info", JOptionPane.INFORMATION_MESSAGE);

                User loggedUser = SessionManager.getInstance().getSession().get(SessionManager.LoginStatus.ONLINE);
                JTable jTable = UserBusiness.getInstance().getUsersTable(loggedUser);
                frame.setMainPanel(new RemoveUserPanel(this, jTable));
                break;
            }

            case DELETE_ACCOUNT:{
                RemoveUserPanel removeUserPanel = (RemoveUserPanel) frame.getMainPanel();
                String selectedUsername = removeUserPanel.getSelectedUser();
                if(selectedUsername == null){
                    JOptionPane.showMessageDialog(null, "Seleziona un account prima", "Errore",
                            JOptionPane.ERROR_MESSAGE);
                    break;
                }
                int choose = JOptionPane.showConfirmDialog(frame, "Sei sicuro di voler eliminare l'utente "+ selectedUsername +"?", "Eliminazione account", JOptionPane.YES_NO_OPTION);
                UploadResponse uploadResponse = new UploadResponse();
                uploadResponse.setMessage("Eliminazione account annullata.");
                if(choose == JOptionPane.YES_OPTION){
                    System.out.println("Manca metodo eliminazione account");
                    uploadResponse = UserBusiness.getInstance().removeByUsername(selectedUsername);
                }

                JOptionPane.showMessageDialog(null, uploadResponse.getMessage(), "Info", JOptionPane.INFORMATION_MESSAGE);
                User loggedUser = SessionManager.getInstance().getSession().get(SessionManager.LoginStatus.ONLINE);
                JTable jTable = UserBusiness.getInstance().getUsersTable(loggedUser);
                frame.setMainPanel(new RemoveUserPanel(this, jTable));
                break;
            }
        }
        frame.invalidate();
        frame.revalidate();
        frame.repaint();
    }
}
