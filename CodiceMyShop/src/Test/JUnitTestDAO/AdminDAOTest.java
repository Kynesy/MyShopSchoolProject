package Test.JUnitTestDAO;

import DAO.*;
import DbInterface.DbUser;
import Model.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AdminDAOTest {
    DbUser dbUser = DbUser.getInstance();
    Administrator administrator;
    Store store;
    Manager manager;

    @Before
    public void setUp(){
        IAdministratorDAO administratorDAO = AdministratorDAO.getInstance();

        store = new Store();
        store.setCity("Roma");
        administratorDAO.addNewStore(store);

        administrator = new Administrator();
        administrator.setPassword("password");
        administrator.setUsername("cesare");
        administrator.setName("admin");
        administratorDAO.addAdmin(administrator);

        manager = new Manager();
        manager.setName("Cesare");
        manager.setSurname("Culcea");
        manager.setPassword("password");
        manager.setUsername("tmpUser");
        manager.setStoreCity(store.getCity());
        administratorDAO.addManager(manager);

    }

    @After
    public void tearDown() {
        IUserDAO userDAO = UserDAO.getInstance();
        IManagerDAO managerDAO = ManagerDAO.getInstance();
        IAdministratorDAO administratorDAO = AdministratorDAO.getInstance();

        managerDAO.removeManager(manager.getUsername());
        userDAO.removeByUsername(manager.getUsername());

        administratorDAO.removeAdmin(administrator.getUsername());
        userDAO.removeByUsername(administrator.getUsername());

        administratorDAO.removeStore(store.getCity());
    }

    @Test
    public void getManagerTest(){
        IAdministratorDAO administratorDAO = AdministratorDAO.getInstance();
        Manager newManager = administratorDAO.getManagerByUsername(manager.getUsername());

        Assert.assertEquals(manager.toString(), newManager.toString());
    }

    @Test
    public void getAdminTest(){
        IAdministratorDAO administratorDAO = AdministratorDAO.getInstance();
        Administrator newAdmin = administratorDAO.getAdminByUsername(administrator.getUsername());

        Assert.assertEquals(administrator.toString(), newAdmin.toString());
    }

    @Test
    public void storeExistTest(){
        IAdministratorDAO administratorDAO = AdministratorDAO.getInstance();
        boolean exist = administratorDAO.storeExist(store.getCity());

        Assert.assertTrue(exist);
    }


    @Test
    public void adminExistTest(){
        IUserDAO userDAO = UserDAO.getInstance();
        boolean exist = userDAO.administratorExist(administrator.getUsername());

        Assert.assertTrue(exist);
    }


    @Test
    public void managerExistTest(){
        IUserDAO userDAO = UserDAO.getInstance();
        boolean exist = userDAO.managerExist(manager.getUsername());

        Assert.assertTrue(exist);
    }


}
