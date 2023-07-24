package Model;

public class Order {
    private String orderID;
    private String username;
    private int paidStatus;

    public Order(String orderID, String username, int paidStatus) {
        this.orderID = orderID;
        this.username = username;
        this.paidStatus = paidStatus;
    }

    public void setPaidStatus(int paidStatus) {
        this.paidStatus = paidStatus;
    }

    public String getOrderID() {
        return orderID;
    }

    public String getUsername() {
        return username;
    }

    public int getPaidStatus() {
        return paidStatus;
    }

    @Override
    public String toString() {
        return "[ID:"+orderID+" - Username: "+username+" - PaidStatus: "+paidStatus+"]";
    }
}
