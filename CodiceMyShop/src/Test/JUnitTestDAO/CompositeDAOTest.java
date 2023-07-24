package Test.JUnitTestDAO;

import DAO.*;
import DbInterface.DbUser;
import Model.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class CompositeDAOTest {
    DbUser dbUser = DbUser.getInstance();
    CompositeItem compositeItem;
    Category category;
    ItemVendor itemVendor;
    Store store;
    Item i1;
    Item i2;

    @Before
    public void setUp(){
        IArticleDAO articleDAO = ArticleDAO.getInstance();
        IItemDAO itemDAO = ItemDAO.getInstance();
        IAdministratorDAO administratorDAO = AdministratorDAO.getInstance();
        ICompositeDAO compositeDAO = CompositeDAO.getIstance();

        store = new Store();
        store.setCity("Palermo");
        administratorDAO.addNewStore(store);

        itemVendor = new ItemVendor();
        itemVendor.setName("Compagnia 1");
        itemVendor.setCity("Roma");
        itemVendor.setNation("Italia");
        itemVendor.setWebsite("http:sito");
        itemDAO.addItemVendor(itemVendor);

        category = new Category();
        category.setName("Casa");
        int catID = articleDAO.addCategory(category);
        category.setId(catID);

        ArrayList<ItemInterface> subItems = new ArrayList<>();
        i1 = new Item();
        i1.setName("i1");
        i1.setDescription("Descrizione di i1");
        i1.setPrice(22.50);
        i1.setPosition(3,4);
        i1.setCityStore(store.getCity());
        i1.setItemVendor(itemVendor);
        i1.setCategory(category);
        int i1ID = itemDAO.createItem(i1);
        i1.setId(i1ID);
        subItems.add(i1);
        i2 = new Item();
        i2.setName("i2");
        i2.setDescription("Descrizione di i2");
        i2.setPrice(50.50);
        i2.setPosition(2,44);
        i2.setCityStore(store.getCity());
        i2.setItemVendor(itemVendor);
        i2.setCategory(category);
        int i2ID = itemDAO.createItem(i2);
        i2.setId(i2ID);
        subItems.add(i2);

        compositeItem = new CompositeItem();
        compositeItem.setName("bundle");
        compositeItem.setDescription("Descrizione del bundle");
        compositeItem.setPosition(3,4);
        compositeItem.setItemVendor(itemVendor);
        compositeItem.setCategory(category);
        compositeItem.setSubItemList(subItems);
        compositeItem.setCityStore(store.getCity());

        int itemID = itemDAO.createItem(compositeItem);
        compositeItem.setId(itemID);
        for(ItemInterface i : compositeItem.getSubItemList()){
            compositeDAO.addItemToComposite(i.getID(), compositeItem.getID());
        }
    }

    @After
    public void tearDown() {
        IArticleDAO articleDAO = ArticleDAO.getInstance();
        IItemDAO itemDAO = ItemDAO.getInstance();
        IAdministratorDAO administratorDAO = AdministratorDAO.getInstance();
        ICompositeDAO compositeDAO = CompositeDAO.getIstance();

        compositeDAO.clearComposite(compositeItem.getID());
        itemDAO.deleteItemInfo(compositeItem.getID());
        itemDAO.deleteItem(compositeItem.getId());

        itemDAO.deleteItemInfo(i1.getID());
        itemDAO.deleteItem(i1.getId());
        articleDAO.deleteArticle(i1.getId());
        itemDAO.deleteItemInfo(i2.getID());
        itemDAO.deleteItem(i2.getId());
        articleDAO.deleteArticle(i2.getId());

        articleDAO.deleteArticle(compositeItem.getId());
        articleDAO.removeCategory(category.getId());
        itemDAO.removeItemVendor(itemVendor.getName());
        administratorDAO.removeStore(store.getCity());
    }

    @Test
    public void compositeExistTest(){
        ICompositeDAO compositeDAO = CompositeDAO.getIstance();
        boolean exist = compositeDAO.isComposite(compositeItem.getId());
        Assert.assertTrue(exist);
    }

    @Test
    public void getCompositeTest(){
        ICompositeDAO compositeDAO = CompositeDAO.getIstance();
        CompositeItem newCompositeItem = compositeDAO.getCompositeByID(compositeItem.getID());

        Assert.assertEquals(compositeItem.toString(), newCompositeItem.toString());
    }

    @Test
    public void getSubItemsTest(){
        ICompositeDAO compositeDAO = CompositeDAO.getIstance();
        List<Integer> subItemsId = compositeDAO.getCompositeSubitemsID(compositeItem.getID());

        List<ItemInterface> originalSubItemList = compositeItem.getSubItemList();
        List<Integer> originalIDs = new ArrayList<>();
        for( ItemInterface tmpItem: originalSubItemList){
            originalIDs.add(tmpItem.getID());
        }
        Assert.assertEquals(originalIDs.toString(), subItemsId.toString());
    }
}
