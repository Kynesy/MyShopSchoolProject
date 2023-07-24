package View;

import View.Listener.ManagerListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class RemoveUserPanel extends JPanel {
    private ActionListener listener;
    private String selectedUser;
    private JTable jTable;

    public RemoveUserPanel(ActionListener listener, JTable jTable){
        this.listener = listener;
        this.jTable = jTable;
        this.setLayout(new BorderLayout());
        setBackground(Color.darkGray);


        JLabel lblTitle = new JLabel("Lista degli account");
        lblTitle.setForeground(Color.white);
        add(lblTitle, BorderLayout.NORTH);

        add(new JScrollPane(jTable), BorderLayout.CENTER);

        JButton btnRemove = new JButton("Rimuovi account");
        btnRemove.setBackground(Color.gray);
        btnRemove.setForeground(Color.white);
        btnRemove.addActionListener(listener);
        btnRemove.setActionCommand("" + ManagerListener.CommandKeyManager.DELETE_ACCOUNT);

        JButton btnDisable = new JButton("Disabilita account");
        btnDisable.setBackground(Color.gray);
        btnDisable.setForeground(Color.white);
        btnDisable.addActionListener(listener);
        btnDisable.setActionCommand("" + ManagerListener.CommandKeyManager.DISABLE_ACCOUNT);

        JButton btnReabilitate = new JButton("Riabilita account");
        btnReabilitate.setBackground(Color.gray);
        btnReabilitate.setForeground(Color.white);
        btnReabilitate.addActionListener(listener);
        btnReabilitate.setActionCommand("" + ManagerListener.CommandKeyManager.ABILITATE_ACCOUNT);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(btnRemove);
        bottomPanel.add(btnDisable);
        bottomPanel.add(btnReabilitate);

        bottomPanel.setBackground(Color.darkGray);
        add(bottomPanel, BorderLayout.SOUTH);

    }

    public String getSelectedUser() {
        int i = jTable.getSelectedRow();
        if(i==-1){
            return null;
        }
        selectedUser = (String) jTable.getValueAt(i, 0);
        return selectedUser;
    }
}
