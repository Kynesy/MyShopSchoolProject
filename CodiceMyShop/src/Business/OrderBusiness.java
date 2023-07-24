package Business;

import Business.PDFBridge.PDFGenerator;
import Business.PDFBridge.PDFAbstractGen;
import Business.PDFBridge.ITextPDFApi;
import DAO.CustomerDAO;
import DAO.ItemDAO;
import DAO.ManagerDAO;
import DAO.OrderDAO;
import Model.*;
import com.itextpdf.text.DocumentException;

import javax.print.attribute.standard.Sides;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class OrderBusiness {
    private static OrderBusiness instance = new OrderBusiness();
    private OrderDAO orderDAO = OrderDAO.getInstance();

    public static OrderBusiness getInstance() {
        return instance;
    }

    public double calculateSubtotal(WishList wish) {
        double subtotal = 0;
        for (int i = 0; i < wish.getItems().size(); i++)
        {
            Item item = wish.getItems().get(i);
            int quantity = item.getWishlistQuantity();
            subtotal += item.getPrice()*quantity;
        }

        for (int i = 0; i < wish.getServices().size(); i++)
        {
            subtotal += wish.getServices().get(i).getPrice();
        }
        return subtotal;
    }

    private File createOrderPDF(User user, WishList wishList, String orderID) throws DocumentException, FileNotFoundException {
        PDFAbstractGen fileGenerator = new PDFGenerator(new ITextPDFApi(), user, wishList, orderID);
        return fileGenerator.createOrder();
    }

    private File createReceiptPDF(String username, String orderID) throws DocumentException, FileNotFoundException {
        PDFAbstractGen fileGenerator = new PDFGenerator(new ITextPDFApi(), username, orderID);
        return fileGenerator.createReceipt();
    }

    public UploadResponse generateOrder(User u, WishList wishlist) throws DocumentException, FileNotFoundException {
        String orderID = generateOrderID();//creo id
        UploadResponse orderResponse = new UploadResponse();
        orderResponse.setMessage("Ordine creato correttamente. ");

        File file = createOrderPDF(u, wishlist,orderID); // creo il file con la lista d'acquisto
        String message = "Gentile "+u.getUsername()+", \n" +
                "Il suo ordine è stato avviato con successo. Il suo ID di procedura è ["+orderID+"].\n" +
                "Per eventuali problemi contatti l'amministrazione riferendo il suo ID di procedura.\n" +
                "In allegato trova il pdf relativo all'ordine. \n\n" +
                "Lo staff di MyShop.";
        UploadResponse mailResponse = ManagerBusiness.getInstance().sendMail(message,
                "MyShop: ORDINE ["+orderID+"] AVVIATO", file, u.getEmail() ); //invio la mail
        if(!mailResponse.isDone()){
            orderResponse.setMessage("Ordine interrotto: " +  mailResponse.getMessage());
            file.delete();
            return orderResponse;
        }

        int row = orderDAO.uploadOrder(u.getUsername(), orderID, 0); //carico il record dell'ordine nella tabella per il manager
        if(row!=1){
            orderResponse.setMessage("Errore nella creazione ordine.");
            return orderResponse;
        }

        Iterator<Service> serviceIterator = wishlist.getServices().iterator();
        while (serviceIterator.hasNext()){
            Service tmpService = serviceIterator.next();
            if(!orderDAO.isAlreadyOrdered(u.getUsername(), tmpService.getId())){//aggiungo il servizio nello storico utente
                orderDAO.addToOrderHistory(u.getUsername(), tmpService.getId());
            }
        }


        //cambio le quantità dei prodotti acquistati, togliendole dal magazzino
        Iterator<Item> itemIterator = wishlist.getItems().iterator();
        StringBuilder bookingMessage = new StringBuilder("Gentile " + u.getUsername() + ", \n I seguenti articoli da lei ordinati non sono disponibili.\n" +
                "E' stata creata una prenotazione e le arriveranno appena disponibili.\n Di seguito la lista:\n ");
        boolean needBooking = false;
        while (itemIterator.hasNext()){
            Item tmpItem = itemIterator.next();
            if(!orderDAO.isAlreadyOrdered(u.getUsername(), tmpItem.getID())){//aggiungo il prodotto nello storico utente
                orderDAO.addToOrderHistory(u.getUsername(), tmpItem.getID());
            }
            int storeQuantity = tmpItem.getAvaiability();
            int wishlistQuantity = tmpItem.getWishlistQuantity();
            if(wishlistQuantity>storeQuantity){//verifico i pezzi chiesti non superino l'offerta
                int row1 = ManagerDAO.getInstance().changeQuantity(tmpItem.getId(), 0);//se lo fanno tolgo tutto dal magazzino
                if(row1!=1){
                    orderResponse.setMessage(orderResponse.getMessage()+ "\n Errori nel cambio quantità del prodotto ["+tmpItem.getId()+"]");
                }
                int row2 = orderDAO.createBooking(u.getUsername(), tmpItem.getId(),wishlistQuantity-storeQuantity);//creo la prenotazione per i pezzi mancanti
                if(row2==0){
                    orderResponse.setMessage(orderResponse.getMessage()+ "\n Errori nella prenotazione del prodotto ["+tmpItem.getId()+"]");
                }
                bookingMessage.append(tmpItem).append(" Quantità prenotata: ").append(wishlistQuantity - storeQuantity).append("\n");
                needBooking = true;
            }else {//altrimenti aggiorno la nuova disponibilità in magazzino
                ManagerDAO.getInstance().changeQuantity(tmpItem.getId(), storeQuantity-wishlistQuantity);
            }
        }
        if(needBooking){
            orderResponse.setMessage(orderResponse.getMessage() + bookingMessage);
        }
        return orderResponse;
    }

    public UploadResponse generateReceipt(String username,String orderID) throws DocumentException, FileNotFoundException {
        UploadResponse uploadResponse = new UploadResponse();//pago l'ordine nel db
        uploadResponse.setMessage("Ordine pagato con successo. \n");
        int row = orderDAO.updatePaidStatus(orderID, 1);
        if(row!=1){
            uploadResponse.setMessage("Errore: impossibile pagare l'ordine.");
            return uploadResponse;
        }
        //creo il file pdf
        File file = createReceiptPDF(username, orderID);
        User u = CustomerDAO.getInstance().getByUsername(username);
        //invio il file per mail
        UploadResponse mailResponse = ManagerBusiness.getInstance().sendMail("Gentile ["+u.getUsername()+"], " +
                        "\n Il suo ordine ["+orderID+"] si è concluso con successo e risulta pagato.\n" +
                        "In allegato la sua ricevuta.\n\n " +
                        "Lo staff di MyShop",
                "Ricevuta ordine - " + orderID, file, u.getEmail());

        uploadResponse.setMessage(uploadResponse.getMessage() + mailResponse.getMessage());

        return uploadResponse;
    }

    public boolean isAlreadyOrdered(User u, int articleID){
        return orderDAO.isAlreadyOrdered(u.getUsername(),articleID);
    }

    public UploadResponse deleteOrder(Order order){
        UploadResponse uploadResponse = new UploadResponse();
        uploadResponse.setMessage("L'ordine ["+order.getOrderID()+"] è stato eliminato con successo.");
        if(order == null){
            uploadResponse.setMessage("Selezionare un ordine prima.");
            return uploadResponse;
        }
        int row = orderDAO.deleteOrder(order.getOrderID());
        if(row != 1){
            uploadResponse.setMessage("Errore: L'ordine ["+order.getOrderID()+"] non è stato eliminato");
            return uploadResponse;
        }

        return uploadResponse;
    }

    private String generateOrderID() {
        Random random = new Random();
        String alphabet = "QWERTYUIOPLKJHGFDSAZXCVBNM";
        String numbers = "1234567890";
        String result = null;
        boolean done = false;
        while (!done) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < 10; i++) {
                if (random.nextBoolean()) {
                    stringBuilder.append(alphabet.charAt(random.nextInt(26)));
                } else {
                    stringBuilder.append(numbers.charAt(random.nextInt(10)));
                }
            }

            done = !orderDAO.orderExist(stringBuilder.toString());
            if(done){
                result = stringBuilder.toString();
            }
        }
        return result;
    }

    public ArrayList<Prenotation> getAllPrenotations(){
        return orderDAO.getAllPrenotations();
    }

    public UploadResponse approvePrenotation(Prenotation prenotation){
        UploadResponse uploadResponse = new UploadResponse();
        if(prenotation==null){
            uploadResponse.setMessage("Selezionare una prenotazione prima.");
            return uploadResponse;
        }
        uploadResponse.setMessage("Prenotazione [ID:"+prenotation.getID()+"] approvata.\n");

        int row = orderDAO.deleteBooking(prenotation.getID());
        if(row!=1){
            uploadResponse.setMessage("Errore: prenotazione non approvata.");
            return uploadResponse;
        }
        User u = CustomerDAO.getInstance().getByUsername(prenotation.getUsername());
        String message = "Gentile ["+u.getUsername()+"],\n" +
                "La informiamo che la sua prenotazione del prodotto [ID: "+prenotation.getArticleID()+"] " +
                "[Pezzi richiesti:"+prenotation.getQuantity()+"] è stata approvata e i pezzi richiesti " +
                "sono pronti per il ritiro.\n\n Lo staff di MyShop";
        UploadResponse mailResponse = ManagerBusiness.getInstance().sendMail(message, "MyShop: Prenotazione approvata",
                u.getEmail());
        uploadResponse.setMessage(uploadResponse.getMessage() + mailResponse.getMessage());
        return uploadResponse;
    }

    public ArrayList<Order> getAllOrders() {
        return orderDAO.getAllOrders();
    }
}
