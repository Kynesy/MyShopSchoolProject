package Business.TableFactory.TableUsersType;

import javax.swing.*;

public class TableManager extends JTable {
    public TableManager(String[][] data) {
        super(data, new String[]{"Username","Nome", "Abilitato", "Punto vendita"});
        setDefaultEditor(Object.class, null);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
}
