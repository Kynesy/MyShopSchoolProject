package Test.JUnitTestDAO;

import DAO.*;
import DbInterface.DbUser;
import Model.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ArticleDAOTest {
    DbUser dbUser = DbUser.getInstance();
    Service service;
    Category category;
    ServiceVendor serviceVendor;
    Feedback feedback;
    User user;

    @Before
    public void setUp(){
        IArticleDAO articleDAO = ArticleDAO.getInstance();
        IServiceDAO serviceDAO = ServiceDAO.getInstance();
        IUserDAO userDAO = UserDAO.getInstance();

        service = new Service();

        category = new Category();
        service.setName("trasporto");
        service.setDescription("Descrizione della servizio");
        service.setPrice(42.50);
        category.setName("Casa");
        int catID = articleDAO.addCategory(category);
        category.setId(catID);
        service.setCategory(category);

        serviceVendor = new ServiceVendor();
        serviceVendor.setName("Compagnia 1");
        serviceVendor.setCity("Roma");
        serviceVendor.setNation("Italia");
        serviceVendor.setWebsite("http:sito");

        service.setVendorName(serviceVendor.getName());
        serviceDAO.addServiceVendor(serviceVendor);

        int serviceID = serviceDAO.createService(service);
        service.setId(serviceID);

        user = new User();
        user.setEmail("cezar99.rp@gmail.com");
        user.setName("Cesare");
        user.setSurname("Culcea");
        user.setPassword("password");
        user.setUsername("cesare");
        userDAO.addNewUser(user);

        feedback = new Feedback(3, "Messaggio del feedback", user.getUsername());
        articleDAO.createFeedback(feedback.getIndex(), feedback.getMessage(), service.getId(), user.getUsername());
    }

    @After
    public void tearDown() {
        IArticleDAO articleDAO = ArticleDAO.getInstance();
        IServiceDAO serviceDAO = ServiceDAO.getInstance();
        IUserDAO userDAO = UserDAO.getInstance();

        articleDAO.removeAllUserFeedbacks(user.getUsername());
        userDAO.removeByUsername(user.getUsername());

        serviceDAO.deleteService(service.getId());
        articleDAO.deleteArticle(service.getId());
        articleDAO.removeCategory(category.getId());
        serviceDAO.deleteServiceVendor("Compagnia 1");

    }


    @Test
    public void articleExistTest(){
        IArticleDAO articleDAO = ArticleDAO.getInstance();
        boolean exist = articleDAO.articleExist(service.getId());
        Assert.assertTrue(exist);
    }

    @Test
    public void getCategoryByArticleTest(){
        IArticleDAO articleDAO = ArticleDAO.getInstance();
        Category newCategory = articleDAO.getCategoryByID(service.getId());

        Assert.assertEquals(category.getName(), newCategory.getName());
    }

    @Test
    public void getCategoryByIdCategoryTest(){
        IArticleDAO articleDAO = ArticleDAO.getInstance();
        Category newCategory = articleDAO.getCategory(category.getId());

        Assert.assertEquals(category.getName(), newCategory.getName());
    }

    @Test
    public void getFeedbackTest(){
        IArticleDAO articleDAO = ArticleDAO.getInstance();
        Feedback newFeedback = articleDAO.getAllFeedbackByArticleId(service.getId()).get(0);

        Assert.assertEquals(feedback.toString(), newFeedback.toString());
    }
}
