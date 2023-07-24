package Model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CompositeItem extends Item implements ItemInterface{
    private List<ItemInterface> subItemList;

    public CompositeItem(){
        subItemList = new ArrayList<>();
    }

    public void add(List<ItemInterface> subItems){
        subItemList.addAll(subItems);
    }

    public List<ItemInterface> getSubItemList(){
        return subItemList;
    }

    public void add(ItemInterface item){
        if( ((Article) item).getId() == this.getId() ){
            return;
        }else {
            subItemList.add(item);
        }
    }

    public void setSubItemList(List<ItemInterface> subItemList) {
        this.subItemList = subItemList;
        double p = 0;
        Iterator<ItemInterface> iterator = subItemList.iterator();
        while (iterator.hasNext()){
            ItemInterface item = iterator.next();
            p = p + item.getPrice();
        }
        price=p;
    }

    public void clear(){
        subItemList.clear();
    }

    public void remove(ItemInterface item){
        int id = ((Article) item).getId();
         subItemList.removeIf(i -> id == ((Article) i).getId());
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public int getID() {
        return getId();
    }

}
