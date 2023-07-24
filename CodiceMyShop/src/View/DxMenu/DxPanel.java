package View.DxMenu;

import Business.SessionManager;
import Business.UserBusiness;
import Model.User;
import View.Menu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class DxPanel extends JPanel {
    ActionListener listener;

    public DxPanel(ActionListener listener) {
        this.listener = listener;
        setLayout( new GridLayout(10, 1));
        setBackground(Color.darkGray);
        refresh();

    }

    public void refresh() {
        removeAll();

        Menu menu = new DxGuestMenu();

        User user = SessionManager.getInstance().getSession().get(SessionManager.LoginStatus.ONLINE);

        if(user != null) {
            switch (UserBusiness.getInstance().getUserPrivilege(user)) {
                case USER:
                    menu = new DxUserMenu();
                    break;
                case MANAGER:
                    menu = new DxUserMenu();
                    break;
                case ADMINISTRATOR:
                    menu = new DxUserMenu();
                    break;
            }
        }

        for(JButton btn : menu.getButtonList()){
            btn.addActionListener(listener);
            btn.setForeground(Color.white);
            btn.setBackground(Color.gray);
            add(btn);
        }

        invalidate();
        validate();
        repaint();
    }
}
