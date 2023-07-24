package View.AddArticlePanel;

import Model.Category;
import Model.Item;
import Model.Store;
import Model.Vendor;
import View.Listener.AdminListener;

import java.awt.event.ActionListener;
import java.io.File;

public class ModifyItemPanel extends AddItemSubpanel{
    private int itemID;


    public ModifyItemPanel(ActionListener actionListener, Store[] storeCitiesArray, Vendor[] itemVendorsArray, Category[] categories) {
        super(actionListener, storeCitiesArray, itemVendorsArray, categories);
        this.topLabel.setText("Form modifica di un Prodotto.");
        this.btnCreate.setText("Ricarica il prodotto");
        this.btnCreate.setActionCommand("" + AdminListener.CommandKeyAdmin.MODIFY_ITEM);
    }

    public void setItem (Item item){
        name.setText(item.getName());
        this.fotos = item.getFotos();
        this.fotoNumber = fotos.size();
        this.txtFileName.setText("");
        for(File file:fotos){
            this.txtFileName.setText(txtFileName.getText() +" "+ file.getName());
        }
        this.description.setText(item.getDescription());
        this.shelf.setText(String.valueOf(item.getShelf()));
        this.line.setText(String.valueOf(item.getLine()));
        this.price.setText(String.valueOf(item.getPrice()));
        itemID= item.getID();
    }

    public int getItemID(){
        return itemID;
    }
}
