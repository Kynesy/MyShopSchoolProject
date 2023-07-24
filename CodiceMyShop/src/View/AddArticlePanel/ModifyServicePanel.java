package View.AddArticlePanel;

import Model.Category;
import Model.Service;
import Model.Vendor;
import View.Listener.AdminListener;

import java.awt.event.ActionListener;
import java.io.File;

public class ModifyServicePanel extends AddServiceSubpanel{
    private int serviceID;

    public ModifyServicePanel(ActionListener actionListener, Vendor[] serviceVendorArray, Category[] categories) {
        super(actionListener, serviceVendorArray, categories);
        this.topLabel.setText("Form modifica di un Servizio.");
        this.btnCreate.setText("Ricarica il servizio");
        this.btnCreate.setActionCommand("" + AdminListener.CommandKeyAdmin.MODIFY_SERVICE);
    }


    public void setService (Service service){
        name.setText(service.getName());
        this.fotos = service.getFotos();
        this.fotoNumber = fotos.size();
        this.txtFileName.setText("");
        for(File file:fotos){
            this.txtFileName.setText(txtFileName.getText() +" "+ file.getName());
        }
        this.description.setText(service.getDescription());
        this.price.setText(String.valueOf(service.getPrice()));
        serviceID= service.getId();
    }

    public int getServiceID(){
        return serviceID;
    }
}
