package Test.JUnitTestDAO;

import DAO.*;
import DbInterface.DbUser;
import Model.Category;
import Model.Service;
import Model.ServiceVendor;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ServiceDAOTest {
    DbUser dbUser = DbUser.getInstance();
    Service service;
    Category category;
    ServiceVendor serviceVendor;

    @Before
    public void setUp(){
        IArticleDAO articleDAO = ArticleDAO.getInstance();
        IServiceDAO serviceDAO = ServiceDAO.getInstance();
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
    }

    @After
    public void tearDown() {
        IArticleDAO articleDAO = ArticleDAO.getInstance();
        IServiceDAO serviceDAO = ServiceDAO.getInstance();
        serviceDAO.deleteService(service.getId());
        articleDAO.deleteArticle(service.getId());
        articleDAO.removeCategory(category.getId());
        serviceDAO.deleteServiceVendor("Compagnia 1");
    }

    @Test
    public void serviceExistTest(){
        IServiceDAO serviceDAO = ServiceDAO.getInstance();
        boolean exist = serviceDAO.serviceExist(service.getId());
        Assert.assertTrue(exist);
    }

    @Test
    public void getServiceVendorTest(){
        IServiceDAO serviceDAO = ServiceDAO.getInstance();
        ServiceVendor newServiceVendor = serviceDAO.getServiceVendor(serviceVendor.getName());

        Assert.assertEquals(serviceVendor.toString(), newServiceVendor.toString());
    }

    @Test
    public void getServiceTest(){
        IServiceDAO serviceDAO = ServiceDAO.getInstance();
        Service newService = serviceDAO.getService(service.getId());

        Assert.assertEquals(service.toString(), newService.toString());
    }

    @Test
    public void serviceVendorExistTest(){
        IServiceDAO serviceDAO = ServiceDAO.getInstance();
        boolean exist = serviceDAO.serviceVendorExist(serviceVendor.getName());

        Assert.assertTrue(exist);
    }

    @Test
    public void modifyServiceTest(){
        IServiceDAO serviceDAO = ServiceDAO.getInstance();
        serviceDAO.modifyService(service);

        Service newService = serviceDAO.getService(service.getId());
        Assert.assertEquals(service.toString(), newService.toString());
    }
}
