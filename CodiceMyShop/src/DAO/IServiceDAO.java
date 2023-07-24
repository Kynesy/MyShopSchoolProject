package DAO;

import Model.*;

import java.io.File;
import java.util.ArrayList;

public interface IServiceDAO {
    int createService(Service service);
    boolean serviceExist(int id);
    Service getService(int id);
    ArrayList<Service> getAllServices();

    int addServiceVendor(ServiceVendor serviceVendor);
    boolean serviceVendorExist(String name);
    ServiceVendor getServiceVendor(String name);
    ArrayList<Vendor> getAllServiceVendors();

    int modifyService(Service service);

    void deleteServiceVendor(String name);
    void deleteService(int serviceID);
}
