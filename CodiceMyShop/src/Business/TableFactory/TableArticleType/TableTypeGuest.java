package Business.TableFactory.TableArticleType;

import javax.swing.*;

public class TableTypeGuest extends JTable {
    public TableTypeGuest(String[][] data) {
        super(data, new String[]{"ID","Nome", "Prezzo","Punto vendita", "Tipo"});
        setDefaultEditor(Object.class, null);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
}
