package View;

import View.Listener.DxListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
//colored

public class LoginPanel extends JPanel {
    private ActionListener listener;
    private JTextField txtUsername;
    private JPasswordField txtPassword;

    public LoginPanel(ActionListener listener) {
        this.listener = listener;
        setLayout(new BorderLayout());
        setBackground(Color.darkGray);

        JLabel lblWelcome = new JLabel("Accedi qui");
        lblWelcome.setForeground(Color.white);
        JLabel lblUsername = new JLabel("Username");
        lblUsername.setForeground(Color.white);
        txtUsername = new JTextField(20);
        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setForeground(Color.white);
        txtPassword = new JPasswordField(20);
        JButton btnCheckCred = new JButton("LogIn");
        btnCheckCred.setForeground(Color.white);
        btnCheckCred.setBackground(Color.darkGray);
        btnCheckCred.addActionListener(listener);
        btnCheckCred.setActionCommand("" + DxListener.CommandKeyDx.START_LOGIN);

        JPanel topPanel = new JPanel( new FlowLayout(FlowLayout.CENTER) );
        JPanel centerPanel = new JPanel(new FlowLayout());
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        topPanel.add(lblWelcome);
        centerPanel.add(lblUsername);
        centerPanel.add(txtUsername);
        centerPanel.add(lblPassword);
        centerPanel.add(txtPassword);
        centerPanel.add(btnCheckCred);

        topPanel.setBackground(Color.darkGray);
        centerPanel.setBackground(Color.darkGray);
        bottomPanel.setBackground(Color.darkGray);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public String getUsername() {
        return txtUsername.getText();
    }

    public String getPassword() {
        return new String(txtPassword.getPassword());
    }
}
