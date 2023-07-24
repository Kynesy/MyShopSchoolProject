package DbInterface;

public class DbUser {
    private static DbUser instance = new DbUser();
    private final String username;
    private final String password;
    private final String schemaName;

    private DbUser(){
        username = ""; //insert dbUsername
        password = ""; //insert dbPassword
        schemaName = "tmp"; //insert dbName
    }

    public static DbUser getInstance() {
        return instance;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getSchemaName() {
        return schemaName;
    }
}
