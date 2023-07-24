package DAO;

import Business.ArticleFactory.AbstractArticleFactory;
import Business.ArticleFactory.ArticleFactoryProvider;
import DbInterface.DbConnection;
import DbInterface.IDbConnection;
import Model.Item;
import Model.ItemVendor;
import Model.Vendor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ItemDAO implements IItemDAO{
    private static ItemDAO instance = new ItemDAO();
    private static IDbConnection connection;
    private static ResultSet resultSet;
    private AbstractArticleFactory articleFactory;

    private ItemDAO(){
        connection = null;
        resultSet = null;
        articleFactory = ArticleFactoryProvider.getArticleFactory(ArticleFactoryProvider.ArticleType.ITEM);
    }

    public static ItemDAO getInstance(){
        return instance;
    }

    @Override
    public int createItem(Item item)/*restituisce l'id dell'inserimento, altrimenti -1 se va male*/ {
            connection = DbConnection.getInstance();

            String sqlStatement1 = "INSERT INTO tmp.Article (Name, Description, Price, idCategory) " +
                    "VALUES ('"+ item.getName()+"', '"+ item.getDescription()+"', '"+ item.getPrice() +"', '"+ item.getCategory().getId() +"');";

            int row1 = connection.executeUpdate(sqlStatement1);
            int id = connection.getLastInsertId();

            String sqlStatement2 = "INSERT INTO tmp.item " +
                    "VALUES ('"+ id +"', '"+ item.getItemVendor().getName() + "');";
            int row2 = connection.executeUpdate(sqlStatement2);

            String sqlStatement3 = "INSERT INTO tmp.itemStat "+
                    "VALUES ('"+ id +"', '" + item.getCityStore() +"', '0', '"+ item.getLine() +"', '"+ item.getShelf() +"');";
            int row3 = connection.executeUpdate(sqlStatement3);
            connection.close();
            if(row1 + row2 + row3 == 3){
                return id;
            }else {
                return -1;
            }
    }

    @Override
    public int modifyItem(Item item) {
        connection = DbConnection.getInstance();

        String sqlStatement1 = "UPDATE tmp.Article SET Name='"+ item.getName()+"', " +
                "Description='"+ item.getDescription()+"', " +
                "Price='"+ item.getPrice() +"'," +
                "idCategory='"+ item.getCategory().getId() +"' " +
                "WHERE ID ='"+item.getId()+"';";
        int row1 = connection.executeUpdate(sqlStatement1);

        String sqlStatement2 =  "UPDATE tmp.item SET Producer='"+ item.getItemVendor().getName() +"' " +
                "WHERE ID='"+item.getId()+"';";
        int row2 = connection.executeUpdate(sqlStatement2);

        String sqlStatement3 = "UPDATE tmp.itemStat SET store_City ='"+item.getCityStore()+"', " +
                "LineNum='"+item.getLine()+"'," +
                "ShelfNum='"+item.getShelf()+"'" +
                "WHERE item_ID='"+item.getId()+"';";

        int row3 = connection.executeUpdate(sqlStatement3);
        connection.close();
        if(row1 + row2 + row3 == 3){
            return 1;
        }else {
            return -1;
        }
    }

    @Override
    public boolean itemExist(int id) {
        String sqlStatement = "SELECT ID FROM tmp.Item WHERE ID = '" + id +"';";
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
        }finally {
            connection.close();
            return exist;
        }
    }

    @Override
    public Item getItem(int id){
        Item item = (Item) articleFactory.create();
        String sqlStatement = "SELECT tmp.article.ID, tmp.article.Name, tmp.article.Description, tmp.article.Price," +
                "tmp.itemstat.storeQuantity, tmp.itemstat.store_City, tmp.itemstat.LineNum, " +
                "tmp.itemstat.ShelfNum FROM tmp.article INNER JOIN tmp.item ON tmp.article.ID = tmp.item.ID " +
                "INNER JOIN tmp.itemstat ON tmp.article.ID = tmp.itemstat.item_ID WHERE tmp.article.ID = '"+ id+"';";
        connection= DbConnection.getInstance();
        resultSet = connection.executeQuery(sqlStatement);

        try {
            resultSet.next();
            if (resultSet.getRow() == 1) {
                item.setId(resultSet.getInt("ID"));
                item.setName(resultSet.getString("Name"));
                item.setPrice(resultSet.getDouble("Price"));
                item.setDescription(resultSet.getString("Description"));
                item.setPosition(resultSet.getInt("LineNum"), resultSet.getInt("ShelfNum"));
                item.setCityStore(resultSet.getString("store_City"));
                item.setDescription(resultSet.getString("Description"));
            item.setAvaiability(resultSet.getInt("storeQuantity"));
            }
            return item;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            connection.close();
        }
        return null;
    }

    @Override
    public ArrayList<Item> getAllItemsByStore(String storeCity) { //se passato null, verranno visualizzati tutti i prodotti nel db
        connection = DbConnection.getInstance();
        ArrayList<Item> items = new ArrayList<>();

        String sqlStatement = "SELECT tmp.article.ID, tmp.article.Name, tmp.article.Description, tmp.article.Price, tmp.itemstat.storeQuantity," +
                " tmp.itemstat.store_City, tmp.itemstat.LineNum, tmp.itemstat.ShelfNum FROM tmp.article INNER JOIN tmp.item ON tmp.article.ID = tmp.item.ID " +
                "INNER JOIN tmp.itemstat ON tmp.article.ID = tmp.itemstat.item_ID";

        if(storeCity != null){
            sqlStatement = sqlStatement + " WHERE tmp.itemstat.store_City = '"+ storeCity +"';";
        }else{
            sqlStatement = sqlStatement + ";";
        }


        resultSet = connection.executeQuery(sqlStatement);

        try{
            while(resultSet.next()) {
                Item item = new Item();
                item.setId(resultSet.getInt("ID"));
                item.setName(resultSet.getString("Name"));
                item.setPrice(resultSet.getDouble("Price"));
                item.setPosition( resultSet.getInt("LineNum"), resultSet.getInt("ShelfNum"));
                item.setCityStore(resultSet.getString("store_City"));
                item.setDescription(resultSet.getString("Description"));
                item.setAvaiability(resultSet.getInt("storeQuantity"));
                items.add(item);
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
        return items;
    }

    @Override
    public int addItemVendor(ItemVendor itemVendor) {
        connection = DbConnection.getInstance();

        String sqlStatement = "INSERT INTO tmp.producer VALUES ('"+ itemVendor.getName() +"', " +
                "'" + itemVendor.getWebsite() +"', " +
                "'" + itemVendor.getCity() + "', " +
                "'" + itemVendor.getNation() + "')";

        int rowCount = connection.executeUpdate(sqlStatement);
        connection.close();

        return rowCount;
    }

    @Override
    public boolean itemVendorExist(String name) {
        String sqlStatement = "SELECT Name FROM tmp.producer WHERE Name = '" + name +"';";
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
    public ItemVendor getItemVendor(String name) {
        ItemVendor producer = new ItemVendor();
        connection = DbConnection.getInstance();

        String sqlStatement = "SELECT tmp.Producer.* FROM tmp.Producer WHERE tmp.Producer.Name = '"+ name +"';";
        resultSet = connection.executeQuery(sqlStatement);

        try{
            resultSet.next();
            if(resultSet.getRow()==1){
                producer.setName(resultSet.getString("Name"));
                producer.setWebsite(resultSet.getString("Website"));
                producer.setCity(resultSet.getString("City"));
                producer.setNation(resultSet.getString("Nation"));
                return producer;
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
    public ItemVendor getItemVendor(int itemID) {
        ItemVendor producer = new ItemVendor();
        connection = DbConnection.getInstance();

        String sqlStatement = "SELECT tmp.Producer.* FROM tmp.Producer " +
                "INNER JOIN tmp.item ON tmp.item.Producer = tmp.producer.Name " +
                "WHERE tmp.item.ID = '"+ itemID +"';";
        resultSet = connection.executeQuery(sqlStatement);

        try{
            resultSet.next();
            if(resultSet.getRow()==1){
                producer.setName(resultSet.getString("Name"));
                producer.setWebsite(resultSet.getString("Website"));
                producer.setCity(resultSet.getString("City"));
                producer.setNation(resultSet.getString("Nation"));
                return producer;
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
    public ArrayList<Vendor> getAllItemVendors() {
        ArrayList<Vendor> vendors = new ArrayList<>();
        String sqlStatement = "SELECT Name, Website, City, Nation FROM tmp.producer;";

        connection = DbConnection.getInstance();
        resultSet = connection.executeQuery(sqlStatement);

        try{
            while(resultSet.next()) {
                Vendor vendor = new ItemVendor();
                vendor.setName(resultSet.getString("Name"));
                vendor.setWebsite(resultSet.getString("Website"));
                vendor.setCity(resultSet.getString("City"));
                vendor.setNation(resultSet.getString("Nation"));
                vendors.add(vendor);
            }
            return vendors;

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
    public void deleteItemInfo(int itemID) {

        String sqlStatement = "DELETE FROM tmp.itemStat WHERE tmp.itemStat.item_ID = '"+itemID+"';";
        connection = DbConnection.getInstance();
        connection.executeUpdate(sqlStatement);
        connection.close();
    }

    @Override
    public void deleteItem(int itemID) {
        String sqlStatement = "DELETE FROM tmp.item WHERE tmp.item.ID = '"+itemID+"';";
        connection = DbConnection.getInstance();
        connection.executeUpdate(sqlStatement);
        connection.close();
    }

    @Override
    public void removeItemVendor(String itemVendorName) {
        String sqlStatement = "DELETE FROM tmp.producer WHERE tmp.producer.Name = '"+itemVendorName+"';";
        connection = DbConnection.getInstance();
        connection.executeUpdate(sqlStatement);
        connection.close();
    }
}
