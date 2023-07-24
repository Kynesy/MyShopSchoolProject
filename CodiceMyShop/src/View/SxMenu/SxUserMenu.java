package View.SxMenu;

import View.Listener.SxListener;
import View.Menu;

import javax.swing.*;
import java.awt.*;

public class SxUserMenu extends Menu {
    public SxUserMenu() {
        JButton btnBrowse = new JButton("Esplora Catalogo");
        btnBrowse.setActionCommand("" + SxListener.CommandKeySx.BROWSE);
        btnBrowse.setForeground(Color.white);
        btnBrowse.setBackground(Color.gray);
        buttonList.add(btnBrowse);

        JButton btnShoppingList = new JButton("Le tue Liste");
        btnShoppingList.setActionCommand("" + SxListener.CommandKeySx.SHOW_WISHLIST);
        btnShoppingList.setForeground(Color.white);
        btnShoppingList.setBackground(Color.gray);
        buttonList.add(btnShoppingList);
    }
}
