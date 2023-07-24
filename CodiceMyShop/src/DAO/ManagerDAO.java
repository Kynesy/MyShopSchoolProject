package DAO;


import Business.MailUtils;
import DbInterface.DbConnection;
import DbInterface.IDbConnection;

import Model.Store;
import Model.User;


import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ManagerDAO implements IManagerDAO{
    private static ManagerDAO instance = new ManagerDAO();
    private static IDbConnection connection;
    private static ResultSet resultSet;

    private ManagerDAO() {
        connection = null;
        resultSet = null;
    }

    public static ManagerDAO getInstance() {
        return instance;
    }

    @Override
    public int changeQuantity(int itemID, int productQuantity) {
        String sqlStatement = "UPDATE tmp.itemstat SET tmp.itemstat.storeQuantity = '"+ productQuantity +"' " +
                "WHERE tmp.itemstat.item_ID = '"+itemID+"';";

        connection = DbConnection.getInstance();
        int rowCount = connection.executeUpdate(sqlStatement);
        connection.close();
        return rowCount;
    }

    @Override
    public Store getManagerStore(String username) {
        Store store = new Store();
        String sqlStatement = "SELECT store_City FROM tmp.manager WHERE Username ='"+ username +"';";
        connection = DbConnection.getInstance();
        resultSet = connection.executeQuery(sqlStatement);
        try {
            resultSet.next();
            store.setCity(resultSet.getString("store_City"));
            return store;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public int disableByUsername(String username) {
        String sqlStatement = "INSERT INTO tmp.disabledAccount VALUE ('"+ username +"');";
        connection = DbConnection.getInstance();
        int row = connection.executeUpdate(sqlStatement);
        connection.close();
        return row;
    }

    @Override
    public boolean isDisabled(String username) {
        String sqlStatement = "SELECT Username FROM tmp.disabledAccount WHERE tmp.disabledAccount.Username = '"+ username +"';";
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
    public int activateByUsername(String username) {
        String sqlStatement = "DELETE FROM tmp.disabledAccount WHERE Username = '" + username + "';";
        connection = DbConnection.getInstance();
        int row = connection.executeUpdate(sqlStatement);
        connection.close();
        return row;
    }

    @Override
    public ArrayList<User> getAllUsersByStore(String storeCity) {
        String sqlStatement = "SELECT tmp.user.Name, tmp.User.Email, tmp.User.Username FROM tmp.User INNER JOIN tmp.userStat " +
                "ON tmp.User.Username = tmp.userStat.Username " +
                "WHERE tmp.userStat.store_City = '"+storeCity+"';";
        ArrayList<User> users = new ArrayList<>();

        connection = DbConnection.getInstance();
        resultSet = connection.executeQuery(sqlStatement);

        try{
            while(resultSet.next()) {
                User user = new User();
                user.setName(resultSet.getString("Name"));
                user.setUsername(resultSet.getString("Username"));
                user.setEmail(resultSet.getString("Email"));
                users.add(user);
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
        for(User user: users){
            if(ManagerDAO.getInstance().isDisabled(user.getUsername())){
                user.setIsDisabled(1);
            }else {
                user.setIsDisabled(0);
            }
        }
        return users;
    }

    @Override
    public void removeManager(String selectedUsername) {
        String sqlStatement = "DELETE FROM tmp.manager WHERE tmp.manager.Username = '"+selectedUsername+"';";
        connection = DbConnection.getInstance();
        connection.executeUpdate(sqlStatement);
        connection.close();
    }
}
