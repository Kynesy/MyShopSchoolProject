package View;

import View.Listener.AdminListener;
import View.Listener.SxListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class AddStorePanel extends JPanel {
    private ActionListener actionListener;
    private JTextField txtStoreCity;

    public AddStorePanel(ActionListener actionListener) {
        this.actionListener = actionListener;

        setLayout( new BorderLayout());
        setBackground(Color.darkGray);

        JLabel topLabel = new JLabel("Creazione di un Punto Vendita");
        topLabel.setForeground(Color.white);
        JPanel topPanel = new JPanel();
        topPanel.add(topLabel);
        topPanel.setLayout( new FlowLayout(FlowLayout.CENTER));

        JPanel sxCenterPanel = new JPanel(new GridLayout(16,1));
        JPanel dxCenterPanel = new JPanel(new GridLayout(16,1));
        JLabel lblCity = new JLabel("    Inserisci la città:");
        lblCity.setForeground(Color.white);
        txtStoreCity = new JTextField(20);
        sxCenterPanel.add(lblCity);
        dxCenterPanel.add(txtStoreCity);

        JButton btnCreate = new JButton("Crea il punto vendita!");
        btnCreate.setForeground(Color.white);
        btnCreate.setBackground(Color.gray);
        btnCreate.addActionListener(actionListener);
        btnCreate.setActionCommand("" + AdminListener.CommandKeyAdmin.CREATE_STORE);
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout( new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(btnCreate);

        topPanel.setBackground(Color.darkGray);
        sxCenterPanel.setBackground(Color.darkGray);
        dxCenterPanel.setBackground(Color.darkGray);
        bottomPanel.setBackground(Color.darkGray);

        add(topPanel, BorderLayout.NORTH);
        add(sxCenterPanel, BorderLayout.WEST);
        add(dxCenterPanel, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public String getStoreCity(){
        return txtStoreCity.getText();
    }

    public void resetFields() {
        txtStoreCity.setText("");
    }
}
