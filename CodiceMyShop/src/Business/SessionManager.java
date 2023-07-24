package Business;

import Model.User;

import java.util.HashMap;

public class SessionManager {
    private static SessionManager instance = new SessionManager();
    public enum LoginStatus {ONLINE}

    private HashMap<LoginStatus, User> session = new HashMap<>();

    private SessionManager(){

    }

    public static SessionManager getInstance() {
        return instance;
    }

    public HashMap<LoginStatus, User> getSession() {
        return session;
    }
}
