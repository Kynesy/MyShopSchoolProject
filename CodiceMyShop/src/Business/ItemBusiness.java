package Business;


import DAO.ArticleDAO;
import DAO.CompositeDAO;
import DAO.ItemDAO;
import DAO.OrderDAO;
import Model.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

public class ItemBusiness {
    private static ItemBusiness instance = new ItemBusiness();
    private ItemDAO itemDAO;
    private ArticleDAO articleDAO;

    private ItemBusiness(){
        itemDAO = ItemDAO.getInstance();
        articleDAO = ArticleDAO.getInstance();
    }

    public static ItemBusiness getInstance(){
        return instance;
    }

    public UploadResponse createItem(Item item){
        UploadResponse uploadResponse = new UploadResponse();
        uploadResponse.setMessage("Prodotto creato con successo.");
        uploadResponse.setDone(true);

        if( item.getName().isBlank() || item.getDescription().isBlank()
                || item.getCityStore()==null || item.getItemVendor()==null
                || item.getFotos().isEmpty()){
            uploadResponse.setMessage("Creazione interrotta: alcuni campi sono vuoti.");
            uploadResponse.setDone(false);
            return uploadResponse;
        }

        if( item.getPrice()<=0 || item.getLine()<=0 || item.getShelf()<=0){
            uploadResponse.setMessage("Creazione interrotta: dati inconsistenti nel campo prezzo o posizione");
            uploadResponse.setDone(false);
            return uploadResponse;
        }

        int id = itemDAO.createItem(item);
        if(id == -1){
            uploadResponse.setMessage("Creazione interrotta: errore nel cariamento dei dati.");
            uploadResponse.setDone(false);
            return uploadResponse;
        }
        //inserisco l'id che ha ricevuto il prodotto dall'autoincrement
        item.setId(id);

        int fotoRow = 0;
        for (File foto : item.getFotos()){
            int r = articleDAO.addFoto(item.getId(), foto);
            fotoRow = fotoRow + r;
        }
        if(fotoRow != item.getFotos().size()){
            uploadResponse.setMessage("Prodotto creato con successo, ma c'è un errore nel caricamento delle foto.");
            return uploadResponse;
        }
        return uploadResponse;
    }

    public UploadResponse createItemProducer(ItemVendor itemProducer){
        UploadResponse uploadResponse = new UploadResponse();
        uploadResponse.setMessage("Fornitore di prodotti correttamente creato.");

        if(itemProducer.getName().isBlank() || itemProducer.getWebsite().isBlank() || itemProducer.getCity().isBlank() || itemProducer.getNation().isBlank()){
            uploadResponse.setMessage("Creazione interrotta: I dati inseriti non sono validi.");
            return uploadResponse;
        }

        if(itemDAO.itemVendorExist(itemProducer.getName())){
            uploadResponse.setMessage("Creazione interrotta: Esiste già un produttore con lo stesso nome.");
            return uploadResponse;
        }

        int row = itemDAO.addItemVendor(itemProducer);
        if(row!=1){
            uploadResponse.setMessage("Errore sconosciuto.");
            return uploadResponse;
        }

        return uploadResponse;
    }

    public Item getItemById(int articleID) {
        Item item = itemDAO.getItem(articleID);
        ItemVendor itemVendor = itemDAO.getItemVendor(articleID);
        item.setItemVendor(itemVendor);
        Category category = ArticleBusiness.getInstance().getCategory(articleID);
        item.setCategory(category);
        ArrayList<File>fotos = articleDAO.getFotosFromDB(articleID);
        item.setFotos(fotos);
        return item;
    }

    public Vendor[] getAllItemVendors() {
        ArrayList<Vendor> arrayList = itemDAO.getAllItemVendors();
        return arrayList.toArray(Vendor[]::new);
    }

    public ArrayList<Item> getAllItemsByStore(String store) {
        ArrayList<Item> items = itemDAO.getAllItemsByStore(store);
        Iterator<Item> iterator = items.iterator();
        while(iterator.hasNext()){
            Item i = iterator.next();
            ItemVendor itemVendor = itemDAO.getItemVendor(i.getId());
            Category category = articleDAO.getCategoryByID(i.getId());
            i.setCategory(category);
            i.setItemVendor(itemVendor);
        }
        return items;
    }

    public UploadResponse modifyItem(Item item) {
        UploadResponse uploadResponse = new UploadResponse();
        uploadResponse.setMessage("Prodotto aggiornato con successo.");
        uploadResponse.setDone(true);

        if( item.getName().isBlank() || item.getDescription().isBlank()
                || item.getCityStore()==null || item.getItemVendor()==null
                || item.getFotos().isEmpty()){
            uploadResponse.setMessage("Aggiornamento interrotto: alcuni campi sono vuoti.");
            uploadResponse.setDone(false);
            return uploadResponse;
        }

        if( item.getPrice()<=0 || item.getLine()<=0 || item.getShelf()<=0){
            uploadResponse.setMessage("Aggiornamento interrotto: dati inconsistenti nel campo prezzo o posizione");
            uploadResponse.setDone(false);
            return uploadResponse;
        }

        int row = itemDAO.modifyItem(item);
        if(row == -1){
            uploadResponse.setMessage("Aggiornamento interrotto: errore nel cariamento dei dati.");
            uploadResponse.setDone(false);
            return uploadResponse;
        }
        //elimino le foto nel db
        articleDAO.deleteAllFotos(item.getId());
        //ricarico le foto nuove
        int fotoRow = 0;
        for (File foto : item.getFotos()){
            int r = articleDAO.addFoto(item.getId(), foto);
            fotoRow = fotoRow + r;
        }
        if(fotoRow != item.getFotos().size()){
            uploadResponse.setMessage("Prodotto aggiornato con successo, ma c'è un errore nel caricamento delle foto.");
            return uploadResponse;
        }
        return uploadResponse;
    }

    public UploadResponse deleteItem(int itemID){
        UploadResponse uploadResponse = new UploadResponse();
        uploadResponse.setMessage("Prodotto eliminato con successo.");

        CompositeDAO.getIstance().removeItemFromAllComposite(itemID);
        itemDAO.deleteItemInfo(itemID);
        articleDAO.deleteServiceFromAllWishlist(itemID);
        articleDAO.deleteServiceFromFeedbacks(itemID);
        articleDAO.deleteAllFotos(itemID);
        OrderDAO.getInstance().deleteArticleFromOrderHistory(itemID);
        OrderDAO.getInstance().deleteArticleFromBooking(itemID);

        itemDAO.deleteItem(itemID);
        int row =articleDAO.deleteArticle(itemID);
        if(row!=1){
            uploadResponse.setMessage("Errore eliminazione prodotto.");
            return uploadResponse;
        }
        return uploadResponse;
    }
}
