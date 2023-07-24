package DAO;

import Model.*;
//import org.apache.pdfbox.pdmodel.PDDocument;

import java.util.ArrayList;

public interface ICustomerDAO {
    Customer getByUsername(String username);
    int addNewCustomer(Customer customer);
    int updateCustomerData(Customer customer);
    int createWishList(String username, String listName);
    ArrayList<WishList> getAllCustomerWishLists(String username);
    int addArticleToWishList(int selectedItem, WishList wishList);
    int removeItemFromWishList(int selectedItem, WishList wishList);
    int updateItemQuantity(int wishlistID, int itemId, int newQuantity);


    ArrayList<Item> getAllItemsByWishlist(int wishlistID);
    ArrayList<Service> getAllServicesByWishList(int wishlistID);
    int wishlistContainsItem(int wishlistId, int itemId); //ritorna la quantità del prodotto, se esiste in una lista, altrimenti -1


    int resetWishlist(int idWishlist);
    int deleteUserWishlist(int idWishlist);
    int removeCustomer(String selectedUsername);

    int removeAllUserWishlist(String selectedUsername);
}
