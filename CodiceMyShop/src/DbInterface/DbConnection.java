package DbInterface;

import java.sql.*;

public class DbConnection implements IDbConnection{
    private static DbUser dbUser = DbUser.getInstance();
    private static DbConnection instance = new DbConnection();
    private static Connection connection;
    private static Statement statement;
    private static ResultSet resultSet;
    private static int rowCount;

    private DbConnection(){
        connection = null;
        statement = null;
        resultSet = null;
        rowCount = 0;
        try {
            Class cls = Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Ho caricato la classe di nome: " + cls.getName());
        } catch (ClassNotFoundException e) {
            System.out.println("Non trovo il driver MySQL JDBC: " + e.getMessage());
        }
    }

    public static DbConnection getInstance() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/" + dbUser.getSchemaName() + "?serverTimezone=UTC", dbUser.getUsername(), dbUser.getPassword());
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        }
        return instance;
    }

    @Override
    public ResultSet executeQuery(String sqlStatement) {
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sqlStatement);
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        }
        return resultSet;
    }

    @Override
    public int executeUpdate(String sqlStatement) {
        try {
            rowCount = 0;
            statement = connection.createStatement();
            rowCount = statement.executeUpdate(sqlStatement);
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        }
        return rowCount;
    }

    @Override
    public void close() {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                System.out.println("SQL Exception: " + e.getMessage());
                System.out.println("SQL State: " + e.getSQLState());
                System.out.println("Vendor Error: " + e.getErrorCode());
            }
            resultSet = null;
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                System.out.println("SQL Exception: " + e.getMessage());
                System.out.println("SQL State: " + e.getSQLState());
                System.out.println("Vendor Error: " + e.getErrorCode());
            }
            statement = null;
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("SQL Exception: " + e.getMessage());
                System.out.println("SQL State: " + e.getSQLState());
                System.out.println("Vendor Error: " + e.getErrorCode());
            }
        }
    }

    @Override
    public int getLastInsertId() {
        int id=0;

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT last_insert_id();");
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Vendor Error: " + e.getErrorCode());
        }

        try{
            resultSet.next();
            if (resultSet.getRow() == 1){
                id = resultSet.getInt("last_insert_id()");
            }
        }catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        } catch (NullPointerException e) {
            System.out.println("Resultset: " + e.getMessage());
        }

        return id;
    }

    @Override
    public Connection getConnection(){
        return connection;
    }
}
