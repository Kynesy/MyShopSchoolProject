package View.SxMenu;

import Business.SessionManager;
import Business.UserBusiness;
import Model.User;
import View.Listener.SxListener;
import View.Menu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;



public class SxPanel extends JPanel {
    ActionListener listener;
    public SxPanel(ActionListener listener) {
        this.listener = listener;
        setLayout( new GridLayout(10,1) );
        setBackground(Color.darkGray);

        refresh();
    }

    public void refresh() {
        removeAll();

        User user = SessionManager.getInstance().getSession().get(SessionManager.LoginStatus.ONLINE);

        Menu menu = new SxGuestMenu();

        if(user != null) {
            switch (UserBusiness.getInstance().getUserPrivilege(user)) {
                case USER:
                    menu = new SxUserMenu();
                    break;

                case MANAGER:
                    menu = new SxManagerMenu();
                    break;

                case ADMINISTRATOR:
                    menu = new SxAdminMenu();
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
