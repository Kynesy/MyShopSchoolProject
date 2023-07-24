package Test.JUnitTestDAO;

import DAO.*;
import DbInterface.DbUser;
import Model.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.management.MemoryNotificationInfo;
import java.util.ArrayList;

public class ManagerDAOTest {
    DbUser dbUser = DbUser.getInstance();
    Manager manager;
    Store store;
    Item item;
    ItemVendor itemVendor;
    Category category;
    User user;


    @Before
    public void setUp(){
        IAdministratorDAO administratorDAO = AdministratorDAO.getInstance();
        IItemDAO itemDAO = ItemDAO.getInstance();
        IArticleDAO articleDAO = ArticleDAO.getInstance();

        store = new Store();
        store.setCity("Roma");
        administratorDAO.addNewStore(store);

        manager = new Manager();
        manager.setEmail("cezar99.rp@gmail.com");
        manager.setName("Cesare");
        manager.setSurname("Culcea");
        manager.setPassword("password");
        manager.setUsername("cesare");
        manager.setStoreCity(store.getCity());

        user = new User();
        user.setEmail("cezar99.rp@gmail.com");
        user.setName("Cesare");
        user.setSurname("Culcea");
        user.setPassword("password");
        user.setUsername("tmpUser");

        administratorDAO.addManager(manager);
        UserDAO.getInstance().addNewUser(user);

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

        item.setAvaiability(10);

        item.setCityStore(store.getCity());
        int itemID = itemDAO.createItem(item);
        item.setId(itemID);

    }

    @After
    public void tearDown() {
        IUserDAO userDAO = UserDAO.getInstance();
        IManagerDAO managerDAO = ManagerDAO.getInstance();
        IAdministratorDAO administratorDAO = AdministratorDAO.getInstance();
        IItemDAO itemDAO = ItemDAO.getInstance();
        IArticleDAO articleDAO = ArticleDAO.getInstance();

        managerDAO.removeManager(manager.getUsername());
        userDAO.removeByUsername(manager.getUsername());
        managerDAO.activateByUsername(user.getUsername());
        userDAO.removeByUsername(user.getUsername());

        itemDAO.deleteItemInfo(item.getID());
        itemDAO.deleteItem(item.getId());
        articleDAO.deleteArticle(item.getId());
        articleDAO.removeCategory(category.getId());
        itemDAO.removeItemVendor(itemVendor.getName());
        administratorDAO.removeStore(store.getCity());
    }

    @Test
    public void changeItemQuantityTest(){
        IManagerDAO managerDAO = ManagerDAO.getInstance();

        managerDAO.changeQuantity(item.getID(), item.getAvaiability());

        Item tmpI = ItemDAO.getInstance().getItem(item.getID());
        Assert.assertEquals(item.getAvaiability(), tmpI.getAvaiability());
    }

    @Test
    public void disableUser(){
        IManagerDAO managerDAO = ManagerDAO.getInstance();
        managerDAO.disableByUsername(user.getUsername());

        boolean isDisabled = managerDAO.isDisabled(user.getUsername());
        Assert.assertTrue(isDisabled);
    }

    @Test
    public void activateUser(){
        IManagerDAO managerDAO = ManagerDAO.getInstance();
        managerDAO.disableByUsername(user.getUsername());
        managerDAO.activateByUsername(user.getUsername());
        boolean isDisabled = managerDAO.isDisabled(user.getUsername());
        Assert.assertFalse(isDisabled);
    }

}
