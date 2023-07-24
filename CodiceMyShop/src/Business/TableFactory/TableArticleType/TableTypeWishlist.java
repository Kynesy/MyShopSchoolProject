package Business.TableFactory.TableArticleType;

import javax.swing.*;

public class TableTypeWishlist extends JTable {
    public TableTypeWishlist(String[][] data) {
        super(data, new String[]{"ID","Nome", "Prezzo", "Fornitore", "Quantità", "Tipo"});
        setDefaultEditor(Object.class, null);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
}
