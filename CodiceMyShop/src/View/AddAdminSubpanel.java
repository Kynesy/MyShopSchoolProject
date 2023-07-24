package View;

import View.Listener.AdminListener;
import View.Listener.SxListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class AddAdminSubpanel extends JPanel {
    private ActionListener actionListener;
    private JTextField txtUsername;
    private JPasswordField txtPassword1;
    private JPasswordField txtPassword2;

    public AddAdminSubpanel(ActionListener actionListener){
        this.actionListener = actionListener;
        setLayout( new BorderLayout() );
        setBackground(Color.darkGray);


        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel centerPanel = new JPanel( new BorderLayout());
        JPanel sxCenter = new JPanel(new GridLayout(0,1));
        JPanel dxCenter = new JPanel(new GridLayout(0,1));
        JPanel sxCenter1 = new JPanel(new GridLayout(15,1));
        sxCenter1.setBackground(Color.darkGray);
        JPanel dxCenter1 = new JPanel(new GridLayout(15,1));
        dxCenter1.setBackground(Color.darkGray);

        sxCenter.add(sxCenter1);
        dxCenter.add(dxCenter1);
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.add(sxCenter, BorderLayout.WEST);
        centerPanel.add(dxCenter, BorderLayout.EAST);

        JLabel topLabel = new JLabel("Creazione di un Amministratore: ");
        topLabel.setForeground(Color.white);
        topPanel.add(topLabel);

        JLabel lblUsername = new JLabel("Username: ");
        lblUsername.setForeground(Color.white);
        sxCenter1.add(lblUsername);
        txtUsername = new JTextField(20);
        dxCenter1.add(txtUsername);

        JLabel lblPassword1 = new JLabel("Password: ");
        lblPassword1.setForeground(Color.white);
        sxCenter1.add(lblPassword1);
        txtPassword1 = new JPasswordField(20);
        dxCenter1.add(txtPassword1);

        JLabel lblPassord2 = new JLabel("Conferma la Password:");
        lblPassord2.setForeground(Color.white);
        sxCenter1.add(lblPassord2);
        txtPassword2 = new JPasswordField(20);
        dxCenter1.add(txtPassword2);

        JButton btnCreate = new JButton("Crea l'Amministratore");
        btnCreate.setForeground(Color.white);
        btnCreate.addActionListener(actionListener);
        btnCreate.setActionCommand("" + AdminListener.CommandKeyAdmin.ADD_ADMIN);
        btnCreate.setForeground(Color.white);
        btnCreate.setBackground(Color.gray);
        bottomPanel.add(btnCreate);

        topPanel.setBackground(Color.darkGray);
        centerPanel.setBackground(Color.darkGray);
        bottomPanel.setBackground(Color.darkGray);
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public String getTxtUsername() {
        return txtUsername.getText();
    }

    public String getTxtPassword1() {
        return new String(txtPassword1.getPassword());
    }

    public String getTxtPassword2() {
        return new String(txtPassword2.getPassword());
    }


    public void resetFields(){
        txtUsername.setText("");
        txtPassword2.setText("");
        txtPassword1.setText("");
    }

    public boolean comparePasswords(){
        boolean passOk = true;
        if(!getTxtPassword1().equals(getTxtPassword2()) ){
            passOk = false;
        }
        return passOk;
    }
}
