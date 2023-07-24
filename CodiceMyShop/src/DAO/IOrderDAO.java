package DAO;

import Model.Order;
import Model.Prenotation;

import java.util.ArrayList;

public interface IOrderDAO {

    int uploadOrder(String username, String orderID, int paidStatus); //0 non pagato, 1 pagato
    int updatePaidStatus(String orderID, int paidStatus);
    int createBooking(String username, int articleId, int quantity);
    boolean orderExist(String orderID);
    int deleteBooking(int bookingID);
    int deleteOrder(String orderID);

    ArrayList<Order> getAllOrders();
    ArrayList<Prenotation> getAllPrenotations();

    int addToOrderHistory(String username, int articleID);
    boolean isAlreadyOrdered(String username, int articleID);
    void deleteArticleFromOrderHistory(int articleID);
    void deleteArticleFromBooking(int articleID);
    int removeAllUserOrders(String selectedUsername);
    int removeAllUserBookings(String selectedUsername);
    int removeUserHistory(String selectedUsername);
}
