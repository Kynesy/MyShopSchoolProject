package View.DxMenu;

import View.Listener.DxListener;
import View.Menu;

import javax.swing.*;
import java.awt.*;

public class DxUserMenu extends Menu {
    public DxUserMenu() {

        JButton btnModifyPersonalData = new JButton("Dati Personali");
        btnModifyPersonalData.setActionCommand("" + DxListener.CommandKeyDx.SHOW_PERSONAL_DATA);
        btnModifyPersonalData.setForeground(Color.white);
        btnModifyPersonalData.setBackground(Color.gray);
        buttonList.add(btnModifyPersonalData);

        JButton btnDeleteAccount = new JButton("Elimina Account");
        btnDeleteAccount.setActionCommand("" + DxListener.CommandKeyDx.DELETE_PERSONAL_ACCOUNT);
        btnDeleteAccount.setForeground(Color.white);
        btnDeleteAccount.setBackground(Color.gray);
        buttonList.add(btnDeleteAccount);

        JButton btnLogout = new JButton("Esci");
        btnLogout.setActionCommand(""+ DxListener.CommandKeyDx.LOGOUT);
        btnLogout.setForeground(Color.white);
        btnLogout.setBackground(Color.gray);
        buttonList.add(btnLogout);

    }
}
