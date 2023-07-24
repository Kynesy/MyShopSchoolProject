package View.SxMenu;

import View.Listener.SxListener;
import View.Menu;

import javax.swing.*;
import java.awt.*;

public class SxManagerMenu extends Menu {
    public SxManagerMenu() {
        JButton btnBrowse = new JButton("Esplora Catalogo");
        btnBrowse.setActionCommand("" + SxListener.CommandKeySx.BROWSE);
        btnBrowse.setForeground(Color.white);
        btnBrowse.setBackground(Color.gray);
        buttonList.add(btnBrowse);

        JButton btnSendEmail = new JButton("Invia una Email");
        btnSendEmail.setActionCommand("" + SxListener.CommandKeySx.SHOW_MAIL_CREATION);
        btnSendEmail.setForeground(Color.white);
        btnSendEmail.setBackground(Color.gray);
        buttonList.add(btnSendEmail);

        JButton btnManagerUsers = new JButton("Gestisci utenti");
        btnManagerUsers.setActionCommand("" + SxListener.CommandKeySx.SHOW_USERS);
        btnManagerUsers.setForeground(Color.white);
        btnManagerUsers.setBackground(Color.gray);
        buttonList.add(btnManagerUsers);

        JButton btnOrders = new JButton("Gestisci ordini");
        btnOrders.setActionCommand("" + SxListener.CommandKeySx.SHOW_ORDERS);
        btnOrders.setForeground(Color.white);
        btnOrders.setBackground(Color.gray);
        buttonList.add(btnOrders);

        JButton btnPrenotations = new JButton("Gestisci prenotazioni");
        btnPrenotations.setActionCommand("" + SxListener.CommandKeySx.SHOW_PRENOTATIONS);
        btnPrenotations.setForeground(Color.white);
        btnPrenotations.setBackground(Color.gray);
        buttonList.add(btnPrenotations);
    }
}
