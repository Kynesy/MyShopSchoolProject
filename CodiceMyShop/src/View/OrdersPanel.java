package View;

import Model.Feedback;
import Model.Order;
import View.Listener.ManagerListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class OrdersPanel extends JPanel {
    private ActionListener actionListener;
    private ArrayList<Order> ordersArrayList;
    private JTable jTable;

    public OrdersPanel(ActionListener actionListener, ArrayList<Order> ordersArrayList) {
        this.actionListener = actionListener;
        this.ordersArrayList = ordersArrayList;

        setLayout(new BorderLayout());
        setBackground(Color.darkGray);

        JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JLabel lblTitle = new JLabel("Qui sono riportati tutti gli ordini di MyShop:");
        lblTitle.setForeground(Color.white);
        northPanel.add(lblTitle);

        String[][] data = getData(ordersArrayList);
        jTable = getTable(data);
        jTable.setBackground(Color.gray);
        jTable.setForeground(Color.white);
        centerPanel.add(new JScrollPane(jTable));

        JButton btnSetPaid = new JButton("Paga Ordine");
        btnSetPaid.setForeground(Color.white);
        btnSetPaid.setBackground(Color.gray);
        btnSetPaid.addActionListener(actionListener);
        btnSetPaid.setActionCommand("" + ManagerListener.CommandKeyManager.SET_PAID);
        southPanel.add(btnSetPaid);

        JButton btnDeleteOrder = new JButton("Cancella l'ordine.");
        btnDeleteOrder.setBackground(Color.gray);
        btnDeleteOrder.setForeground(Color.white);
        btnDeleteOrder.addActionListener(actionListener);
        btnDeleteOrder.setActionCommand("" + ManagerListener.CommandKeyManager.DELETE_ORDER);
        southPanel.add(btnDeleteOrder);

        northPanel.setBackground(Color.darkGray);
        centerPanel.setBackground(Color.darkGray);
        southPanel.setBackground(Color.darkGray);

        add(northPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);

    }

    private String[][] getData(ArrayList<Order> oArray){
        String[][] result;
        result = new String[oArray.size()][3];
        for (int i = 0; i < oArray.size(); i++) {
            Order tmpOrder = oArray.get(i);
            result[i][0] = "" + tmpOrder.getUsername();
            result[i][1] = tmpOrder.getOrderID();
            int paidStatus = tmpOrder.getPaidStatus();
            if(paidStatus==0){
                result[i][2] = "NON Pagato";
            }else {
                result[i][2] = "Pagato";
            }

        }
        return result;
    }

    private JTable getTable(String[][] data){
        JTable jTable = new JTable(data, new String[]{"Username Utente", "ID Ordine", "Stato del Pagamento"});
        jTable.setBackground(Color.gray);
        jTable.setForeground(Color.white);
        jTable.setDefaultEditor(Object.class, null);
        jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return jTable;
    }

    public Order getSelectedOrder() {
        int i = jTable.getSelectedRow();
        if (i<0) return null;
        return ordersArrayList.get(i);
    }
}
