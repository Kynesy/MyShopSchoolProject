package View.BrowseButtons;

import Business.SessionManager;
import Business.UserBusiness;
import Model.User;
import View.Menu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class BrowseButtons extends JPanel {
    private ActionListener listener;

    public BrowseButtons(ActionListener listener) {
        this.listener = listener;
        setLayout( new GridLayout(1, 6));
        setBackground(Color.darkGray);

        refresh();
    }

    public void refresh(){
        User user = SessionManager.getInstance().getSession().get(SessionManager.LoginStatus.ONLINE);
        Menu menu = new BrowseGuestMenu();

        if(user != null) {
            switch (UserBusiness.getInstance().getUserPrivilege(user)) {
                case USER: {
                    menu = new BrowseUserMenu();
                    break;
                }

                case MANAGER: {
                    menu = new BrowseManagerMenu();
                    break;
                }

                case ADMINISTRATOR: {
                    menu = new BrowseAdminMenu();
                    break;
                }
            }
        }

        for(JButton jButton : menu.getButtonList()){
            jButton.addActionListener(listener);
            jButton.setForeground(Color.white);
            jButton.setBackground(Color.gray);
            add(jButton);
        }

        invalidate();
        validate();
        repaint();
    }
}
