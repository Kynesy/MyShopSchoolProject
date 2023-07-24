package DAO;


import DbInterface.DbConnection;
import DbInterface.IDbConnection;
import Model.*;


import java.io.*;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class ArticleDAO implements IArticleDAO{
    private static ArticleDAO instance = new ArticleDAO();
    private static IDbConnection connection;
    private static ResultSet resultSet;

    private ArticleDAO(){
        connection = null;
        resultSet = null;
    }

    public static ArticleDAO getInstance() {
        return instance;
    }

    @Override
    public boolean articleExist(int id) {
        String sqlStatement = "SELECT ID FROM tmp.Article WHERE ID = '" + id +"';";
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


    private byte[] fileToByte (File file){
        try {
            FileInputStream fis = new FileInputStream(file);
            return fis.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int addFoto(int articleID, File image){
        String sqlStatement = "INSERT INTO tmp.foto (Foto, article_ID) VALUES (?,?)";
        int row = 0;
        try {
            connection = DbConnection.getInstance();
            PreparedStatement preparedStatement = connection.getConnection().prepareStatement(sqlStatement);
            preparedStatement.setBytes(1, fileToByte(image));
            preparedStatement.setInt(2, articleID);

            row = preparedStatement.executeUpdate();
            System.out.println("immagine caricata correttamente sul db");
            return row;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            connection.close();
        }
        return row;
    }

    @Override
    public ArrayList<File> getFotosFromDB(int articleId){
        String sqlStatement = "SELECT Foto FROM tmp.foto WHERE article_ID = ?";

        try {
            connection = DbConnection.getInstance();
            PreparedStatement preparedStatement = connection.getConnection().prepareStatement(sqlStatement);
            preparedStatement.setInt(1, articleId);
            resultSet = preparedStatement.executeQuery();

            ArrayList<File> files = new ArrayList<>();
            int index = 0;

            while (resultSet.next()) {
                File file = new File("tmpFoto" + index + ".jpg");
                index++;
                Blob b = resultSet.getBlob("Foto");
                byte bytes[] = b.getBytes(1, (int) b.length());
                FileOutputStream fout = new FileOutputStream(file);
                fout.write(bytes);
                fout.close();
                files.add(file);
            }
            connection.close();
            return files;

        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
        } finally {
            connection.close();
        }
        return null;
    }

    @Override
    public int createFeedback(int index, String message, int articleId, String username) {
        String sqlStatement = "INSERT INTO tmp.feedback (article_ID, user_Username, Value, Text) " +
                "VALUES ('"+articleId+"', '"+username+"', '"+index+"', '"+message+"');";
        connection = DbConnection.getInstance();
        int row = connection.executeUpdate(sqlStatement);
        connection.close();
        return row;
    }

    @Override
    public ArrayList<Feedback> getAllFeedbackByArticleId(int articleID) {
        ArrayList<Feedback> result = new ArrayList<>();
        String sqlStatement = "SELECT tmp.feedback.* FROM tmp.feedback WHERE tmp.feedback.article_ID = '"+articleID+"';";
        connection = DbConnection.getInstance();
        resultSet = connection.executeQuery(sqlStatement);
        try{
            while (resultSet.next()){
                int vote = resultSet.getInt("Value");
                String user = resultSet.getString("user_Username");
                String message = resultSet.getString("Text");
                Feedback f = new Feedback(vote, message, user);
                result.add(f);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            connection.close();
        }
        return result;
    }

    @Override
    public int deleteAllFotos(int articleID) {
        String sqlStatement = "DELETE FROM tmp.foto WHERE tmp.foto.article_ID = '"+articleID+"';";
        connection = DbConnection.getInstance();
        int row = connection.executeUpdate(sqlStatement);
        connection.close();
        if(row>=0){
            return 1;
        }else {
            return 0;
        }
    }

    @Override
    public int deleteServiceFromFeedbacks(int serviceID) {
        String sqlStatement = "DELETE FROM tmp.feedback WHERE tmp.feedback.article_ID = '"+serviceID+"';";
        connection = DbConnection.getInstance();
        int row = connection.executeUpdate(sqlStatement);
        connection.close();
        return row;
    }

    @Override
    public int deleteArticle(int serviceID) {
        String sqlStatement = "DELETE FROM tmp.article WHERE tmp.article.ID = '"+serviceID+"';";
        connection = DbConnection.getInstance();
        int row = connection.executeUpdate(sqlStatement);
        connection.close();
        return row;
    }

    @Override
    public int deleteServiceFromAllWishlist(int serviceID) {
        String sqlStatement = "DELETE FROM tmp.itemList WHERE tmp.itemList.article_ID = '"+serviceID+"';";
        connection = DbConnection.getInstance();
        int row = connection.executeUpdate(sqlStatement);
        connection.close();
        return row;
    }

    @Override
    public int removeAllUserFeedbacks(String selectedUsername) {
        connection = DbConnection.getInstance();
        String sqlStatement =  "DELETE FROM tmp.feedback WHERE user_Username = '" + selectedUsername + "';";
        int rowCount = connection.executeUpdate(sqlStatement) ;
        connection.close();
        return rowCount;
    }


    @Override
    public int addCategory(Category category) {
        String sqlStatement = "INSERT INTO tmp.category (category.name) VALUES ('"+ category.getName() + "');";
        connection = DbConnection.getInstance();
        connection.executeUpdate(sqlStatement);
        int id = connection.getLastInsertId();
        connection.close();
        return id;
    }

    @Override
    public int addCategory(Category category, int parentId) {
        String sqlStatement = "INSERT INTO tmp.category (category.name, category.idParentCategory) " +
                "VALUES ('"+ category.getName() +"', '"+ parentId +"');";
        connection = DbConnection.getInstance();
        connection.executeUpdate(sqlStatement);
        int id = connection.getLastInsertId();
        connection.close();
        return id;
    }

    @Override
    public boolean categoryExist(Category category) {
        String sqlStatement;
        if(category.getParentId() == -1) {
            sqlStatement = "SELECT * FROM tmp.category WHERE name='"+ category.getName() +"';";
        }else {
            sqlStatement = "SELECT * FROM tmp.category WHERE name='"+ category.getName() +"'" +
                    " AND idParentCategory = '"+ category.getParentId() +"';";
        }

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
    public int removeCategory(int id) {
        String sqlStatement = "DELETE FROM tmp.category WHERE category.idCategory = '"+ id +"';";
        connection = DbConnection.getInstance();
        int i = connection.executeUpdate(sqlStatement);
        connection.close();
        return i;
    }

    @Override
    public int getParentId(int id) { //ritorna -1 se non ha categoria padre
        String sqlStatement = "SELECT tmp.category.idCategory, tmp.category.idParentCategory FROM tmp.category " +
                "WHERE idCategory = '"+ id +"';";
        connection = DbConnection.getInstance();
        resultSet = connection.executeQuery(sqlStatement);

        try {
            resultSet.next();
            return resultSet.getInt("idParentCategory");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return -1;
    }

    @Override
    public ArrayList<Category> getSubCategory(int id) {
        String sqlStatement = "SELECT tmp.category.idCategory, tmp.category.name, tmp.category.idParentCategory " +
                "FROM tmp.category WHERE idParentCategory = '"+ id +"';";
        connection = DbConnection.getInstance();
        resultSet = connection.executeQuery(sqlStatement);
        ArrayList<Category> categories = new ArrayList<>();
        try {
            while (resultSet.next()){
                Category cat = new Category();
                cat.setId(resultSet.getInt("idCategory"));
                cat.setName(resultSet.getString("name"));
                cat.setParentId(id);

                categories.add(cat);
            }
            return categories;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public Category getCategory(int id) {
        String sqlStatement = "SELECT tmp.category.idCategory, tmp.category.name, tmp.category.idParentCategory " +
                "FROM tmp.category WHERE idCategory = '"+ id + "';";

        connection = DbConnection.getInstance();
        resultSet = connection.executeQuery(sqlStatement);

        Category cat = new Category();

        try {
            resultSet.next();
            cat.setName(resultSet.getString("name"));
            cat.setId(id);
            cat.setParentId(resultSet.getInt("idParentCategory"));

            return cat;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public Category getCategoryByID(int itemID) {
        String sqlStatement = "SELECT tmp.category.idCategory, tmp.category.name, tmp.category.idParentCategory " +
                "FROM tmp.category INNER JOIN tmp.article ON tmp.category.idCategory = tmp.article.idCategory " +
                "WHERE tmp.article.ID = '"+ itemID +"';";

        connection = DbConnection.getInstance();
        resultSet = connection.executeQuery(sqlStatement);

        Category cat = new Category();

        try {
            resultSet.next();
            cat.setName(resultSet.getString("name"));
            cat.setId(resultSet.getInt("idCategory"));
            cat.setParentId(resultSet.getInt("idParentCategory"));
            return cat;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<Category> getAllCategories() {
        ArrayList<Category> c = new ArrayList<>();
        connection = DbConnection.getInstance();
        String sqlStatement = "SELECT tmp.category.idCategory, tmp.category.name FROM tmp.category;";
        resultSet = connection.executeQuery(sqlStatement);

        try{
            while (resultSet.next()){
                Category cat = new Category();
                cat.setId(resultSet.getInt("idCategory"));
                cat.setName(resultSet.getString("name"));
                c.add(cat);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        connection.close();
        return c;
    }

}
