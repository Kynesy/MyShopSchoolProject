package View.AddArticlePanel;

import Model.*;
import View.Listener.AdminListener;

import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

public class ModifyComposite extends AddCompositeSubpanel{
    private int compositeID;

    public ModifyComposite(ActionListener actionListener, ArrayList<Item> items, Store[] storeCitiesArray, Vendor[] itemVendorsArray, Category[] categories) {
        super(actionListener, items, storeCitiesArray, itemVendorsArray, categories);
        this.topLabel.setText("Form modifica di un Prodotto composito.");
        this.btnCreate.setText("Ricarica il composite");
        this.btnCreate.setActionCommand("" + AdminListener.CommandKeyAdmin.MODIFY_COMPOSITE);
    }


    public void setItem (CompositeItem compositeItem){
        name.setText(compositeItem.getName());
        this.fotos = compositeItem.getFotos();
        this.fotoNumber = fotos.size();
        this.txtFileName.setText("");
        for(File file:fotos){
            this.txtFileName.setText(txtFileName.getText() +" "+ file.getName());
        }
        this.description.setText(compositeItem.getDescription());
        this.shelf.setText(String.valueOf(compositeItem.getShelf()));
        this.line.setText(String.valueOf(compositeItem.getLine()));
        compositeID = compositeItem.getID();
    }

    public int getCompositeID(){
        return compositeID;
    }
}
