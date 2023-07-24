package DAO;

import Model.*;

import java.io.File;
import java.util.ArrayList;

public interface IArticleDAO {
    boolean articleExist(int id);
    int addFoto(int articleID, File image);
    ArrayList<File> getFotosFromDB(int articleId);

    int createFeedback(int index, String message, int articleId, String username);
    ArrayList<Feedback> getAllFeedbackByArticleId(int articleID);


    int addCategory(Category category);
    int addCategory(Category category, int parentId);
    boolean categoryExist(Category category);
    int removeCategory(int id);
    int getParentId(int id); //restituisce -1 se non ha categorie padre
    Category getCategory(int id);
    Category getCategoryByID(int itemID);
    ArrayList<Category> getSubCategory(int id); // restituisce null se non trova sottocategorie
    ArrayList<Category> getAllCategories();


    int deleteAllFotos(int articleID);
    int deleteServiceFromFeedbacks(int serviceID);
    int deleteArticle(int serviceID);
    int deleteServiceFromAllWishlist(int serviceID);

    int removeAllUserFeedbacks(String selectedUsername);
}
