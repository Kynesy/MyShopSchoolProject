package View;

import Model.Store;
import View.Listener.DxListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class RegistrationPanel extends JPanel {
    private ActionListener listener;
    private JTextField txtUsername;
    private JPasswordField txtPassword1;
    private JPasswordField txtPassword2;
    private JTextField txtName;
    private JTextField txtSurname;
    private JTextField txtEmail;
    private JTextField txtPhoneNumber;
    private JTextField txtBirthYear;
    private JTextField txtAddress;
    private JTextField txtOccupation;
    private JComboBox<Store> store;


    public RegistrationPanel(ActionListener listener, Store[] stores) {
        this.listener = listener;
        setLayout(new BorderLayout());
        setBackground(Color.darkGray);

        JPanel topPanel = new JPanel( new FlowLayout(FlowLayout.CENTER) );
        JPanel centerPanel = new JPanel(new GridLayout(20,2));
        JPanel bottomPanel = new JPanel( new FlowLayout(FlowLayout.CENTER) );

        topPanel.setBackground(Color.darkGray);
        centerPanel.setBackground(Color.darkGray);
        bottomPanel.setBackground(Color.darkGray);

        JLabel lblWelcome = new JLabel("Benvenuto sul form di registrazione");
        lblWelcome.setForeground(Color.white);
        topPanel.add(lblWelcome);

        JLabel lblUsername = new JLabel("Inserisci Username *:");
        lblUsername.setForeground(Color.white);
        centerPanel.add(lblUsername);
        txtUsername = new JTextField(20);
        centerPanel.add(txtUsername);

        JLabel lblPassword1 = new JLabel("Scegli una Password *:");
        lblPassword1.setForeground(Color.white);
        centerPanel.add(lblPassword1);
        txtPassword1 = new JPasswordField(20);
        centerPanel.add(txtPassword1);

        JLabel lblPassword2 = new JLabel("Conferma la Password *:");
        lblPassword2.setForeground(Color.white);
        centerPanel.add(lblPassword2);
        txtPassword2 = new JPasswordField(20);
        centerPanel.add(txtPassword2);

        JLabel lblName = new JLabel("Inserisci nome *:");
        lblName.setForeground(Color.white);
        centerPanel.add(lblName);
        txtName = new JTextField(20);
        centerPanel.add(txtName);

        JLabel lblSurname = new JLabel("Inserisci Cognome *:");
        lblSurname.setForeground(Color.white);
        centerPanel.add(lblSurname);
        txtSurname = new JTextField(20);
        centerPanel.add(txtSurname);

        JLabel lblEmail = new JLabel("Inserisci Email *:");
        lblEmail.setForeground(Color.white);
        centerPanel.add(lblEmail);
        txtEmail = new JTextField(20);
        centerPanel.add(txtEmail);

        JLabel lblPhoneNumber = new JLabel("Inserisci Numero di Telefono *:");
        lblPhoneNumber.setForeground(Color.white);
        centerPanel.add(lblPhoneNumber);
        txtPhoneNumber = new JTextField(20);
        centerPanel.add(txtPhoneNumber);

        JLabel lblBirthYear = new JLabel("Inserisci Anno di nascita:");
        lblBirthYear.setForeground(Color.white);
        centerPanel.add(lblBirthYear);
        txtBirthYear = new JTextField(20);
        centerPanel.add(txtBirthYear);

        JLabel lblAddress = new JLabel("Inserisci Indirizzo:");
        lblAddress.setForeground(Color.white);
        centerPanel.add(lblAddress);
        txtAddress = new JTextField(20);
        centerPanel.add(txtAddress);

        JLabel lblOccupation = new JLabel("Inserisci Professione:");
        lblOccupation.setForeground(Color.white);
        centerPanel.add(lblOccupation);
        txtOccupation = new JTextField(20);
        centerPanel.add(txtOccupation);

        JLabel lblStore = new JLabel("Seleziona un Punto Vendita *:");
        lblStore.setForeground(Color.white);
        centerPanel.add(lblStore);
        store = new JComboBox<>(stores);
        store.setForeground(Color.white);
        store.setBackground(Color.gray);
        centerPanel.add(store);

        JButton btnRegistrate = new JButton("Registrati!");
        btnRegistrate.setForeground(Color.white);
        btnRegistrate.setBackground(Color.gray);
        btnRegistrate.setActionCommand("" + DxListener.CommandKeyDx.START_REGISTRATION);
        btnRegistrate.addActionListener(listener);
        bottomPanel.add(btnRegistrate);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public String getUsername() {
        return txtUsername.getText();
    }

    public String getPassword1() {
        return new String(txtPassword1.getPassword());
    }

    public String getPassword2() {
        return new String(txtPassword2.getPassword());
    }

    public String getName() {
        return txtName.getText();
    }

    public String getSurname() {
        return txtSurname.getText();
    }

    public String getEmail() {
        return txtEmail.getText();
    }

    public Store getStoreCity(){
        return  (Store)store.getSelectedItem();
    }

    public long getPhoneNumber() {
        try{
            String s = txtPhoneNumber.getText();
            if( s.contains(".") || s.contains("-")) throw new NumberFormatException();
            long num = Long.parseLong( s.replace(".", ""));
            return num;
        } catch (NumberFormatException e){
            return 0;
        }
    }

    public int getBirthYear() {
        try{
            int num = Integer.parseInt(txtBirthYear.getText());
            return num;
        }catch (NumberFormatException e){
            return 0;
        }
    }

    public String getAddress() {
        return txtAddress.getText();
    }

    public String getOccupation() {
        return txtOccupation.getText();
    }

    public boolean comparePasswords(){
        boolean passOk = true;
        if(!getPassword1().equals(getPassword2()) ){
            passOk = false;
        }
        return passOk;
    }
}
