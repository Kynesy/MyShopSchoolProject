package View.BrowseButtons;

import View.Listener.BrowseListener;
import View.Menu;

import javax.swing.*;
import java.awt.*;

public class BrowseManagerMenu extends Menu {
    public BrowseManagerMenu() {
        JButton btnDetails = new JButton("Dettagli Articolo");
        btnDetails.setActionCommand("" + BrowseListener.CommandKeyBrowse.SHOW_ARTICLE_DETAILS);
        btnDetails.setForeground(Color.white);
        btnDetails.setBackground(Color.gray);
        buttonList.add(btnDetails);

        JButton btnAvaiability = new JButton("Modifica disponibilità");
        btnAvaiability.setActionCommand("" + BrowseListener.CommandKeyBrowse.UPDATE_AVAIABILITY);
        btnAvaiability.setForeground(Color.white);
        btnAvaiability.setBackground(Color.gray);
        buttonList.add(btnAvaiability);
    }
}
