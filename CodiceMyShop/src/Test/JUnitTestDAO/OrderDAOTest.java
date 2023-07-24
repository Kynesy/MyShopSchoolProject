package Test.JUnitTestDAO;

import DAO.*;
import DbInterface.DbUser;
import Model.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class OrderDAOTest {
    DbUser dbUser = DbUser.getInstance();
    User user;
    Item item;
    Category category;
    ItemVendor itemVendor;
    Store store;
    Prenotation prenotation;
    Order order;

    @Before
    public void setUp(){
        IUserDAO userDAO = UserDAO.getInstance();
        IOrderDAO orderDAO = OrderDAO.getInstance();

        user = new User();
        user.setEmail("cezar99.rp@gmail.com");
        user.setName("Cesare");
        user.setSurname("Culcea");
        user.setPassword("password");
        user.setUsername("cesare");
        userDAO.addNewUser(user);

        item = new Item();
        item.setName("sedia");
        item.setDescription("Descrizione della sedia");
        item.setPrice(22.50);
        item.setPosition(3,4);
        category = new Category();
        category.setName("Casa");
        int catID = ArticleDAO.getInstance().addCategory(category);
        category.setId(catID);
        item.setCategory(category);
        itemVendor = new ItemVendor();
        itemVendor.setName("Compagnia 1");
        itemVendor.setCity("Roma");
        itemVendor.setNation("Italia");
        itemVendor.setWebsite("http:sito");
        item.setItemVendor(itemVendor);
        ItemDAO.getInstance().addItemVendor(itemVendor);
        store = new Store();
        store.setCity("Palermo");
        AdministratorDAO.getInstance().addNewStore(store);
        item.setCityStore(store.getCity());
        int itemID = ItemDAO.getInstance().createItem(item);
        item.setId(itemID);

        order = new Order("RandomID", user.getUsername(), 0);
        orderDAO.uploadOrder(order.getUsername(), order.getOrderID(), order.getPaidStatus());

        orderDAO.addToOrderHistory(user.getUsername(), item.getId());

        prenotation = new Prenotation(user.getUsername(), item.getID(), 20);
        int bookingID = orderDAO.createBooking(prenotation.getUsername(), prenotation.getArticleID(), prenotation.getQuantity());
        prenotation.setID(bookingID);

    }

    @After
    public void tearDown() {
        IUserDAO userDAO = UserDAO.getInstance();
        IOrderDAO orderDAO = OrderDAO.getInstance();

        orderDAO.deleteOrder(order.getOrderID());
        orderDAO.removeUserHistory(user.getUsername());
        orderDAO.deleteBooking(prenotation.getID());

        userDAO.removeByUsername(user.getUsername());

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
    public void isOrderedTest(){
        IOrderDAO orderDAO = OrderDAO.getInstance();
        boolean isOrdered = orderDAO.isAlreadyOrdered(user.getUsername(), item.getId());

        Assert.assertTrue(isOrdered);
    }

    @Test
    public void updatePaidStatusTest(){
        IOrderDAO orderDAO = OrderDAO.getInstance();
        int row = orderDAO.updatePaidStatus("RandomID", 1);

        Assert.assertEquals(1, row);
    }

    @Test
    public void getAllOrdersTest(){
        IOrderDAO orderDAO = OrderDAO.getInstance();
        Order newOrder = orderDAO.getAllOrders().get(0);

        Assert.assertEquals(order.toString(), newOrder.toString());
    }


    @Test
    public void getAllPrenotationsTest(){
        IOrderDAO orderDAO = OrderDAO.getInstance();
        Prenotation newPrenotation = orderDAO.getAllPrenotations().get(0);

        Assert.assertEquals(prenotation.toString(), newPrenotation.toString());
    }

}
