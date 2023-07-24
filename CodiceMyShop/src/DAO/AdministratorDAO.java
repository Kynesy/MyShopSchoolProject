package DAO;

import Business.HashAlgorithm.HashAlgorithm;
import DbInterface.DbConnection;
import DbInterface.IDbConnection;
import Model.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AdministratorDAO implements IAdministratorDAO{
    private static AdministratorDAO instance = new AdministratorDAO();
    private Administrator administrator;
    private static IDbConnection connection;
    private static ResultSet resultSet;

    private AdministratorDAO() {
        administrator = null;
        connection = null;
        resultSet = null;
    }

    public static AdministratorDAO getInstance() {
        return instance;
    }

    @Override
    public int addAdmin(Administrator administrator) {
        connection = DbConnection.getInstance();

        String sqlStatement = "INSERT INTO tmp.User VALUES ('" + administrator.getUsername() + "','" + HashAlgorithm.getDigest(administrator.getPassword()) + "','admin','admin','null');";
        connection.executeUpdate(sqlStatement);

        sqlStatement = "INSERT INTO tmp.Administrator VALUES('" + administrator.getUsername() + "')";
        int rowCount = connection.executeUpdate(sqlStatement);
        connection.close();
        return rowCount;
    }

    @Override
    public Administrator getAdminByUsername(String username) {
            String sqlStatement = "SELECT tmp.User.Name, tmp.User.Surname, tmp.User.Email, " +
                    "tmp.User.Username FROM tmp.User INNER JOIN tmp.administrator " +
                    "ON tmp.User.Username = tmp.administrator.Username WHERE tmp.User.Username = '" + username + "';";
            Administrator administrator;
            connection = DbConnection.getInstance();
            resultSet = connection.executeQuery(sqlStatement);

            try{
                resultSet.next();
                if (resultSet.getRow() == 1){
                    administrator = new Administrator();
                    administrator.setUsername(resultSet.getString("Username"));
                    administrator.setName(resultSet.getString("Name"));
                    administrator.setSurname(resultSet.getString("Surname"));
                    administrator.setEmail(resultSet.getString("Email"));
                    return administrator;
                }
            }catch (SQLException e) {
                System.out.println("SQLException: " + e.getMessage());
                System.out.println("SQLState: " + e.getSQLState());
                System.out.println("VendorError: " + e.getErrorCode());
            } catch (NullPointerException e) {
                System.out.println("Resultset: " + e.getMessage());
            } finally {
                connection.close();
            }
            return null;
    }

    @Override
    public int addManager(Manager manager) {
        connection = DbConnection.getInstance();

        String sqlStatement = "INSERT INTO tmp.User VALUES ('" + manager.getUsername() + "','" + HashAlgorithm.getDigest(manager.getPassword()) + "','" + manager.getName() + "','" + manager.getSurname() + "','null');";
        connection.executeUpdate(sqlStatement);

        sqlStatement = "INSERT INTO tmp.Manager VALUES('" + manager.getUsername() + "', '"+ manager.getStoreCity() +"')";
        int rowCount = connection.executeUpdate(sqlStatement);
        connection.close();
        return rowCount;
    }


    @Override
    public Manager getManagerByUsername(String username) {
        String sqlStatement = "SELECT tmp.User.Name, tmp.User.Surname, tmp.User.Email, " +
                "tmp.User.Username, tmp.manager.store_City FROM tmp.User INNER JOIN tmp.manager " +
                "ON tmp.User.Username = tmp.manager.Username WHERE tmp.User.Username = '" + username + "';";
        Manager manager;
        connection = DbConnection.getInstance();
        resultSet = connection.executeQuery(sqlStatement);

        try{
            resultSet.next();
            if (resultSet.getRow() == 1){
                manager = new Manager();
                manager.setUsername(resultSet.getString("Username"));
                manager.setName(resultSet.getString("Name"));
                manager.setSurname(resultSet.getString("Surname"));
                manager.setEmail(resultSet.getString("Email"));
                manager.setStoreCity(resultSet.getString("store_City"));
                return manager;
            }
        }catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        } catch (NullPointerException e) {
            System.out.println("Resultset: " + e.getMessage());
        } finally {
            connection.close();
        }
        return null;
    }

    @Override
    public ArrayList<User> getAllManagers() {
        connection = DbConnection.getInstance();
        ArrayList<User> managers = new ArrayList<>();

        String sqlStatement = "SELECT tmp.User.*, tmp.manager.store_City FROM tmp.User INNER JOIN tmp.Manager ON tmp.User.Username = tmp.Manager.Username;";
        resultSet = connection.executeQuery(sqlStatement);

        try{
            while(resultSet.next()) {
                Manager manager = new Manager();
                manager.setName(resultSet.getString("Name"));
                manager.setSurname(resultSet.getString("Surname"));
                manager.setEmail(resultSet.getString("Email"));
                manager.setUsername(resultSet.getString("Username"));
                manager.setStoreCity(resultSet.getString("store_City"));
                managers.add(manager);
            }
            return managers;

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
    public int addNewStore(Store store) {
        connection = DbConnection.getInstance();

        String sqlStatement = "INSERT INTO tmp.Store VALUES ('"+ store.getCity() +"');";
        int rowCount = connection.executeUpdate(sqlStatement);
        connection.close();

        return rowCount;
    }

    @Override
    public int removeStore(String city) {
        connection = DbConnection.getInstance();

        connection.executeUpdate("DELETE FROM tmp.ItemStat WHERE Store_City = '" + city + "';");
        String sqlStatement = "DELETE FROM tmp.Store WHERE City = '" + city + "';";
        int rowCount = connection.executeUpdate(sqlStatement) ;

        connection.close();
        return rowCount;
    }

    @Override
    public boolean storeExist(String city) {
        String sqlStatement = "SELECT City FROM tmp.Store WHERE tmp.Store.City = '"+ city +"';";
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
    public ArrayList<Store> getAllStores() {
        String sqlStatement = "SELECT City FROM tmp.Store;";
        ArrayList<Store> storeArrayList = new ArrayList<>();

        connection = DbConnection.getInstance();
        resultSet = connection.executeQuery(sqlStatement);
        Store store;
        try{
            while(resultSet.next()) {
                store = new Store();
                store.setCity(resultSet.getString("City"));
                storeArrayList.add(store);
            }
            return storeArrayList;

        }catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        } catch (NullPointerException e) {
            System.out.println("Resultset: " + e.getMessage());
        } finally {
            connection.close();
        }
        return null;
    }

    @Override
    public int removeAdmin(String selectedUsername) {
        connection = DbConnection.getInstance();
        String sqlStatement =  "DELETE FROM tmp.administrator WHERE Username = '"+selectedUsername+"';";
        int rowCount = connection.executeUpdate(sqlStatement) ;
        connection.close();
        return rowCount;
    }
}
