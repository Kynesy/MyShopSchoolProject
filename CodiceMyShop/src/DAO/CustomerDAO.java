package DAO;

import Business.HashAlgorithm.HashAlgorithm;
import DbInterface.DbConnection;
import DbInterface.IDbConnection;
import Model.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerDAO implements ICustomerDAO{
    private static CustomerDAO instance = new CustomerDAO();
    private static IDbConnection connection;
    private static ResultSet resultSet;

    private CustomerDAO() {
        connection = null;
        resultSet = null;
    }

    public static CustomerDAO getInstance() {
        return instance;
    }

    @Override
    public Customer getByUsername(String username) {
        String sqlStatement = "SELECT tmp.User.Name, tmp.User.Surname, tmp.User.Email, " +
                "tmp.User.Username, tmp.User.Password, tmp.userStat.Phone, " +
                "tmp.userStat.store_City, tmp.userStat.Birth, tmp.userStat.Address, tmp.userStat.Occupation " +
                "FROM tmp.User INNER JOIN tmp.userStat ON tmp.user.Username = tmp.userstat.Username " +
                "WHERE tmp.User.Username = '" + username + "';";

        connection = DbConnection.getInstance();
        resultSet = connection.executeQuery(sqlStatement);
        Customer customer;

        try{
            resultSet.next();
            if (resultSet.getRow() == 1){
                customer = new Customer();
                customer.setUsername(resultSet.getString("Username"));
                customer.setPassword(resultSet.getString("Password"));
                customer.setName(resultSet.getString("Name"));
                customer.setSurname(resultSet.getString("Surname"));
                customer.setEmail(resultSet.getString("Email"));
                customer.setPhoneNumber(resultSet.getLong("Phone"));
                customer.setBirthYear((resultSet.getInt("Birth")));
                customer.setAddress(resultSet.getString("Address"));
                customer.setOccupation(resultSet.getString("Occupation"));
                customer.setStoreCity(resultSet.getString("store_City"));
                return customer;
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
    public int addNewCustomer(Customer customer) {
        String sqlStatement1 = "INSERT INTO tmp.User VALUES ('" + customer.getUsername() + "','"
                + HashAlgorithm.getDigest(customer.getPassword()) + "','"
                + customer.getName() + "','"
                + customer.getSurname() +"','"
                + customer.getEmail() +
               "');";

        String sqlStatement2 = "INSERT INTO tmp.userStat VALUES ('" + customer.getUsername() + "','"
                + customer.getStoreCity() + "','"
                + customer.getBirthYear() + "','"
                + customer.getAddress() + "','"
                + customer.getOccupation() + "','"
                + customer.getPhoneNumber() +"');";

        connection = DbConnection.getInstance();
        int rowCount = connection.executeUpdate(sqlStatement1);
        if(rowCount == 0) return rowCount;

        rowCount = connection.executeUpdate(sqlStatement2);
        connection.close();
        return rowCount;
    }

    @Override
    public int updateCustomerData(Customer customer) {
        String sqlStatement2 = "UPDATE tmp.userStat SET store_City = '"+customer.getStoreCity()+"'," +
                " Birth = '" + customer.getBirthYear() + "'," +
                " Address = '" + customer.getAddress() + "'," +
                " Occupation = '" + customer.getOccupation() + "'," +
                " Phone = '" + customer.getPhoneNumber() + "' " +
                "WHERE Username = '" + customer.getUsername() + "';";

        connection = DbConnection.getInstance();
        int row = connection.executeUpdate(sqlStatement2);
        connection.close();
        return row;
    }

    @Override
    public int createWishList(String username, String listName) {
        connection = DbConnection.getInstance();
        String sqlStatement = "INSERT INTO tmp.wishList (Name, user_Username) VALUES ('"+ listName +"', '"+username+"')";
        connection.executeUpdate(sqlStatement);
        int id = connection.getLastInsertId();
        connection.close();

        return id;
    }

    @Override
    public ArrayList<WishList> getAllCustomerWishLists(String username) {
        String sqlStatement = "SELECT tmp.wishlist.* FROM tmp.wishlist WHERE tmp.wishlist.user_Username = '"+username+"';";

        ArrayList<WishList> lists = new ArrayList<>();
        try{
            connection = DbConnection.getInstance();
            resultSet = connection.executeQuery(sqlStatement);
            while (resultSet.next()){
                WishList wishList = new WishList();
                wishList.setName(resultSet.getString("Name"));
                wishList.setId(resultSet.getInt("ID"));
                wishList.setUsername(resultSet.getString("user_Username"));
                lists.add(wishList);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            connection.close();
        }
        //carico prodotti e servizi in ogni lista
        for(WishList wishList : lists){
            wishList.setItems(getAllItemsByWishlist(wishList.getId()));
            wishList.setServices(getAllServicesByWishList(wishList.getId()));
        }
        //per ogni prodotto carico produttori e categorie
        for(WishList wishList : lists){
            ArrayList<Item> iArray = wishList.getItems();
            for(Item item : iArray){
                item.setItemVendor(ItemDAO.getInstance().getItemVendor(item.getId()));
                item.setCategory(ArticleDAO.getInstance().getCategoryByID(item.getId()));
            }
        }
        return lists;
    }

    @Override
    public int addArticleToWishList(int selectedArticle, WishList wishList) {
        String sqlStatement = "INSERT INTO tmp.itemlist VALUES ('"+wishList.getId()+"', '"+selectedArticle+"', '0');";
        connection = DbConnection.getInstance();
        int row = connection.executeUpdate(sqlStatement);
        connection.close();
        return row;
    }

    @Override
    public int updateItemQuantity(int wishlistID, int itemId, int newQuantity) {
        String sqlStatement = "UPDATE tmp.itemlist SET orderedQuantity = '"+ newQuantity+"' " +
                "WHERE wishList_ID = '"+wishlistID+"' and article_ID = '"+itemId+"';";
        connection = DbConnection.getInstance();
        int row = connection.executeUpdate(sqlStatement);
        connection.close();
        return row;
    }


    @Override
    public int wishlistContainsItem(int wishlistId, int itemId) {
        String sqlStatement = "SELECT wishList_ID, article_ID, orderedQuantity FROM tmp.itemList " +
                "WHERE wishList_ID = '" + wishlistId+"' AND article_ID = '" + itemId +"';";

        int quantity = -1;
        connection = DbConnection.getInstance();
        resultSet = connection.executeQuery(sqlStatement);
        try {
            resultSet.next();
            if(resultSet.getRow()==1){
                quantity = resultSet.getInt("orderedQuantity");
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        connection.close();
        return quantity;
    }


    @Override
    public ArrayList<Item> getAllItemsByWishlist(int wishlistID) {
        connection = DbConnection.getInstance();
        ArrayList<Item> items = new ArrayList<>();

        String sqlStatement = "SELECT tmp.itemlist.wishList_ID, tmp.itemlist.orderedQuantity, tmp.article.ID, " +
                "tmp.article.Name," +
                " tmp.article.Description," +
                " tmp.article.Price, tmp.itemstat.storeQuantity, " +
                "tmp.itemstat.store_City, tmp.itemstat.LineNum, tmp.itemstat.ShelfNum FROM tmp.itemlist " +
                "INNER JOIN tmp.article ON tmp.itemlist.article_ID = tmp.article.ID " +
                "INNER JOIN tmp.item ON tmp.itemlist.article_ID = tmp.item.ID " +
                "INNER JOIN tmp.itemstat ON tmp.itemlist.article_ID = tmp.itemstat.item_ID " +
                "WHERE tmp.itemlist.wishList_ID = '"+ wishlistID +"'";

        resultSet = connection.executeQuery(sqlStatement);

        try{
            while (resultSet.next()){
                Item item = new Item();
                item.setId(resultSet.getInt("ID"));
                item.setName(resultSet.getString("Name"));
                item.setPrice(resultSet.getDouble("Price"));
                item.setPosition( resultSet.getInt("LineNum"), resultSet.getInt("ShelfNum"));
                item.setCityStore(resultSet.getString("store_City"));
                item.setDescription(resultSet.getString("Description"));
                item.setAvaiability(resultSet.getInt("storeQuantity")); //relativa alla disponibilità in magazzino
                item.setWishlistQuantity(resultSet.getInt("orderedQuantity")); //relativa alle quantità della lista d'acquisto
                items.add(item);
            }

            return items;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            connection.close();
        }
        return null;
    }

    @Override
    public ArrayList<Service> getAllServicesByWishList(int wishlistID) {
        connection = DbConnection.getInstance();
        ArrayList<Service> services = new ArrayList<>();

        String sqlStatement = "SELECT tmp.itemlist.wishList_ID, tmp.article.ID," +
                " tmp.article.Name, tmp.article.Description,tmp.article.Price, tmp.service.Vendor" +
                " FROM tmp.article INNER JOIN tmp.service ON tmp.article.ID = tmp.service.ID INNER JOIN tmp.itemlist" +
                " ON tmp.article.ID = tmp.itemlist.article_ID WHERE tmp.itemlist.wishList_ID = '"+ wishlistID +"'";

        resultSet = connection.executeQuery(sqlStatement);

        try{
            while (resultSet.next()){
                Service service = new Service();
                service.setId(resultSet.getInt("ID"));
                service.setName(resultSet.getString("Name"));
                service.setPrice(resultSet.getDouble("Price"));
                service.setVendorName(resultSet.getString("Vendor"));
                service.setDescription(resultSet.getString("Description"));
                services.add(service);
            }

            return services;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            connection.close();
        }
        return null;
    }

    @Override
    public int resetWishlist(int idWishlist) {
        String sqlStatement = "DELETE FROM tmp.itemlist WHERE wishlist_ID = '"+ idWishlist +"';";
        connection = DbConnection.getInstance();
        int row = connection.executeUpdate(sqlStatement);
        connection.close();
        return row;
    }

    @Override
    public int deleteUserWishlist(int idWishlist) {
        String sqlStatement = "DELETE FROM tmp.wishlist WHERE ID = '"+ idWishlist +"';";
        connection = DbConnection.getInstance();
        int row = connection.executeUpdate(sqlStatement);
        connection.close();
        return row;
    }

    @Override
    public int removeItemFromWishList(int selectedItem, WishList wishList) {
        String sqlStatement = "DELETE FROM tmp.itemlist WHERE tmp.itemlist.wishList_ID = '"+wishList.getId()+"' " +
                "AND tmp.itemlist.article_ID = '"+selectedItem+"';";
        connection = DbConnection.getInstance();
        int row = connection.executeUpdate(sqlStatement);
        connection.close();
        return row;
    }

    @Override
    public int removeCustomer(String selectedUsername) {
        connection = DbConnection.getInstance();
        String sqlStatement =  "DELETE FROM tmp.userStat WHERE Username = '" + selectedUsername + "';";
        int rowCount = connection.executeUpdate(sqlStatement) ;
        connection.close();
        return rowCount;
    }

    @Override
    public int removeAllUserWishlist(String selectedUsername) {
        connection = DbConnection.getInstance();
        String sqlStatement =  "DELETE FROM tmp.wishList WHERE user_Username = '" + selectedUsername + "';";
        int rowCount = connection.executeUpdate(sqlStatement) ;
        connection.close();
        return rowCount;
    }
}
