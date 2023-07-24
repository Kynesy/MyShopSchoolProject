package Business;

import DAO.ArticleDAO;
import DAO.CompositeDAO;
import DAO.ItemDAO;
import Model.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CompositeBusiness {
    private static CompositeBusiness instance = new CompositeBusiness();
    private ArticleDAO articleDAO;
    private ItemDAO itemDAO;
    private CompositeDAO compositeDAO;

    private CompositeBusiness(){
        articleDAO = ArticleDAO.getInstance();
        itemDAO = ItemDAO.getInstance();
        compositeDAO = CompositeDAO.getIstance();
    }

    public static CompositeBusiness getInstance(){
        return instance;
    }

    public UploadResponse uploadCompositeItemlist(CompositeItem compositeItem){
        //creo il prodotto base
        UploadResponse uploadResponse = ItemBusiness.getInstance().createItem(compositeItem);
        //aggiungo singolarmente i vari prodotti al composite
        if(uploadResponse.isDone()) {
            List<ItemInterface> subItemList = compositeItem.getSubItemList();
            Iterator<ItemInterface> iterator = subItemList.iterator();
            while (iterator.hasNext()) {
                ItemInterface genericItem = iterator.next();
                int r1 = compositeDAO.addItemToComposite(genericItem.getID(), compositeItem.getID());
                if (r1 != 1) {
                    uploadResponse.setMessage(uploadResponse.getMessage() + "\nErrore nella creazione: prodotto [" + genericItem.getID() + "] non aggiunto al composite.");
                }
            }
        }
        return uploadResponse;
    }

    public CompositeItem getCompositeByID(int articleID) {
        CompositeItem compositeItem = compositeDAO.getCompositeByID(articleID);
        ItemVendor itemVendor = itemDAO.getItemVendor(articleID);
        compositeItem.setItemVendor(itemVendor);
        compositeItem.setItemVendor(itemDAO.getItemVendor(articleID));
        compositeItem.setCategory(ArticleBusiness.getInstance().getCategory(articleID));
        List<Integer> itemsID = compositeDAO.getCompositeSubitemsID(articleID);
        List<ItemInterface> subItemsList = new ArrayList<>();
        for(Integer id : itemsID){
            ItemInterface itemInterface;
            if (compositeDAO.isComposite(id)){
                itemInterface = compositeDAO.getCompositeByID(id);
            }else {
                itemInterface = ItemBusiness.getInstance().getItemById(id);
            }
            subItemsList.add(itemInterface);
        }
        compositeItem.setSubItemList(subItemsList);
        compositeItem.setFotos(articleDAO.getFotosFromDB(articleID));
        return compositeItem;
    }

    public UploadResponse modifyComposite(CompositeItem compositeItem) {
        //modifico il prodotto base
        UploadResponse uploadResponse = ItemBusiness.getInstance().modifyItem(compositeItem);
        //svuoto il composite dai sottoprodotti
        compositeDAO.clearComposite(compositeItem.getID());

        //aggiungo singolarmente i vari prodotti al composite
        List<ItemInterface> subItemList = compositeItem.getSubItemList();
        Iterator<ItemInterface> iterator = subItemList.iterator();
        while (iterator.hasNext()){
            ItemInterface genericItem = iterator.next();
            int r1 = compositeDAO.addItemToComposite(genericItem.getID(), compositeItem.getID());
            if(r1 != 1){
                uploadResponse.setMessage(uploadResponse.getMessage() + "\nErrore nella creazione: prodotto ["+ genericItem.getID() +"] non aggiunto al composite.");
            }
        }
        return uploadResponse;
    }

    public ArrayList<Item> removeCompositeFromArray(ArrayList<Item> items){
        ArrayList<Item> result = new ArrayList<>();
        //rimuovo il prodotto composito dalla lista, per evitare loop infiniti
        Iterator<Item> iterator = items.iterator();
        while (iterator.hasNext()){
            Item tmpItem = iterator.next();
            if(!compositeDAO.isComposite(tmpItem.getID())){
                result.add(tmpItem);
            }
        }
        return result;
    }
}
