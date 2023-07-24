package View.SxMenu;

import View.Listener.SxListener;
import View.Menu;

import javax.swing.*;
import java.awt.*;
//colored

public class SxAdminMenu extends Menu {
    public SxAdminMenu() {

        JButton btnBrowse = new JButton("Esplora Catalogo");
        btnBrowse.setActionCommand("" + SxListener.CommandKeySx.BROWSE);
        btnBrowse.setForeground(Color.white);
        btnBrowse.setBackground(Color.gray);
        buttonList.add(btnBrowse);

        JButton btnAddArticle = new JButton("Inserisci un Articolo");
        btnAddArticle.setActionCommand("" + SxListener.CommandKeySx.SHOW_ADD_ARTICLE);
        btnAddArticle.setForeground(Color.white);
        btnAddArticle.setBackground(Color.gray);
        buttonList.add(btnAddArticle);

        JButton btnAddCategory = new JButton("Inserisci una Categoria");
        btnAddCategory.setActionCommand("" +  SxListener.CommandKeySx.SHOW_ADD_CATEGORY);
        btnAddCategory.setForeground(Color.white);
        btnAddCategory.setBackground(Color.gray);
        buttonList.add(btnAddCategory);

        JButton btnAddProducer = new JButton("Inserisci un Produttore/Fornitore"); //il produttore offre prodotti, il fornitore servizi
        btnAddProducer.setActionCommand("" + SxListener.CommandKeySx.SHOW_CREATE_VENDOR);
        btnAddProducer.setForeground(Color.white);
        btnAddProducer.setBackground(Color.gray);
        buttonList.add(btnAddProducer);

        JButton btnAddStore = new JButton("Registra un punto vendita");
        btnAddStore.setActionCommand("" + SxListener.CommandKeySx.SHOW_ADD_STORE);
        btnAddStore.setForeground(Color.white);
        btnAddStore.setBackground(Color.gray);
        buttonList.add(btnAddStore);

        JButton btnSeeManager = new JButton("Aggiungi un Manager");
        btnSeeManager.setActionCommand("" + SxListener.CommandKeySx.SHOW_ADD_MANAGER);
        btnSeeManager.setForeground(Color.white);
        btnSeeManager.setBackground(Color.gray);
        buttonList.add(btnSeeManager);

        JButton btnRemoveManager = new JButton("Gestisci Manager");
        btnRemoveManager.setActionCommand("" + SxListener.CommandKeySx.SHOW_USERS);
        btnRemoveManager.setForeground(Color.white);
        btnRemoveManager.setBackground(Color.gray);
        buttonList.add(btnRemoveManager);


    }
}
