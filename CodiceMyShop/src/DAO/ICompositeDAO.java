package DAO;

import Model.CompositeItem;
import Model.ItemInterface;

import java.util.List;

public interface ICompositeDAO {

    int addItemToComposite(int itemID, int compositeID );
    int removeItemFromAllComposite(int itemID);
    List<Integer> getCompositeSubitemsID(int compositeID);
    int clearComposite(int compositeID);
    void deleteComposite(int compositeID);
    boolean isComposite(int id);
    CompositeItem getCompositeByID(int id);
}
