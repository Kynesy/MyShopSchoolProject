package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class TopPanel extends JPanel {
    ActionListener listener;

    public TopPanel(ActionListener listener) {
        this.listener = listener;
        setLayout( new FlowLayout() );
        setBackground(Color.darkGray);

        JLabel lblWelcome = new JLabel("Benvenuto su MyShop");
        lblWelcome.setForeground(Color.white);
        add(lblWelcome);
    }
}
