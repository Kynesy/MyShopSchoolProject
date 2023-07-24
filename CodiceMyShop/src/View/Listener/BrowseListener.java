package View.Listener;

import Business.*;
import Business.ArticleFactory.ArticleFactoryProvider;
import Model.*;
import View.*;
import View.AddArticlePanel.ModifyComposite;
import View.AddArticlePanel.ModifyItemPanel;
import View.AddArticlePanel.ModifyServicePanel;
import com.itextpdf.text.DocumentException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.util.ArrayList;


public class BrowseListener implements ActionListener {
    private IStandardFrame frame;

    public enum CommandKeyBrowse {CREATE_WISHLIST, ADD_ARTICLE_TO_WISHLIST, ORDER, REMOVE_ARTICLE_FROM_WISHLIST, DELETE_WISHLIST, EMPTY_WISHLIST, //specifici dell'user
        UPDATE_AVAIABILITY, //manager
        DELETE_ARTICLE, //da fare admin
        CREATE_FEEDBACK, SHOW_FEEDBACK_FORM, OPEN_FEEDBACK, REPLY_FEEDBACK, SHOW_ARTICLE_MODIFY, SHOW_ARTICLE_DETAILS // generici
    }

    public void setFrame(IStandardFrame frame) {
        this.frame = frame;
    }

    public BrowseListener(IStandardFrame frame){
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        CommandKeyBrowse cmd = CommandKeyBrowse.valueOf(e.getActionCommand());
        System.out.println("Azionato comando:" + cmd);

        switch (cmd){
            case REPLY_FEEDBACK:{
                IArticlePannels articlePannels = (IArticlePannels) frame.getMainPanel();
                Feedback feedback = articlePannels.getSelectedFeedback();
                int idArticle = articlePannels.getSelected();
                if(feedback == null){
                    JOptionPane.showMessageDialog(null, "Selezionare una recensione prima.", "Errore", JOptionPane.INFORMATION_MESSAGE);
                    break;
                }
                User user = UserBusiness.getInstance().getUser(feedback.getUsername());
                String message = JOptionPane.showInputDialog("Scrivi la risposta da inviare all'utente:");
                if(message==null){
                    break;
                }
                UploadResponse uploadResponse = new UploadResponse();
                uploadResponse.setMessage("Testo non valido.");

                if(!message.isBlank()) {
                 uploadResponse = ManagerBusiness.getInstance().sendMail(message + "\n\n Lo Staff di MyShop.",
                            "MyShop: Risposta recensione articolo [ID:" + idArticle + "]", user.getEmail());
                }
                JOptionPane.showMessageDialog(null, uploadResponse.getMessage(), "Info", JOptionPane.INFORMATION_MESSAGE);
                break;
            }

            case SHOW_ARTICLE_MODIFY:{
                IArticlePannels browsePanel = (IArticlePannels) frame.getMainPanel();
                int articleID = browsePanel.getSelected();
                if(articleID==-1){
                    JOptionPane.showMessageDialog(null, "Nessun articolo selezionato.",
                            "Informazione", JOptionPane.INFORMATION_MESSAGE);
                    break;
                }
                switch (ArticleBusiness.getInstance().getArticleType(articleID)){
                    case ITEM:{
                        Item item = ItemBusiness.getInstance().getItemById(articleID);
                        Store[] storeArrayList = AdministratorBusiness.getInstance().getAllStores();
                        Vendor[] itemVendorsArrayList = ItemBusiness.getInstance().getAllItemVendors();
                        Category[] categories = ArticleBusiness.getInstance().getAllCategories();
                        ModifyItemPanel modifyItemPanel = new ModifyItemPanel(new AdminListener(frame), storeArrayList,
                                itemVendorsArrayList,categories);
                        modifyItemPanel.setItem(item);
                        frame.setMainPanel(modifyItemPanel);
                        break;
                    }

                    case SERVICE:{
                        Vendor[] serviceVendorsArrayList = ServiceBusiness.getInstance().getAllServiceVendors();
                        Service service = ServiceBusiness.getInstance().getServiceById(articleID);
                        Category[] categories = ArticleBusiness.getInstance().getAllCategories();
                        ModifyServicePanel modifyServicePanel = new ModifyServicePanel(new AdminListener(frame), serviceVendorsArrayList, categories);
                        modifyServicePanel.setService(service);
                        frame.setMainPanel(modifyServicePanel);
                        break;
                    }

                    case COMPOSITE_ITEM:{
                        CompositeItem compositeItem = CompositeBusiness.getInstance().getCompositeByID(articleID);
                        ArrayList<Item> items = new ArrayList<>(ItemBusiness.getInstance().getAllItemsByStore(null));
                        ArrayList<Item> itemsNoComposite = CompositeBusiness.getInstance().removeCompositeFromArray(items);

                        Store[] storeArrayList = AdministratorBusiness.getInstance().getAllStores();
                        Vendor[] itemVendorsArrayList = ItemBusiness.getInstance().getAllItemVendors();
                        Category[] categories = ArticleBusiness.getInstance().getAllCategories();
                        ModifyComposite modifyComposite = new ModifyComposite(new AdminListener(frame),itemsNoComposite,
                                storeArrayList, itemVendorsArrayList, categories);
                        modifyComposite.setItem(compositeItem);
                        frame.setMainPanel(modifyComposite);
                    }
                }
                break;
            }

            case SHOW_ARTICLE_DETAILS:{
                IArticlePannels browsePanel = (IArticlePannels) frame.getMainPanel();
                int articleID = browsePanel.getSelected();
                if(articleID==-1){
                    JOptionPane.showMessageDialog(null, "Nessun articolo selezionato.",
                            "Informazione", JOptionPane.INFORMATION_MESSAGE);
                    break;
                }
                User u = SessionManager.getInstance().getSession().get(SessionManager.LoginStatus.ONLINE);
                UserBusiness.UserPrivilege userPrivilege = UserBusiness.getInstance().getUserPrivilege(u);
                switch (ArticleBusiness.getInstance().getArticleType(articleID)){
                    case ITEM:{
                        Item item = ItemBusiness.getInstance().getItemById(articleID);
                        ArrayList<Feedback> feedbackArrayList = ArticleBusiness.getInstance().getAllFeedbacksByID(articleID);
                        InformationFrame informationFrame = new InformationFrame("Dettaglio prodotto", null);
                        ItemDetailsPanel itemDetailsPanel = new ItemDetailsPanel(item, feedbackArrayList,
                                new BrowseListener(informationFrame),userPrivilege );
                        informationFrame.setMainPanel(itemDetailsPanel);

                        break;
                    }
                    case SERVICE:{
                        Service service = ServiceBusiness.getInstance().getServiceById(articleID);
                        ArrayList<Feedback> feedbackArrayList = ArticleBusiness.getInstance().getAllFeedbacksByID(articleID);
                        InformationFrame informationFrame = new InformationFrame("Dettaglio servizio", null);
                        ServiceDetailsPanel serviceDetailsPanel = new ServiceDetailsPanel(service,feedbackArrayList,
                                new BrowseListener(informationFrame), userPrivilege);
                        informationFrame.setMainPanel(serviceDetailsPanel);
                        break;
                    }
                    case COMPOSITE_ITEM:{
                        CompositeItem compositeItem = CompositeBusiness.getInstance().getCompositeByID(articleID);
                        InformationFrame informationFrame = new InformationFrame("Dettaglio Prodotto Composto", null);
                        BrowseListener tmpListener = new BrowseListener(informationFrame);
                        CompositeDetailsPanel compositeDetailsPanel = new CompositeDetailsPanel(compositeItem, tmpListener);
                        informationFrame.setMainPanel(compositeDetailsPanel);
                        break;
                    }
                }
                break;
            }

            case CREATE_WISHLIST:{
                User u = SessionManager.getInstance().getSession().get(SessionManager.LoginStatus.ONLINE);
                String listName;
                boolean done;
                do {
                    listName = JOptionPane.showInputDialog("Scegli un nome per la lista:");
                    if(listName == null) break;
                    UploadResponse uploadResponse = CustomerBusiness.getInstance().createWishlist(listName, u);
                    JOptionPane.showMessageDialog(null, uploadResponse.getMessage(),"Informazione", JOptionPane.INFORMATION_MESSAGE);
                    done = uploadResponse.isDone();
                }while (!done);
                break;
            }

            case ADD_ARTICLE_TO_WISHLIST:{
                User user = SessionManager.getInstance().getSession().get(SessionManager.LoginStatus.ONLINE);//prendo l'utente
                WishList[] listArray = CustomerBusiness.getInstance().getAllCustomerWishlists(user); //prendo le sue liste
                if(listArray.length == 0) {//verifico che abbia liste
                    JOptionPane.showMessageDialog(null, "Non hai liste. Creane una prima.",
                            "Informazione", JOptionPane.INFORMATION_MESSAGE);
                    break;
                }

                IArticlePannels panel = (IArticlePannels) frame.getMainPanel();
                int id = panel.getSelected();//prendo l'id dell'articolo da aggiungere
                //creo un popup di selezione lista e quantità
                JPanel comboBoxPanel = new JPanel(new GridLayout(2,2));
                ArticleFactoryProvider.ArticleType articleType = ArticleBusiness.getInstance().getArticleType(id);
                JComboBox<WishList> comboBox = new JComboBox<>(listArray);
                comboBoxPanel.add(new JLabel("Seleziona una lista:"));
                comboBoxPanel.add(comboBox);
                JTextField quantityTxt = new JTextField(10);
                if(articleType == ArticleFactoryProvider.ArticleType.ITEM || articleType == ArticleFactoryProvider.ArticleType.COMPOSITE_ITEM){//le opzioni per la quantità compaiono solo se è un prodotto
                    comboBoxPanel.add(new JLabel("Quantità: "));
                    comboBoxPanel.add(quantityTxt);
                }
                int choose = JOptionPane.showConfirmDialog(null, comboBoxPanel,
                        "Aggiunta articolo", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if(choose != JOptionPane.YES_OPTION) break;

                int quantity = 0;//setto la quantità base a 0
                WishList wishList = (WishList) comboBox.getSelectedItem();//prendo la lista selezionata

                if(articleType == ArticleFactoryProvider.ArticleType.ITEM || articleType == ArticleFactoryProvider.ArticleType.COMPOSITE_ITEM){//verifico che l'utente abbia inserito un numero
                    try {
                        quantity = Integer.parseInt(quantityTxt.getText());
                    } catch (NumberFormatException numberFormatException){
                        JOptionPane.showMessageDialog(null, "Il campo deve contenere solo numeri interi", "Errore", JOptionPane.ERROR_MESSAGE);
                        break;
                    }
                }
                UploadResponse uploadResponse = CustomerBusiness.getInstance().addArticleToWishlist(wishList, id, quantity);
                JOptionPane.showMessageDialog(null, uploadResponse.getMessage(), "Informazione", JOptionPane.INFORMATION_MESSAGE);
                break;
            }

            case UPDATE_AVAIABILITY:{
                IArticlePannels panel = (IArticlePannels) frame.getMainPanel();
                int productID = panel.getSelected(); //prendo l'id del prodotto selezionato

                int newQuantity;
                boolean done;
                do {
                    done = true;
                    //chiedo la nuova quantità in un popup
                    String inputDialog = JOptionPane.showInputDialog(null, "Inserisci la nuova diponibilità: ",
                            "Disponibilità", JOptionPane.WARNING_MESSAGE);
                    if (inputDialog == null) break;

                    if(inputDialog.isEmpty()){//verifico non sia vuoto il campo
                        done = false;
                        JOptionPane.showMessageDialog(null, "Il campo non può essere vuoto.", "Errore", JOptionPane.ERROR_MESSAGE);
                    } else{
                        try{
                            newQuantity = Integer.parseInt(inputDialog);//mi assicuro siano numeri
                            UploadResponse uploadResponse = ManagerBusiness.getInstance().updateItemAvailability(productID, newQuantity);
                            done = uploadResponse.isDone();//definisco se l'operazione ha funzionato o no
                            JOptionPane.showMessageDialog(null, uploadResponse.getMessage(), "Informazione", JOptionPane.INFORMATION_MESSAGE);
                        } catch (NumberFormatException numberFormatException){
                            done = false;
                            JOptionPane.showMessageDialog(null, "Il campo deve contenere solo numeri interi", "Errore", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }while (!done);
                //riaggiorno il catalogo con le nuove quantità
                User u = SessionManager.getInstance().getSession().get(SessionManager.LoginStatus.ONLINE);
                frame.setMainPanel(new BrowsePanel(new BrowseListener(frame), ArticleBusiness.getInstance().getArticleTable(u)));
                break;
            }

            case ORDER:{
                User u = SessionManager.getInstance().getSession().get(SessionManager.LoginStatus.ONLINE);
                WishlistPanel wishlistPanel = (WishlistPanel) frame.getMainPanel();
                int choose = JOptionPane.showConfirmDialog(null, "Accettando avvierai la procedura dell'ordine.\n " +
                        "Non è possibile annullarla senza l'intervento di un membro dello Staff.\n" +
                        "Il totale del suo ordine è ["+ OrderBusiness.getInstance().calculateSubtotal(wishlistPanel.getWishlist())+" Euro]. Continuare?");
                if(choose == JOptionPane.YES_OPTION){
                    UploadResponse orderResponse = new UploadResponse();
                    orderResponse.setMessage("Errore sconosciuto (Livello listener)");
                    try {
                        orderResponse = OrderBusiness.getInstance().generateOrder(u, wishlistPanel.getWishlist());
                    } catch (DocumentException | FileNotFoundException documentException) {
                        documentException.printStackTrace();
                    }
                    JOptionPane.showMessageDialog(null, orderResponse.getMessage(), "Info", JOptionPane.INFORMATION_MESSAGE);
                }else{
                    JOptionPane.showMessageDialog(null, "Ordine interrotto.", "Informazione", JOptionPane.INFORMATION_MESSAGE);
                }
                //riaggiorno le liste
                WishList[] wishListArrayList = CustomerBusiness.getInstance().getAllCustomerWishlists(u);
                if(wishListArrayList==null || wishListArrayList.length == 0){
                    JOptionPane.showMessageDialog(null, "Non hai liste, creane una.", "Errore", JOptionPane.INFORMATION_MESSAGE);
                    break;
                }
                frame.setMainPanel(new WishlistPanel(this, wishListArrayList));

                break;
            }

            case REMOVE_ARTICLE_FROM_WISHLIST:{
                WishlistPanel wishlistPanel = (WishlistPanel) frame.getMainPanel();
                int articleID = wishlistPanel.getSelected();

                ArticleFactoryProvider.ArticleType articleType = ArticleBusiness.getInstance().getArticleType(articleID);
                int quantity = 1;
                if(articleType == ArticleFactoryProvider.ArticleType.ITEM || articleType == ArticleFactoryProvider.ArticleType.COMPOSITE_ITEM){
                    String input = JOptionPane.showInputDialog( "Quantità da rimuovere");
                    if(input == null){
                        break;
                    }
                    try {
                        quantity = Integer.parseInt(input);
                    }catch (NumberFormatException numberFormatException){
                        System.out.println("Errore formato numerico");
                        break;
                    }
                }

                UploadResponse uploadResponse = CustomerBusiness.getInstance().removeArticleFromWishlist(wishlistPanel.getWishlist(), articleID, quantity);
                JOptionPane.showMessageDialog(null, uploadResponse.getMessage(), "Informazione", JOptionPane.INFORMATION_MESSAGE);
                //aggiorno la lista
                User u = SessionManager.getInstance().getSession().get(SessionManager.LoginStatus.ONLINE);
                WishList[] wishListArrayList = CustomerBusiness.getInstance().getAllCustomerWishlists(u);
                frame.setMainPanel(new WishlistPanel(new BrowseListener(frame), wishListArrayList));
                break;
            }

            case EMPTY_WISHLIST:{
                WishlistPanel wishlistPanel = (WishlistPanel) frame.getMainPanel();
                WishList wishList = wishlistPanel.getWishlist();
                UploadResponse uploadResponse = CustomerBusiness.getInstance().emptyUserWishlist(wishList);
                JOptionPane.showMessageDialog(null, uploadResponse.getMessage(), "Info",
                        JOptionPane.INFORMATION_MESSAGE);
                //aggiorno le liste utente o mostro il catalogo se non ne ha
                User u = SessionManager.getInstance().getSession().get(SessionManager.LoginStatus.ONLINE);
                WishList[] wishListArrayList = CustomerBusiness.getInstance().getAllCustomerWishlists(u);
                if(wishListArrayList==null || wishListArrayList.length == 0){
                    JOptionPane.showMessageDialog(null, "Non hai liste, creane una.", "Errore", JOptionPane.INFORMATION_MESSAGE);
                    break;
                }
                frame.setMainPanel(new WishlistPanel(new BrowseListener(frame), wishListArrayList));
                break;

            }

            case DELETE_WISHLIST:{
                WishlistPanel wishlistPanel = (WishlistPanel) frame.getMainPanel();
                WishList wishList = wishlistPanel.getWishlist();
                UploadResponse uploadResponse = CustomerBusiness.getInstance().deleteUserWishlist(wishList);
                JOptionPane.showMessageDialog(null, uploadResponse.getMessage(), "Info",
                        JOptionPane.INFORMATION_MESSAGE);

                //aggiorno liste utenti e le mostro
                User u = SessionManager.getInstance().getSession().get(SessionManager.LoginStatus.ONLINE);
                WishList[] wishListArrayList = CustomerBusiness.getInstance().getAllCustomerWishlists(u);
                if(wishListArrayList==null || wishListArrayList.length == 0){
                    JOptionPane.showMessageDialog(null, "Non hai liste, creane una.", "Errore", JOptionPane.INFORMATION_MESSAGE);
                    break;
                }
                frame.setMainPanel(new WishlistPanel(new BrowseListener(frame), wishListArrayList));
                break;
            }

            case SHOW_FEEDBACK_FORM:{
                IArticlePannels iArticlePannels = (IArticlePannels) frame.getMainPanel();
                int articleId = iArticlePannels.getSelected();
                User u = SessionManager.getInstance().getSession().get(SessionManager.LoginStatus.ONLINE);

                if(!OrderBusiness.getInstance().isAlreadyOrdered(u,articleId)){
                    JOptionPane.showMessageDialog(null, "Devi comprare il prodotto prima di recensirlo.",
                            "Info", JOptionPane.INFORMATION_MESSAGE);
                    break;
                }
                InformationFrame informationFrame = new InformationFrame("Form della Recensione", null);
                FeedbackPanel feedbackPanel = new FeedbackPanel(new BrowseListener(informationFrame), articleId);
                informationFrame.setMainPanel(feedbackPanel);
                break;
            }

            case CREATE_FEEDBACK:{
                FeedbackPanel feedbackPanel = (FeedbackPanel) frame.getMainPanel();
                int articleId = feedbackPanel.getArticleId();
                int vote = feedbackPanel.getRatingComboBox();
                String message = feedbackPanel.getTxtMessage();
                User u = SessionManager.getInstance().getSession().get(SessionManager.LoginStatus.ONLINE);

                Feedback feedback = new Feedback(vote, message, u.getUsername());
                UploadResponse uploadResponse = ArticleBusiness.getInstance().createFeedback(feedback, articleId);
                JOptionPane.showMessageDialog(null, uploadResponse.getMessage(), "Info", JOptionPane.INFORMATION_MESSAGE);
               if(uploadResponse.isDone()) {
                   InformationFrame informationFrame = (InformationFrame) frame;
                   informationFrame.dispatchEvent(new WindowEvent(informationFrame, WindowEvent.WINDOW_CLOSING));
               }
                break;
            }

            case OPEN_FEEDBACK:{
                IArticlePannels articlePannels = (IArticlePannels) frame.getMainPanel();
                Feedback feedback = articlePannels.getSelectedFeedback();
                if(feedback == null){
                    JOptionPane.showMessageDialog(null, "Selezionare una recensione prima.", "Errore", JOptionPane.INFORMATION_MESSAGE);
                    break;
                }
                JPanel jPanel = new JPanel(new GridLayout(3,1));
                jPanel.setBackground(Color.darkGray);
                JLabel lblUser =new JLabel("Username: " + feedback.getUsername());
                lblUser.setForeground(Color.white);
                JLabel lblStar = new JLabel("Stelle: " + feedback.getGraphicalVote());
                lblStar.setForeground(Color.white);
                JLabel lblMessage = new JLabel("Recensione: "+ feedback.getMessage());
                lblMessage.setForeground(Color.white);
                jPanel.add(lblUser);
                jPanel.add(lblStar);
                jPanel.add(lblMessage);
                InformationFrame informationFrame = new InformationFrame("Dettagli recensione",jPanel);
                break;
            }

            case DELETE_ARTICLE:{
                IArticlePannels browsePanel = (IArticlePannels) frame.getMainPanel();
                int articleID = browsePanel.getSelected();
                UploadResponse uploadResponse = ArticleBusiness.getInstance().deleteArticle(articleID);
                JOptionPane.showMessageDialog(null, uploadResponse.getMessage(), "Info", JOptionPane.INFORMATION_MESSAGE);
                //riaggiorno il browsepanel
                User u = SessionManager.getInstance().getSession().get(SessionManager.LoginStatus.ONLINE);
                frame.setMainPanel(new BrowsePanel(new BrowseListener(frame), ArticleBusiness.getInstance().getArticleTable(u)));

                break;
            }

        }

        ((JFrame)frame).invalidate();
        ((JFrame)frame).revalidate();
        ((JFrame)frame).repaint();
    }

}
