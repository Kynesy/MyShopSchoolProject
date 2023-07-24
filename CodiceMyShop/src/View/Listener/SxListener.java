package View.Listener;

import Business.*;
import Model.*;
import View.*;
import View.AddArticlePanel.AddArticlePanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class SxListener implements ActionListener {
    private DefaultFrame frame;
    public enum CommandKeySx {BROWSE, //serve a ogni utente
        SHOW_WISHLIST, // specifici dell'utente loggato
        SHOW_MAIL_CREATION, SHOW_USERS, //specifici del manager
        SHOW_ADD_ARTICLE, SHOW_ADD_CATEGORY, SHOW_CREATE_VENDOR, SHOW_ADD_STORE, SHOW_ADD_MANAGER, //specifici dell'admin
        SHOW_ADD_ADMIN, SHOW_ORDERS, SHOW_PRENOTATIONS, HOME, //servono solo per la creazione del primo admin
    }

    private AdminListener adminListener;
    private ManagerListener managerListener;
    private BrowseListener browseListener;

    public SxListener(DefaultFrame frame){
        this.frame = frame;
        adminListener = new AdminListener(frame);
        managerListener = new ManagerListener(frame);
        browseListener = new BrowseListener(frame);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        CommandKeySx cmd = CommandKeySx.valueOf(e.getActionCommand());
        System.out.println("Azionato comando:" + cmd);

        switch (cmd){
            case HOME:{
                try {
                    frame.setMainPanel(new StartingPanel(new File("MyshopGif.gif")));
                } catch (IOException ioException) {
                    JPanel tmpPanel= new JPanel();
                    tmpPanel.add(new JLabel("Errore nel caricamento dell'immagine.(Path error)"));
                    frame.setMainPanel(tmpPanel);
                }
                break;
            }
            case BROWSE: {
                User u = SessionManager.getInstance().getSession().get(SessionManager.LoginStatus.ONLINE);
                frame.getSxPanel().refresh();
                frame.getDxPanel().refresh();
                JTable jTable;
                if(u==null) {
                    jTable = ArticleBusiness.getInstance().getArticleTable(null);
                } else {
                    jTable = ArticleBusiness.getInstance().getArticleTable(u);
                }
                BrowsePanel browsePanel = new BrowsePanel(browseListener, jTable);
                frame.setMainPanel(browsePanel);
                break;
            }

            case SHOW_ORDERS:{
                ArrayList<Order> orders = OrderBusiness.getInstance().getAllOrders();
                frame.setMainPanel(new OrdersPanel(managerListener, orders));
                break;
            }

            case SHOW_WISHLIST:{
                User u = SessionManager.getInstance().getSession().get(SessionManager.LoginStatus.ONLINE);
                WishList[] wishListArrayList = CustomerBusiness.getInstance().getAllCustomerWishlists(u);
                if(wishListArrayList==null || wishListArrayList.length == 0){
                    JOptionPane.showMessageDialog(null, "Non hai liste, creane una.", "Errore", JOptionPane.INFORMATION_MESSAGE);
                    break;
                }
                frame.setMainPanel(new WishlistPanel(browseListener, wishListArrayList));
                break;
            }

            case SHOW_PRENOTATIONS:{
                ArrayList<Prenotation> prenotations = OrderBusiness.getInstance().getAllPrenotations();
                frame.setMainPanel(new PrenotationsPanel(new ManagerListener(frame), prenotations));
                break;
            }

            case SHOW_MAIL_CREATION:{
                User loggedManager = SessionManager.getInstance().getSession().get(SessionManager.LoginStatus.ONLINE);
                ArrayList<User> users = ManagerBusiness.getInstance().getUsersByManagerUsername(loggedManager.getUsername());
                if(users != null) {
                    SendMailPanel sendMailPanel = new SendMailPanel(users, managerListener);
                    frame.setMainPanel(sendMailPanel);
                }
                break;
            }

            case SHOW_USERS:{
                User loggedUser = SessionManager.getInstance().getSession().get(SessionManager.LoginStatus.ONLINE);
                JTable jTable = UserBusiness.getInstance().getUsersTable(loggedUser);
                frame.setMainPanel(new RemoveUserPanel(managerListener, jTable));
                break;
            }

            case SHOW_ADD_ARTICLE:{
                Store[] storeArrayList = AdministratorBusiness.getInstance().getAllStores();
                Vendor[] itemVendorsArrayList = ItemBusiness.getInstance().getAllItemVendors();
                Vendor[] serviceVendorsArrayList = ServiceBusiness.getInstance().getAllServiceVendors();
                Category[] categories = ArticleBusiness.getInstance().getAllCategories();

                if((storeArrayList == null) || storeArrayList.length == 0 ) {
                    JOptionPane.showMessageDialog(null, "Impossibile inserire un articolo poichè il DB non contiene alcun punto vendita." +
                            " Creane uno prima.", "Errore", JOptionPane.ERROR_MESSAGE);
                    break;
                }
                if(itemVendorsArrayList == null || itemVendorsArrayList.length == 0) {
                    JOptionPane.showMessageDialog(null, "Impossibile inserire un articolo poichè il DB non contiene alcun produttore." +
                            " Creane uno prima.", "Errore", JOptionPane.ERROR_MESSAGE);
                    break;
                }
                if(serviceVendorsArrayList == null || serviceVendorsArrayList.length == 0) {
                    JOptionPane.showMessageDialog(null, "Impossibile inserire un articolo poichè il DB non contiene alcun fornitore." +
                            " Creane uno prima.", "Errore", JOptionPane.ERROR_MESSAGE);
                    break;
                }
                if(categories == null || categories.length == 0) {
                    JOptionPane.showMessageDialog(null, "Impossibile inserire un articolo poichè il DB non contiene alcuna categoria." +
                            " Creane una prima.", "Errore", JOptionPane.ERROR_MESSAGE);
                    break;
                }

                ArrayList<Item> items = new ArrayList<>(ItemBusiness.getInstance().getAllItemsByStore(null));
                ArrayList<Item> itemsNoComposite = CompositeBusiness.getInstance().removeCompositeFromArray(items);
                JPanel addArticlePanel = new AddArticlePanel(adminListener,itemsNoComposite, storeArrayList, itemVendorsArrayList, serviceVendorsArrayList, categories);
                frame.setMainPanel(addArticlePanel);
                break;
            }

            case SHOW_ADD_CATEGORY:{
                Category[] c = ArticleBusiness.getInstance().getAllCategories();
                AddCategoryPanel addCategoryPanel = new AddCategoryPanel(adminListener, c);
                frame.setMainPanel(addCategoryPanel);
                break;
            }

            case SHOW_CREATE_VENDOR:{
                JPanel createVendorPanel = new AddVendorPanel(adminListener);
                frame.setMainPanel(createVendorPanel);
                break;
            }

            case SHOW_ADD_STORE:{
                JPanel addStorePanel = new AddStorePanel(adminListener);
                frame.setMainPanel(addStorePanel);
                break;
            }

            case SHOW_ADD_MANAGER:{
                Store[] storeArrayList = AdministratorBusiness.getInstance().getAllStores();
                if(storeArrayList.length == 0 || storeArrayList == null){
                    JOptionPane.showMessageDialog(null, "Non esistono ancora punti vendita. Creane uno" +
                            " per procedere alla creazione del Manager.", "Errore", JOptionPane.ERROR_MESSAGE);
                    break;
                }
                JPanel addManagerPanel = new AddManagerSubpanel(adminListener, storeArrayList);
                frame.setMainPanel(addManagerPanel);
                break;
            }

            case SHOW_ADD_ADMIN:{
                JPanel addAdminPanel = new AddAdminSubpanel(adminListener);
                frame.setMainPanel(addAdminPanel);
                break;
            }
        }

        frame.invalidate();
        frame.revalidate();
        frame.repaint();
    }
}
