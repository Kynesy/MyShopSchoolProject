package DbInterface;
import java.io.File;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface IDbConnection {
        ResultSet executeQuery(String sqlStatement);
        int executeUpdate(String sqlStatement);
        void close();
        int getLastInsertId();
        Connection getConnection();
}
