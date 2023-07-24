package View;

import Model.Order;
import Model.Prenotation;
import View.Listener.ManagerListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class PrenotationsPanel extends JPanel {
    private ActionListener actionListener;
    private ArrayList<Prenotation> prenotationArrayList;
    private JTable jTable;

    public PrenotationsPanel(ActionListener actionListener, ArrayList<Prenotation> prenotationArrayList) {
        this.actionListener = actionListener;
        this.prenotationArrayList = prenotationArrayList;

        setLayout(new BorderLayout());
        setBackground(Color.darkGray);

        JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JLabel lblTitle = new JLabel("Qui sono riportati tutte le prenotazioni di MyShop:");
        lblTitle.setForeground(Color.white);
        northPanel.add(lblTitle);

        String[][] data = getData(prenotationArrayList);
        jTable = getTable(data);
        jTable.setForeground(Color.white);
        jTable.setBackground(Color.gray);
        centerPanel.add(new JScrollPane(jTable));

        JButton btnSetPaid = new JButton("Approva prenotazione");
        btnSetPaid.setForeground(Color.white);
        btnSetPaid.setBackground(Color.gray);
        btnSetPaid.addActionListener(actionListener);
        btnSetPaid.setActionCommand("" + ManagerListener.CommandKeyManager.APPROVE_PRENOTATION);
        southPanel.add(btnSetPaid);

        northPanel.setBackground(Color.darkGray);
        centerPanel.setBackground(Color.darkGray);
        southPanel.setBackground(Color.darkGray);

        add(northPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
    }

    private String[][] getData(ArrayList<Prenotation> prenotations){
        String[][] result;
        result = new String[prenotations.size()][4];
        for (int i = 0; i < prenotations.size(); i++) {
            Prenotation p = prenotations.get(i);
            result[i][0] = "" + p.getID();
            result[i][1] = "" +p.getUsername();
            result[i][2] = "" + p.getArticleID();
            result[i][3] = "" + p.getQuantity();
        }
        return result;
    }

    private JTable getTable(String[][] data){
        JTable jTable = new JTable(data, new String[]{"ID Prenotazione","Username Utente", "ID Articolo", "Pezzi Ordinati"});
        jTable.setForeground(Color.white);
        jTable.setBackground(Color.gray);
        jTable.setDefaultEditor(Object.class, null);
        jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return jTable;
    }

    public Prenotation getSelectedPrenotation() {
        int i = jTable.getSelectedRow();
        if (i<0) return null;
        return prenotationArrayList.get(i);
    }
}
