package Test.JUnitTestDAO;

import DAO.*;
import DbInterface.DbUser;
import Model.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ItemDAOTest {
    DbUser dbUser = DbUser.getInstance();
    Item item;
    Category category;
    ItemVendor itemVendor;
    Store store;

    @Before
    public void setUp(){
        IArticleDAO articleDAO = ArticleDAO.getInstance();
        IItemDAO itemDAO = ItemDAO.getInstance();
        IAdministratorDAO administratorDAO = AdministratorDAO.getInstance();

        item = new Item();
        item.setName("sedia");
        item.setDescription("Descrizione della sedia");
        item.setPrice(22.50);
        item.setPosition(3,4);

        category = new Category();
        category.setName("Casa");
        int catID = articleDAO.addCategory(category);
        category.setId(catID);
        item.setCategory(category);

        itemVendor = new ItemVendor();
        itemVendor.setName("Compagnia 1");
        itemVendor.setCity("Roma");
        itemVendor.setNation("Italia");
        itemVendor.setWebsite("http:sito");

        item.setItemVendor(itemVendor);
        itemDAO.addItemVendor(itemVendor);

        store = new Store();
        store.setCity("Palermo");
        administratorDAO.addNewStore(store);

        item.setCityStore(store.getCity());
        int itemID = itemDAO.createItem(item);
        item.setId(itemID);
    }

    @After
    public void tearDown() {
        IArticleDAO articleDAO = ArticleDAO.getInstance();
        IItemDAO itemDAO = ItemDAO.getInstance();
        IAdministratorDAO administratorDAO = AdministratorDAO.getInstance();
        itemDAO.deleteItemInfo(item.getID());
        itemDAO.deleteItem(item.getId());
        articleDAO.deleteArticle(item.getId());
        articleDAO.removeCategory(category.getId());
        itemDAO.removeItemVendor(itemVendor.getName());
        administratorDAO.removeStore(store.getCity());
    }

    @Test
    public void itemExistTest(){
        IItemDAO itemDAO = ItemDAO.getInstance();
        boolean exist = itemDAO.itemExist(item.getId());
        Assert.assertTrue(exist);
    }

    @Test
    public void getItemVendorTest(){
        IItemDAO itemDAO = ItemDAO.getInstance();
        ItemVendor newItemVendor = itemDAO.getItemVendor(itemVendor.getName());

        Assert.assertEquals(itemVendor.toString(), newItemVendor.toString());
    }

    @Test
    public void getItemTest(){
        IItemDAO itemDAO = ItemDAO.getInstance();
        Item newItem = itemDAO.getItem(item.getId());

        Assert.assertEquals(item.toString(), newItem.toString());
    }

    @Test
    public void itemVendorExistTest(){
        IItemDAO itemDAO = ItemDAO.getInstance();
        boolean exist = itemDAO.itemVendorExist(itemVendor.getName());

        Assert.assertTrue(exist);
    }

    @Test
    public void modifyItemTest(){
        IItemDAO itemDAO = ItemDAO.getInstance();
        itemDAO.modifyItem(item);

        Item newItem = itemDAO.getItem(item.getId());
        Assert.assertEquals(item.toString(), newItem.toString());
    }

    @Test
    public void getAllItemsByStoreTest(){
        IItemDAO itemDAO = ItemDAO.getInstance();
        Item newItem = itemDAO.getAllItemsByStore(store.getCity()).get(0);

        Assert.assertEquals(item.toString(), newItem.toString());
    }


}
