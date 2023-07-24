package View.SxMenu;

import View.Listener.SxListener;
import View.Menu;

import javax.swing.*;
import java.awt.*;

public class SxGuestMenu extends Menu {
    public SxGuestMenu() {
        JButton btnBrowse = new JButton("Esplora Catalogo");
        btnBrowse.setActionCommand("" + SxListener.CommandKeySx.BROWSE);
        btnBrowse.setForeground(Color.white);
        btnBrowse.setBackground(Color.gray);
        buttonList.add(btnBrowse);

    }
}
