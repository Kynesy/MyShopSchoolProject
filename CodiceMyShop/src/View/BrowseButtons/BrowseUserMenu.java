package View.BrowseButtons;

import View.Listener.BrowseListener;
import View.Menu;

import javax.swing.*;
import java.awt.*;

public class BrowseUserMenu extends Menu {
    public BrowseUserMenu() {
        JButton btnDetails = new JButton("Dettagli Articolo");
        btnDetails.setActionCommand("" + BrowseListener.CommandKeyBrowse.SHOW_ARTICLE_DETAILS);
        btnDetails.setForeground(Color.white);
        btnDetails.setBackground(Color.gray);
        buttonList.add(btnDetails);

        JButton btnAddToWishlist = new JButton("Aggiungi alla lista...");
        btnAddToWishlist.setActionCommand("" + BrowseListener.CommandKeyBrowse.ADD_ARTICLE_TO_WISHLIST);
        btnAddToWishlist.setForeground(Color.white);
        btnAddToWishlist.setBackground(Color.gray);
        buttonList.add(btnAddToWishlist);

        JButton btnCreateWishlist = new JButton("Crea una lista");
        btnCreateWishlist.setActionCommand("" + BrowseListener.CommandKeyBrowse.CREATE_WISHLIST);
        btnCreateWishlist.setForeground(Color.white);
        btnCreateWishlist.setBackground(Color.gray);
        buttonList.add(btnCreateWishlist);
    }
}
