package Test.JUnitTestDAO;

import DAO.*;
import DbInterface.DbUser;
import Model.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UserDAOTest {
    DbUser dbUser = DbUser.getInstance();
    User user;

    @Before
    public void setUp(){
        IUserDAO userDAO = UserDAO.getInstance();
        user = new User();
        user.setEmail("cezar99.rp@gmail.com");
        user.setName("Cesare");
        user.setSurname("Culcea");
        user.setPassword("password");
        user.setUsername("cesare");

        userDAO.addNewUser(user);

    }

    @After
    public void tearDown() {
        IUserDAO userDAO = UserDAO.getInstance();

        userDAO.removeByUsername(user.getUsername());
    }

    @Test
    public void modifyUserTest(){
        IUserDAO userDAO = UserDAO.getInstance();
        userDAO.updateUserData(user);

        User newUser = userDAO.getByUsername(user.getUsername());
        Assert.assertEquals(user.toString(), newUser.toString());
    }

    @Test
    public void credentialsOkTest(){
        IUserDAO userDAO = UserDAO.getInstance();

        boolean ok = userDAO.credentialsOk(user.getUsername(), user.getPassword());
        Assert.assertTrue(ok);
    }

    @Test
    public void userExistTest(){
        IUserDAO userDAO = UserDAO.getInstance();

        boolean exist = userDAO.userExist(user.getUsername());
        Assert.assertTrue(exist);
    }
}
