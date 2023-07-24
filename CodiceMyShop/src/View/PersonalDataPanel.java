package View;

import Business.UserBusiness;
import Model.Customer;
import Model.Store;
import Model.User;
import View.Listener.DxListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class PersonalDataPanel extends JPanel {
    private ActionListener listener;
    private JTextField txtName;
    private JTextField txtSurname;
    private JTextField txtEmail;
    private JTextField txtBirth;
    private JTextField txtAddress;
    private JTextField txtOccupation;
    private JTextField txtPhone;
    private JComboBox<Store> store;

    public PersonalDataPanel(ActionListener listener, UserBusiness.UserPrivilege userPrivilege, User u, Store[] stores) {
        this.listener = listener;
        setLayout( new BorderLayout());
        setBackground(Color.darkGray);


        JPanel topPanel = new JPanel( new FlowLayout(FlowLayout.CENTER) );
        JPanel centerPanel = new JPanel(new GridLayout(20,1));
        centerPanel.setForeground(Color.white);
        centerPanel.setBackground(Color.darkGray);
        JLabel lblDati = new JLabel("I tuoi dati personali");
        lblDati.setForeground(Color.white);
        topPanel.add(lblDati);

        JLabel lblName = new JLabel("Nome:");
        lblName.setForeground(Color.white);
        centerPanel.add(lblName);
        txtName = new JTextField(20);
        txtName.setText(u.getName());
        centerPanel.add(txtName);

        JLabel lblSurname = new JLabel("Cognome *:");
        lblSurname.setForeground(Color.white);
        centerPanel.add(lblSurname);
        txtSurname = new JTextField(20);
        txtSurname.setText(u.getSurname());
        centerPanel.add(txtSurname);

        JLabel lblEmail = new JLabel("Email*:");
        lblEmail.setForeground(Color.white);
        centerPanel.add(lblEmail);
        txtEmail = new JTextField(20);
        txtEmail.setText(u.getEmail());
        centerPanel.add(txtEmail);

        if(userPrivilege == UserBusiness.UserPrivilege.USER){
            Customer c = (Customer) u;

            JLabel lblStore = new JLabel("Seleziona un Punto Vendita *:");
            lblStore.setForeground(Color.white);
            centerPanel.add(lblStore);
            store = new JComboBox<>(stores);
            store.setBackground(Color.gray);
            store.setForeground(Color.white);
            centerPanel.add(store);

            JLabel lblPhone = new JLabel("Numero di Telefono *:");
            lblPhone.setForeground(Color.white);
            centerPanel.add(lblPhone);
            txtPhone = new JTextField(20);
            txtPhone.setText(String.valueOf(c.getPhoneNumber()));
            centerPanel.add(txtPhone);

            JLabel lblYear = new JLabel("Anno di nascita *:");
            lblYear.setForeground(Color.white);
            centerPanel.add(lblYear);
            txtBirth = new JTextField(20);
            txtBirth.setText(String.valueOf(c.getBirthYear()));
            centerPanel.add(txtBirth);

            JLabel lblAddress = new JLabel("Indirizzo *:");
            lblAddress.setForeground(Color.white);
            centerPanel.add(lblAddress);
            txtAddress = new JTextField(20);
            txtAddress.setText(c.getAddress());
            centerPanel.add(txtAddress);

            JLabel lblOccupation = new JLabel("Professione *:");
            lblOccupation.setForeground(Color.white);
            centerPanel.add(lblOccupation);
            txtOccupation = new JTextField(20);
            txtOccupation.setText(c.getOccupation());
            centerPanel.add(txtOccupation);
        }


        JButton btnUpdateData = new JButton("Aggiorna i tuoi dati!");
        btnUpdateData.setActionCommand("" + DxListener.CommandKeyDx.UPDATE_DATA);
        btnUpdateData.addActionListener(listener);
        btnUpdateData.setForeground(Color.white);
        btnUpdateData.setBackground(Color.gray);
        centerPanel.add(btnUpdateData);

        topPanel.setBackground(Color.darkGray);
        centerPanel.setBackground(Color.darkGray);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
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

    public long getPhoneNumber() {
        try{
            String s = txtPhone.getText();
            if( s.contains(".") || s.contains("-")) throw new NumberFormatException();
            long num = Long.parseLong( s.replace(".", ""));
            return num;
        } catch (NumberFormatException e){
            return 0;
        }
    }

    public int getBirthYear() {
        try{
            int num = Integer.parseInt(txtBirth.getText());
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


    public Store getStoreCity(){
        return  (Store)store.getSelectedItem();
    }
}
