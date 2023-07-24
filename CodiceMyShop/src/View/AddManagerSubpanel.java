package View;

import Model.Store;
import View.Listener.AdminListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class AddManagerSubpanel extends JPanel {
    private ActionListener actionListener;
    private JTextField txtUsername;
    private JTextField txtName;
    private JTextField txtSurname;
    private JPasswordField txtPassword1;
    private JPasswordField txtPassword2;
    private JComboBox<Store> txtStore;

    public AddManagerSubpanel(ActionListener actionListener, Store[] storesArray){
        this.actionListener = actionListener;
        setLayout( new BorderLayout() );
        setBackground(Color.darkGray);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel centerPanel = new JPanel( new BorderLayout());
        JPanel sxCenter = new JPanel(new GridLayout(0,1));
        JPanel dxCenter = new JPanel(new GridLayout(0,1));
        JPanel sxCenter1 = new JPanel(new GridLayout(15,1));
        JPanel dxCenter1 = new JPanel(new GridLayout(15,1));
        sxCenter.setBackground(Color.darkGray);
        sxCenter1.setBackground(Color.darkGray);
        dxCenter.setBackground(Color.darkGray);
        dxCenter1.setBackground(Color.darkGray);
        sxCenter.add(sxCenter1);
        dxCenter.add(dxCenter1);
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.add(sxCenter, BorderLayout.WEST);
        centerPanel.add(dxCenter, BorderLayout.EAST);

        JLabel topLabel = new JLabel("Creazione di un Manager: ");
        topLabel.setForeground(Color.white);
        topPanel.add(topLabel);

        JLabel lblName = new JLabel("Nome : ");
        lblName.setForeground(Color.white);
        sxCenter1.add(lblName);
        txtName = new JTextField(20);
        dxCenter1.add(txtName);

        JLabel lblSurname = new JLabel("Cognome : ");
        lblSurname.setForeground(Color.white);
        sxCenter1.add(lblSurname);
        txtSurname = new JTextField(20);
        dxCenter1.add(txtSurname);

        JLabel lblUsername = new JLabel("Username* : ");
        lblUsername.setForeground(Color.white);
        sxCenter1.add(lblUsername);
        txtUsername = new JTextField(20);
        dxCenter1.add(txtUsername);

        JLabel lblPassword1 = new JLabel("Password* : ");
        lblPassword1.setForeground(Color.white);
        sxCenter1.add(lblPassword1);
        txtPassword1 = new JPasswordField(20);
        dxCenter1.add(txtPassword1);

        JLabel lblPassord2 = new JLabel("Conferma la Password* :");
        lblPassord2.setForeground(Color.white);
        sxCenter1.add(lblPassord2);
        txtPassword2 = new JPasswordField(20);
        dxCenter1.add(txtPassword2);

        JLabel lblStore = new JLabel("Seleziona il Punto vendita* : ");
        lblStore.setForeground(Color.white);
        sxCenter1.add(lblStore);
        txtStore = new JComboBox<>(storesArray);
        dxCenter1.add(txtStore);

        JButton btnCreate = new JButton("Crea il Manager");
        btnCreate.setForeground(Color.white);
        btnCreate.setBackground(Color.GRAY);
        btnCreate.addActionListener(actionListener);
        btnCreate.setActionCommand("" + AdminListener.CommandKeyAdmin.ADD_MANAGER);
        bottomPanel.add(btnCreate);

        JLabel lbl = new JLabel("I campi contrassegnati da * sono obbligatori.");
        lbl.setForeground(Color.white);
        centerPanel.add(lbl);

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

    public String getTxtStore() {
        return ((Store) txtStore.getSelectedItem()).getCity();
    }

    public String getTxtName() {
        if(txtName.getText().isBlank()) {
            return "null";
        }else {
            return txtName.getText();
        }
    }

    public String getTxtSurname() {
        if(txtSurname.getText().isBlank()) {
            return "null";
        }else {
            return txtSurname.getText();
        }
    }

    public void resetFields(){
        txtUsername.setText("");
        txtPassword2.setText("");
        txtPassword1.setText("");
        txtSurname.setText("");
        txtName.setText("");
        txtStore.setSelectedIndex(0);
    }

    public boolean comparePasswords(){
        boolean passOk = true;
        if(!getTxtPassword1().equals(getTxtPassword2()) ){
            passOk = false;
        }
        return passOk;
    }
}
