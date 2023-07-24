package DAO;

import Model.User;

import java.util.ArrayList;

public interface IUserDAO {
    User getByUsername(String username);
    ArrayList<User> getAllUsers();
    int addNewUser(User user);
    int removeByUsername(String username);
    int updateUserData(User user);
    boolean userExist(String username);
    boolean managerExist(String username);
    boolean administratorExist(String username);
    boolean credentialsOk(String username, String password);
}
