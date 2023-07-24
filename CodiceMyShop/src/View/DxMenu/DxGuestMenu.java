package View.DxMenu;

import View.Listener.DxListener;
import View.Menu;

import javax.swing.*;
import java.awt.*;

public class DxGuestMenu extends Menu {
    public DxGuestMenu() {
        JButton btnRegistrazione = new JButton("Registrati qui");
        btnRegistrazione.setActionCommand(""+ DxListener.CommandKeyDx.SHOW_REGISTRATION);
        btnRegistrazione.setForeground(Color.white);
        btnRegistrazione.setBackground(Color.gray);
        buttonList.add(btnRegistrazione);

        JButton btnLogin = new JButton("Accedi");
        btnLogin.setActionCommand("" + DxListener.CommandKeyDx.SHOW_LOGIN);
        btnLogin.setForeground(Color.white);
        btnLogin.setBackground(Color.gray);
        buttonList.add(btnLogin);
    }
}
