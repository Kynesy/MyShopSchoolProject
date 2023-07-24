package View.BrowseButtons;

import View.Listener.BrowseListener;
import View.Menu;

import javax.swing.*;
import java.awt.*;

public class BrowseGuestMenu extends Menu {
    public BrowseGuestMenu() {
        JButton btnDetails = new JButton("Dettagli Articolo");
        btnDetails.setActionCommand("" + BrowseListener.CommandKeyBrowse.SHOW_ARTICLE_DETAILS);
        btnDetails.setForeground(Color.white);
        btnDetails.setBackground(Color.gray);
        buttonList.add(btnDetails);
    }
}
