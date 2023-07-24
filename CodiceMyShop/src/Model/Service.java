package Model;

public class Service extends Article{
    private String vendorName;

    public Service() {
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public double getPrice(){
        return price;
    }

    @Override
    public String toString() {
        return "[Nome: "+getName()+" - Prezzo: "+getPrice()+" - Fornitore: "+getVendorName()+"]";
    }
}
