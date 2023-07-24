package Business.TableFactory.TableArticleType;

import javax.swing.*;

public class TableTypeAdmin extends JTable {
    public TableTypeAdmin(String[][] data) {
        super(data, new String[]{"ID","Nome", "Prezzo", "Fornitore","Punto Vendita", "Tipo"});
        setDefaultEditor(Object.class, null);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
}
