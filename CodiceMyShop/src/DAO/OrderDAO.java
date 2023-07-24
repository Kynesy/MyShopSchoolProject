package DAO;


import DbInterface.DbConnection;
import DbInterface.IDbConnection;
import Model.Feedback;
import Model.Order;
import Model.Prenotation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class OrderDAO implements IOrderDAO{
    private static OrderDAO instance = new OrderDAO();
    private static IDbConnection connection;
    private static ResultSet resultSet;

    private OrderDAO(){
        connection = null;
        resultSet = null;
    }

    public static OrderDAO getInstance() {
        return instance;
    }

    @Override
    public int uploadOrder(String username, String orderID, int i) {
        String sqlStatement = "INSERT INTO tmp.orders VALUES ('"+orderID+"','"+ i +"','"+username+"');";
        connection = DbConnection.getInstance();
        int row = connection.executeUpdate(sqlStatement);
        connection.close();
        return row;
    }

    @Override
    public int createBooking(String username, int articleID, int quantity) {
        String sqlStatement = "INSERT INTO tmp.booking(username, articleID, quantity) VALUES ('"+username+"','"+articleID+"','"+quantity+"');";
        connection = DbConnection.getInstance();
        connection.executeUpdate(sqlStatement);
        int id = connection.getLastInsertId();
        connection.close();
        return id;
    }

    @Override
    public int deleteBooking(int bookingID) {
        String sqlStatement = "DELETE FROM tmp.booking WHERE tmp.booking.ID = '"+bookingID+"';";
        connection = DbConnection.getInstance();
        int row = connection.executeUpdate(sqlStatement);
        connection.close();
        return row;
    }

    @Override
    public void deleteArticleFromOrderHistory(int articleID) {
        String sqlStatement = "DELETE FROM tmp.articlesOrdered WHERE tmp.articlesOrdered.article_ID = '"+articleID+"';";
        connection = DbConnection.getInstance();
        connection.executeUpdate(sqlStatement);
        connection.close();
    }

    @Override
    public boolean orderExist(String orderID) {
        String sqlStatement = "SELECT tmp.orders.orderID FROM tmp.orders WHERE tmp.orders.orderID = '"+orderID+"';";
        boolean exist = false;

        connection = DbConnection.getInstance();
        resultSet = connection.executeQuery(sqlStatement);

        try {
            resultSet.next();
            if(resultSet.getRow() == 1){
                exist = true;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        connection.close();
        return exist;
    }

    @Override
    public int updatePaidStatus(String orderID, int paidStatus) {
        String sqlStatement = "UPDATE tmp.orders SET tmp.orders.paidStatus = '"+paidStatus+"'" +
                " WHERE tmp.orders.orderID ='"+orderID+"';";
        connection = DbConnection.getInstance();
        int row = connection.executeUpdate(sqlStatement);
        connection.close();
        return row;
    }

    @Override
    public int deleteOrder(String orderID) {
        String sqlStatement = "DELETE FROM tmp.orders WHERE tmp.orders.orderID = '"+orderID+"';";
        connection = DbConnection.getInstance();
        int row = connection.executeUpdate(sqlStatement);
        connection.close();
        return row;
    }

    @Override
    public ArrayList<Order> getAllOrders(){
        ArrayList<Order> orderArrayList = new ArrayList<>();

        String sqlStatement = "SELECT tmp.orders.* FROM tmp.orders;";
        connection = DbConnection.getInstance();
        resultSet = connection.executeQuery(sqlStatement);
        try {
            while (resultSet.next()) {
                String orderID = resultSet.getString("orderID");
                String username = resultSet.getString("user_Username");
                int paidStatus = resultSet.getInt("paidStatus");
                Order o = new Order(orderID, username, paidStatus);
                orderArrayList.add(o);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            connection.close();
        }
        return orderArrayList;
    }

    @Override
    public ArrayList<Prenotation> getAllPrenotations() {
        ArrayList<Prenotation> prenotationArrayList = new ArrayList<>();

        String sqlStatement = "SELECT tmp.booking.* FROM tmp.booking;";
        connection = DbConnection.getInstance();
        resultSet = connection.executeQuery(sqlStatement);
        try {
            while (resultSet.next()) {
                int articleID = Integer.parseInt(resultSet.getString("articleID"));
                String username = resultSet.getString("username");
                int quantity = resultSet.getInt("quantity");
                int id = resultSet.getInt("ID");
                Prenotation p = new Prenotation(username,articleID,quantity);
                p.setID(id);
                prenotationArrayList.add(p);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            connection.close();
        }
        return prenotationArrayList;
    }

    @Override
    public int addToOrderHistory(String username, int articleID) {
        String sqlStatement = "INSERT INTO tmp.articlesOrdered VALUES('"+username+"','"+articleID+"');";
        connection = DbConnection.getInstance();
        int row = connection.executeUpdate(sqlStatement);
        connection.close();
        return row;
    }

    @Override
    public boolean isAlreadyOrdered(String username, int articleID) {
        String sqlStatement = "SELECT tmp.articlesOrdered.* FROM tmp.articlesOrdered " +
                "WHERE tmp.articlesOrdered.user_Username = '"+username+"' " +
                "AND tmp.articlesOrdered.article_ID = '"+articleID+"';";
        boolean exist = false;

        connection = DbConnection.getInstance();
        resultSet = connection.executeQuery(sqlStatement);

        try {
            resultSet.next();
            if(resultSet.getRow() == 1){
                exist = true;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        connection.close();
        return exist;
    }

    @Override
    public void deleteArticleFromBooking(int articleID) {
        String sqlStatement = "DELETE FROM tmp.booking WHERE tmp.booking.articleID = '"+articleID+"';";
        connection = DbConnection.getInstance();
        connection.executeUpdate(sqlStatement);
        connection.close();
    }

    @Override
    public int removeAllUserOrders(String selectedUsername) {
        connection = DbConnection.getInstance();
        String sqlStatement =  "DELETE FROM tmp.orders WHERE user_Username = '" + selectedUsername + "';";
        int rowCount = connection.executeUpdate(sqlStatement) ;
        connection.close();
        return rowCount;
    }

    @Override
    public int removeAllUserBookings(String selectedUsername) {
        connection = DbConnection.getInstance();
        String sqlStatement =  "DELETE FROM tmp.booking WHERE username = '" + selectedUsername + "';";
        int rowCount = connection.executeUpdate(sqlStatement) ;
        connection.close();
        return rowCount;
    }

    @Override
    public int removeUserHistory(String selectedUsername) {
        connection = DbConnection.getInstance();
        String sqlStatement =  "DELETE FROM tmp.articlesOrdered WHERE user_Username = '" + selectedUsername + "';";
        int rowCount = connection.executeUpdate(sqlStatement) ;
        connection.close();
        return rowCount;
    }
}
