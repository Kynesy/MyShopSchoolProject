package Test.JUnitTestDAO;

import DAO.*;
import DbInterface.DbUser;
import Model.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class CustomerDAOTest {
    DbUser dbUser = DbUser.getInstance();
    Customer customer;
    Store store;
    Item item;
    WishList wishList;
    ItemVendor itemVendor;
    Category category;


    @Before
    public void setUp(){
        IAdministratorDAO administratorDAO = AdministratorDAO.getInstance();
        ICustomerDAO customerDAO = CustomerDAO.getInstance();
        IItemDAO itemDAO = ItemDAO.getInstance();
        IArticleDAO articleDAO = ArticleDAO.getInstance();

        store = new Store();
        store.setCity("Roma");
        administratorDAO.addNewStore(store);

        customer = new Customer();
        customer.setEmail("cezar99.rp@gmail.com");
        customer.setName("Cesare");
        customer.setSurname("Culcea");
        customer.setPassword("password");
        customer.setUsername("cesare");
        customer.setStoreCity(store.getCity());
        customer.setBirthYear(1999);
        customer.setAddress("Indirizzo");
        customer.setPhoneNumber(1234567890);

        customerDAO.addNewCustomer(customer);

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

        item.setCityStore(store.getCity());
        int itemID = itemDAO.createItem(item);
        item.setId(itemID);
        item.setWishlistQuantity(10);

        wishList = new WishList();
        wishList.setName("Lista dei desideri");
        wishList.setUsername(customer.getUsername());
        ArrayList<Item> arrayList = new ArrayList<>();
        arrayList.add(item);
        wishList.setItems(arrayList);
        wishList.setServices(new ArrayList<>());

        int idwishlist =customerDAO.createWishList(wishList.getUsername(), wishList.getName());
        wishList.setId(idwishlist);

        customerDAO.addArticleToWishList(item.getId(), wishList);
        customerDAO.updateItemQuantity(wishList.getId(), item.getID(), item.getWishlistQuantity());

    }

    @After
    public void tearDown() {
        IUserDAO userDAO = UserDAO.getInstance();
        ICustomerDAO customerDAO = CustomerDAO.getInstance();
        IAdministratorDAO administratorDAO = AdministratorDAO.getInstance();
        IItemDAO itemDAO = ItemDAO.getInstance();
        IArticleDAO articleDAO = ArticleDAO.getInstance();

        customerDAO.resetWishlist(wishList.getId());
        customerDAO.deleteUserWishlist(wishList.getId());

        customerDAO.removeCustomer(customer.getUsername());
        userDAO.removeByUsername(customer.getUsername());

        itemDAO.deleteItemInfo(item.getID());
        itemDAO.deleteItem(item.getId());
        articleDAO.deleteArticle(item.getId());
        articleDAO.removeCategory(category.getId());
        itemDAO.removeItemVendor(itemVendor.getName());
        administratorDAO.removeStore(store.getCity());


    }

    @Test
    public void modifyCustomerTest(){
        IUserDAO userDAO = UserDAO.getInstance();
        ICustomerDAO customerDAO = CustomerDAO.getInstance();

        customerDAO.updateCustomerData(customer);
        userDAO.updateUserData(customer);

        Customer newCustomer = customerDAO.getByUsername(customer.getUsername());
        Assert.assertEquals( customer.toString(), newCustomer.toString());
    }

    @Test
    public void getCustomerWishlist(){
        ICustomerDAO customerDAO = CustomerDAO.getInstance();
        WishList newWishlist = customerDAO.getAllCustomerWishLists(customer.getUsername()).get(0);

        Assert.assertEquals(wishList.getName(), newWishlist.getName());
    }

    @Test
    public void wishListContainItemTest(){
        ICustomerDAO customerDAO = CustomerDAO.getInstance();
        int quantity = customerDAO.wishlistContainsItem(wishList.getId(), item.getID());
        Assert.assertEquals(item.getWishlistQuantity(), quantity);
    }

    @Test
    public void updateItemQuantityTest(){
        ICustomerDAO customerDAO = CustomerDAO.getInstance();
        customerDAO.updateItemQuantity(wishList.getId(), item.getID(), 20);
        int quantity = customerDAO.wishlistContainsItem(wishList.getId(), item.getID());

        Assert.assertEquals(20, quantity);
    }


}
