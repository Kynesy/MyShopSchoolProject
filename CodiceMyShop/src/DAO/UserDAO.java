package DAO;

import Business.HashAlgorithm.HashAlgorithm;
import DbInterface.*;
import Model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserDAO implements IUserDAO{
    private static UserDAO instance = new UserDAO();
    private User user;
    private static IDbConnection connection;
    private static ResultSet resultSet;

    private UserDAO() {
        user = null;
        connection = null;
        resultSet = null;
    }

    public static UserDAO getInstance() {
        return instance;
    }

    @Override
    public User getByUsername(String username) {
        String sqlStatement = "SELECT tmp.User.Name, tmp.User.Surname, tmp.User.Email, " +
                "tmp.User.Username, tmp.User.Password FROM tmp.User WHERE tmp.User.Username = '" + username + "';";

        connection = DbConnection.getInstance();
        resultSet = connection.executeQuery(sqlStatement);

        try{
            resultSet.next();
            if (resultSet.getRow() == 1){
                user = new User();
                user.setUsername(resultSet.getString("Username"));
                user.setPassword(resultSet.getString("Password"));
                user.setName(resultSet.getString("Name"));
                user.setSurname(resultSet.getString("Surname"));
                user.setEmail(resultSet.getString("Email"));
                return user;
            }
        }catch (SQLException e) {
            // handle any errors
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        } catch (NullPointerException e) {
            // handle any errors
            System.out.println("Resultset: " + e.getMessage());
        } finally {
            connection.close();
        }
        return null;
    }

    @Override
    public int addNewUser(User user) {
        String sqlStatement1 = "INSERT INTO tmp.User VALUES ('" + user.getUsername() + "','"
                + HashAlgorithm.getDigest(user.getPassword()) + "','"
                + user.getName() + "','"
                + user.getSurname() +"','"
                + user.getEmail() +"');";

        connection = DbConnection.getInstance();
        int rowCount = connection.executeUpdate(sqlStatement1);
        connection.close();
        return rowCount;
    }

    @Override
    public int removeByUsername(String username) {
        connection = DbConnection.getInstance();

        String sqlStatement =  "DELETE FROM tmp.User WHERE Username = '" + username + "';";
        int rowCount = connection.executeUpdate(sqlStatement) ;
        connection.close();
        return rowCount;
    }

    @Override
    public ArrayList<User> getAllUsers() {
        String sqlStatement = "SELECT Name, Surname, Email FROM tmp.User;";
        ArrayList<User> users = new ArrayList<>();

        connection = DbConnection.getInstance();
        resultSet = connection.executeQuery(sqlStatement);

        try{
            while(resultSet.next()) {
                user = new User();
                user.setName(resultSet.getString("Name"));
                user.setSurname(resultSet.getString("Surname"));
                user.setEmail(resultSet.getString("Email"));
                users.add(user);
            }
            return users;

        }catch (SQLException e) {
            // handle any errors
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        } catch (NullPointerException e) {
            // handle any errors
            System.out.println("Resultset: " + e.getMessage());
        } finally {
            connection.close();
        }
        return null;
    }


    @Override
    public boolean userExist(String username) {
        String sqlStatement = "SELECT Username FROM tmp.User WHERE tmp.User.Username = '"+ username +"';";
        boolean exists = false;

        connection = DbConnection.getInstance();
        resultSet = connection.executeQuery(sqlStatement);

        try {
            resultSet.next();
            if(resultSet.getRow()==1){
                exists = true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            connection.close();
        }

        return exists;
    }

    @Override
    public boolean managerExist(String username) {
        String sqlStatement = "SELECT Username FROM tmp.Manager WHERE tmp.Manager.Username = '"+ username +"';";
        boolean exists = false;

        connection = DbConnection.getInstance();
        resultSet = connection.executeQuery(sqlStatement);

        try {
            resultSet.next();
            if(resultSet.getRow()==1){
                exists = true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            connection.close();
        }

        return exists;
    }

    @Override
    public boolean administratorExist(String username) {
        String sqlStatement = "SELECT Username FROM tmp.Administrator WHERE tmp.Administrator.Username = '"+ username +"';";
        boolean exists = false;

        connection = DbConnection.getInstance();
        resultSet = connection.executeQuery(sqlStatement);

        try {
            resultSet.next();
            if(resultSet.getRow()==1){
                exists = true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            connection.close();
        }

        return exists;
    }

    @Override
    public boolean credentialsOk(String username, String password) {
        boolean credOK = false;
        user = UserDAO.getInstance().getByUsername(username);
        if(HashAlgorithm.checkHash(password, user.getPassword())){
            credOK = true;
        }
        return credOK;
    }

    @Override
    public int updateUserData(User user) {
        String sqlStatement = "UPDATE tmp.User SET Name = '" + user.getName() +"', Surname = '" + user.getSurname() + "', Email = '" + user.getEmail() + "' WHERE Username = '" + user.getUsername() + "';";
        connection = DbConnection.getInstance();
        int row = connection.executeUpdate(sqlStatement);
        connection.close();
        return row;
    }
}
