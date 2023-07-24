package Business.TableFactory.TableArticleType;

import javax.swing.*;

public class TableTypeUser extends JTable {
    public TableTypeUser(String[][] data) {
        super(data, new String[]{"ID","Nome", "Prezzo", "Fornitore", "Disponibilità", "Tipo"});
        setDefaultEditor(Object.class, null);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
}
