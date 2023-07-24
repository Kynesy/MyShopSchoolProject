package Model;

public class Prenotation {
    private String username;
    private int articleID;
    private int quantity;
    private int ID;

    public Prenotation(String username, int articleID, int quantity) {
        this.username = username;
        this.articleID = articleID;
        this.quantity = quantity;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getUsername() {
        return username;
    }

    public int getArticleID() {
        return articleID;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "Prenotation{" +
                "username='" + username + '\'' +
                ", articleID=" + articleID +
                ", quantity=" + quantity +
                '}';
    }
}
