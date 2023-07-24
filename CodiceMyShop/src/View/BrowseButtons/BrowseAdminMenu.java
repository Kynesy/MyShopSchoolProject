package View.BrowseButtons;

import View.Listener.BrowseListener;
import View.Menu;

import javax.swing.*;
import java.awt.*;

public class BrowseAdminMenu extends Menu {
    public BrowseAdminMenu() {
        JButton btnDetails = new JButton("Dettagli Articolo");
        btnDetails.setActionCommand("" + BrowseListener.CommandKeyBrowse.SHOW_ARTICLE_DETAILS);
        btnDetails.setForeground(Color.white);
        btnDetails.setBackground(Color.gray);
        buttonList.add(btnDetails);

        JButton btnModify = new JButton("Modifica Articolo");
        btnModify.setActionCommand("" + BrowseListener.CommandKeyBrowse.SHOW_ARTICLE_MODIFY);
        btnModify.setForeground(Color.white);
        btnModify.setBackground(Color.gray);
        buttonList.add(btnModify);

        JButton btnDeleteArticle = new JButton("Elimina articolo");
        btnDeleteArticle.setActionCommand("" + BrowseListener.CommandKeyBrowse.DELETE_ARTICLE);
        btnDeleteArticle.setForeground(Color.white);
        btnDeleteArticle.setBackground(Color.gray);
        buttonList.add(btnDeleteArticle);
    }
}
