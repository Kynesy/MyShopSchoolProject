package DAO;

import Business.ArticleFactory.AbstractArticleFactory;
import Business.ArticleFactory.ArticleFactoryProvider;
import DbInterface.DbConnection;
import DbInterface.IDbConnection;
import Model.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ServiceDAO implements IServiceDAO{
    private static ServiceDAO instance = new ServiceDAO();
    private static IDbConnection connection;
    private static ResultSet resultSet;
    private AbstractArticleFactory articleFactory;

    private ServiceDAO(){
        connection = null;
        resultSet = null;
        articleFactory = ArticleFactoryProvider.getArticleFactory(ArticleFactoryProvider.ArticleType.SERVICE);
    }

    public static ServiceDAO getInstance(){
        return instance;
    }

    @Override
    public int createService(Service service) {//restituisce l'id se l'inserimento è andato a buon fine, altrimenti -1
        connection = DbConnection.getInstance();

        String sqlStatement1 = "INSERT INTO tmp.Article (Name, Description, Price, idCategory) " +
                "VALUES ('"+ service.getName()+"', '"+ service.getDescription()+"', '"+ service.getPrice() +"'," +
                "'"+service.getCategory().getId()+"');";

        int r1 = connection.executeUpdate(sqlStatement1);
        int id = connection.getLastInsertId();

        String sqlStatement2 = "INSERT INTO tmp.service " +
                "VALUES ('"+ id +"', '"+ service.getVendorName() + "');";


        int r2 = connection.executeUpdate(sqlStatement2);
        connection.close();

        if(r1 + r2 == 2){
            return id;
        }else {
            return -1;
        }
    }

    @Override
    public int modifyService(Service service) {
        connection = DbConnection.getInstance();

        String sqlStatement1 = "UPDATE tmp.Article SET Name = '"+service.getName()+"', " +
                "Description = '"+service.getDescription()+"', " +
                "Price='"+service.getPrice()+"', " +
                "idCategory= '"+service.getCategory().getId()+"' " +
                "WHERE tmp.Article.ID = '"+service.getId()+"';";

        int r1 = connection.executeUpdate(sqlStatement1);

        String sqlStatement2 = "UPDATE tmp.service SET Vendor='"+service.getVendorName()+"'  " +
                "WHERE tmp.service.ID ='"+service.getId()+"';";

        int r2 = connection.executeUpdate(sqlStatement2);
        connection.close();

        if(r1 + r2 == 2){
            return 1;
        }else {
            return -1;
        }
    }

    @Override
    public boolean serviceExist(int id) {
        String sqlStatement = "SELECT ID FROM tmp.Service WHERE ID = '" + id +"';";
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
    public Service getService(int id){
        Service service = (Service) articleFactory.create();
        String sqlStatement = "SELECT tmp.Article.*, tmp.Service.Vendor " +
                "FROM tmp.Article INNER JOIN tmp.Service " +
                "ON tmp.Article.ID = tmp.service.ID " +
                "WHERE tmp.Article.ID = '"+ id + "';";
        connection = DbConnection.getInstance();
        resultSet = connection.executeQuery(sqlStatement);

        try {
            resultSet.next();
            if (resultSet.getRow() == 1) {
                service.setId(resultSet.getInt("ID"));
                service.setName(resultSet.getString("Name"));
                service.setPrice(resultSet.getDouble("Price"));
                service.setDescription(resultSet.getString("Description"));
                service.setVendorName(resultSet.getString("vendor"));

                return service;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            connection.close();
        }
        return null;
    }

    @Override
    public ArrayList<Service> getAllServices() {
        connection = DbConnection.getInstance();
        ArrayList<Service> services = new ArrayList<>();

        String sqlStatement = "SELECT tmp.article.ID, tmp.article.Name, tmp.article.Description,tmp.article.Price, tmp.service.Vendor" +
                " FROM tmp.article INNER JOIN tmp.service ON tmp.article.ID = tmp.service.ID;";

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
    public int addServiceVendor(ServiceVendor serviceVendor) {
        connection = DbConnection.getInstance();

        String sqlStatement = "INSERT INTO tmp.vendor VALUES ('"+ serviceVendor.getName() +"', " +
                "'" + serviceVendor.getWebsite() +"', " +
                "'" + serviceVendor.getCity() + "', " +
                "'" + serviceVendor.getNation() + "')";

        int rowCount = connection.executeUpdate(sqlStatement);
        connection.close();

        return rowCount;
    }

    @Override
    public boolean serviceVendorExist(String name) {
        String sqlStatement = "SELECT Name FROM tmp.vendor WHERE Name = '" + name +"';";
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
    public ServiceVendor getServiceVendor(String name) {
        ServiceVendor serviceVendor = new ServiceVendor();
        connection = DbConnection.getInstance();

        String sqlStatement = "SELECT tmp.Vendor.* FROM tmp.Vendor WHERE tmp.Vendor.Name = '"+ name +"';";
        resultSet = connection.executeQuery(sqlStatement);

        try{
            resultSet.next();
            if(resultSet.getRow()==1){
                serviceVendor.setName(resultSet.getString("Name"));
                serviceVendor.setWebsite(resultSet.getString("Website"));
                serviceVendor.setCity(resultSet.getString("City"));
                serviceVendor.setNation(resultSet.getString("Nation"));
                return serviceVendor;
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
    public ArrayList<Vendor> getAllServiceVendors() {
        ArrayList<Vendor> vendors = new ArrayList<>();
        String sqlStatement = "SELECT Name, Website, City, Nation FROM tmp.vendor;";

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
    public void deleteService(int serviceID) {
        String sqlStatement = "DELETE FROM tmp.service WHERE tmp.service.ID = '"+serviceID+"';";
        connection = DbConnection.getInstance();
        connection.executeUpdate(sqlStatement);
        connection.close();
    }

    @Override
    public void deleteServiceVendor(String name) {
        String sqlStatement = "DELETE FROM tmp.vendor WHERE tmp.vendor.Name = '"+name+"';";
        connection = DbConnection.getInstance();
        connection.executeUpdate(sqlStatement);
        connection.close();
    }
}
