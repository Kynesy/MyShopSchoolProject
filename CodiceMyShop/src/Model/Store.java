package Model;

public class Store implements Comparable{
    private String city;

    public Store() {
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public int compareTo(Object o) {
        Store other = (Store) o;
        return this.getCity().compareTo(other.getCity());
    }

    @Override
    public String toString() {
        return this.getCity();
    }
}
