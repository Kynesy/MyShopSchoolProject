package Business.TableFactory.TableUsersType;

import javax.swing.*;

public class TableUser extends JTable {
    public TableUser(String[][] data) {
        super(data, new String[]{"Username","Nome", "Mail", "Abilitato"});
        setDefaultEditor(Object.class, null);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    }
}
