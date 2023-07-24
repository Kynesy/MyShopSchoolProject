package View;

import Model.User;
import View.Listener.ManagerListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class SendMailPanel extends JPanel {
    private ArrayList<User> userArrayList;
    private ActionListener listener;
    private JComboBox<User> userJComboBox;
    private JTextField txtSubject;
    private JTextArea txtMail;
    private JCheckBox jCheckBox;

    public SendMailPanel(ArrayList<User> usersArrayList, ActionListener actionListener) {
        this.userArrayList = usersArrayList;
        User[] u = usersArrayList.toArray(User[]::new);
        this.listener = actionListener;
        setLayout(new BorderLayout());
        setBackground(Color.darkGray);


        JLabel lblTitle = new JLabel("Invia una mail agli utenti iscritti al tuo punto vendita");
        lblTitle.setForeground(Color.white);
        add(lblTitle, BorderLayout.NORTH);


        JPanel cPanel = new JPanel(new GridLayout(10,1));
        JLabel lblUser = new JLabel("UTENTE: ");
        lblUser.setForeground(Color.white);
        lblUser.setBackground(Color.darkGray);
        userJComboBox = new JComboBox<>(u);
        userJComboBox.setForeground(Color.white);
        userJComboBox.setBackground(Color.gray);
        userJComboBox.setEnabled(false);
        jCheckBox = new JCheckBox("Tutti gli Utenti", true);
        jCheckBox.setForeground(Color.cyan);
        jCheckBox.setBackground(Color.gray);
        jCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean allUsers = ((JCheckBox) e.getSource()).isSelected();
                if(allUsers){
                    userJComboBox.setEnabled(false);
                }else {
                    userJComboBox.setEnabled(true);
                }
            }
        });
        JLabel lblSubject = new JLabel("OGGETTO: ");
        lblSubject.setForeground(Color.white);
        txtSubject = new JTextField(40);
        JLabel lblMail = new JLabel("CONTENUTO: ");
        lblMail.setForeground(Color.white);
        txtMail = new JTextArea(10, 20);
        txtMail.setSize(30,20);
        txtMail.setLineWrap(true);


        JButton sendMail = new JButton("Invia!");
        sendMail.setForeground(Color.white);
        sendMail.setBackground(Color.gray);
        sendMail.setActionCommand("" + ManagerListener.CommandKeyManager.SEND_MAIL);
        sendMail.addActionListener(listener);

        cPanel.add(jCheckBox);
        cPanel.add(lblUser);
        cPanel.add(userJComboBox);
        cPanel.add(lblSubject);
        cPanel.add(txtSubject);
        cPanel.add(lblMail);
        cPanel.add(txtMail);

        cPanel.setBackground(Color.darkGray);
        add(cPanel, BorderLayout.CENTER);
        add(sendMail, BorderLayout.SOUTH);
    }

    public String getSelectedUser() {
        User u = (User) userJComboBox.getSelectedItem();
        if(u==null){
            return null;
        }
        return u.getEmail();
    }

    public boolean sendingToAllUsers(){
        return jCheckBox.isSelected();
    }

    public String getSubject() {
        return txtSubject.getText();
    }

    public String getMail() {
        return txtMail.getText();
    }

    public ArrayList<User> getUserArrayList() {
        return userArrayList;
    }
}
