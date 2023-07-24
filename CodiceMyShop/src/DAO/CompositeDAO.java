package DAO;

import Business.ArticleFactory.AbstractArticleFactory;
import Business.ArticleFactory.ArticleFactoryProvider;
import Business.ArticleFactory.CompositeItemFactory;
import DbInterface.DbConnection;
import DbInterface.IDbConnection;
import Model.CompositeItem;
import Model.Item;
import Model.ItemInterface;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CompositeDAO implements ICompositeDAO{
    private static CompositeDAO istance = new CompositeDAO();
    private static IDbConnection connection;
    private static ResultSet resultSet;
    private AbstractArticleFactory articleFactory;

    private CompositeDAO(){
        connection = null;
        resultSet = null;
        articleFactory = new CompositeItemFactory();
    }

    public static CompositeDAO getIstance() {
        return istance;
    }

    @Override
    public boolean isComposite(int id) {
        String sqlStatement = "SELECT tmp.compositeitem.itemID FROM tmp.compositeitem " +
                "WHERE tmp.compositeitem.itemID = '"+ id +"';";
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
    public int addItemToComposite(int itemID, int compositeID) {
        connection = DbConnection.getInstance();
        String sqlStatement="INSERT INTO tmp.compositeitem VALUES ('"+ compositeID +"', '"+ itemID +"');";
        int i = connection.executeUpdate(sqlStatement);
        connection.close();
        return i;
    }

    @Override
    public int removeItemFromAllComposite(int itemID) {
        connection = DbConnection.getInstance();
        String sqlStatement="DELETE FROM tmp.compositeitem WHERE tmp.compositeitem.subItemID = '"+ itemID +"';";
        int i = connection.executeUpdate(sqlStatement);
        connection.close();
        return i;
    }

    @Override
    public void deleteComposite(int compositeID) {
        connection = DbConnection.getInstance();
        String sqlStatement="DELETE FROM tmp.compositeitem WHERE tmp.compositeitem.itemID = '"+ compositeID +"';";
        connection.executeUpdate(sqlStatement);
        connection.close();
    }

    @Override
    public CompositeItem getCompositeByID(int id){
        CompositeItem item = (CompositeItem) articleFactory.create();
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
            item.setItemVendor(ItemDAO.getInstance().getItemVendor(id));

            return item;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            connection.close();
        }
        return null;
    }

    @Override
    public List<Integer> getCompositeSubitemsID(int compositeID) {
        connection = DbConnection.getInstance();
        List<Integer> list = new ArrayList<>();
        String sqlStatement= "SELECT tmp.compositeitem.subItemID " +
                "FROM tmp.compositeitem " +
                "WHERE tmp.compositeitem.itemID = '"+ compositeID +"'";
        resultSet = connection.executeQuery(sqlStatement);
        try{
            while (resultSet.next()){
                int id = resultSet.getInt("subItemID");
                list.add(id);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            connection.close();
        }
        return list;
    }

    @Override
    public int clearComposite(int compositeID) {
        connection = DbConnection.getInstance();
        String sqlStatement = "DELETE FROM tmp.compositeitem WHERE tmp.compositeitem.itemID = '"+ compositeID +"';";
        int i = connection.executeUpdate(sqlStatement);
        connection.close();
        return i;
    }


}
