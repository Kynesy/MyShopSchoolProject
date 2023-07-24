package Business.TableFactory.TableArticleType;

import javax.swing.*;

public class TableTypeManager extends JTable {
    public TableTypeManager(String[][] data) {
        super(data, new String[]{"ID","Nome", "Prezzo","Disponibilità", "Tipo"});
        setDefaultEditor(Object.class, null);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
}
