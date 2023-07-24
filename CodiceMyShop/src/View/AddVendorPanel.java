package View;

import View.Listener.AdminListener;
import View.Listener.SxListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class AddVendorPanel extends JPanel {
    private ActionListener actionListener;
    private JTextField txtName;
    private JTextField txtWebsite;
    private JTextField txtCity;
    private JTextField txtNation;
    private JComboBox<ProducerType> selection;

    public enum ProducerType{ ITEM_PRODUCER, SERVICE_VENDOR }

    public AddVendorPanel(ActionListener actionListener) {
        this.actionListener = actionListener;
        setLayout( new BorderLayout());
        setBackground(Color.darkGray);

        JPanel topPanel = new JPanel( new FlowLayout(FlowLayout.CENTER) );
        JPanel centerPanel = new JPanel(new BorderLayout());
        JPanel sxCenter = new JPanel(new GridLayout(16,1));
        JPanel dxCenter = new JPanel(new GridLayout(16,1));
        sxCenter.setBackground(Color.darkGray);
        dxCenter.setBackground(Color.darkGray);
        centerPanel.add(sxCenter, BorderLayout.WEST);
        centerPanel.add(dxCenter, BorderLayout.EAST);
        JPanel bottomPanel = new JPanel( new FlowLayout(FlowLayout.CENTER) );

        JLabel topLabel = new JLabel("Creazione di un Produttore/Fornitore");
        topLabel.setForeground(Color.white);
        topPanel.add(topLabel);

        JLabel lblSelection = new JLabel("   Stai registrando un: ");
        lblSelection.setForeground(Color.white);
        sxCenter.add(lblSelection);
        selection = new JComboBox<>(ProducerType.values());
        dxCenter.add(selection);

        JLabel lblName = new JLabel("   Inserisci il nome: ");
        lblName.setForeground(Color.white);
        sxCenter.add(lblName);
        txtName = new JTextField(20);
        dxCenter.add(txtName);

        JLabel lblWebsite = new JLabel("   Inserisci il sito web: ");
        lblWebsite.setForeground(Color.white);
        sxCenter.add(lblWebsite);
        txtWebsite = new JTextField(20);
        dxCenter.add(txtWebsite);

        JLabel lblCity = new JLabel("   Inserisci la città: ");
        lblCity.setForeground(Color.white);
        sxCenter.add(lblCity);
        txtCity = new JTextField(20);
        dxCenter.add(txtCity);

        JLabel lblNation = new JLabel("   Inserisci la nazione: ");
        lblNation.setForeground(Color.white);
        sxCenter.add(lblNation);
        txtNation = new JTextField(20);
        dxCenter.add(txtNation);

        JButton btnCreate = new JButton("Crea!");
        btnCreate.setForeground(Color.white);
        btnCreate.setBackground(Color.GRAY);
        btnCreate.setActionCommand("" + AdminListener.CommandKeyAdmin.CREATE_VENDOR);
        btnCreate.addActionListener(actionListener);
        bottomPanel.add(btnCreate);

        topPanel.setBackground(Color.darkGray);
        centerPanel.setBackground(Color.darkGray);
        bottomPanel.setBackground(Color.darkGray);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public String getName() {
        return txtName.getText();
    }

    public String getWebsite() {
        return txtWebsite.getText();
    }

    public String getCity() {
        return txtCity.getText();
    }

    public String getNation() {
        return txtNation.getText();
    }

    public ProducerType getSelection() {
        return (ProducerType) selection.getSelectedItem();
    }

    public void resetFields(){
        txtNation.setText("");
        txtCity.setText("");
        txtWebsite.setText("");
        txtName.setText("");
        selection.setSelectedIndex(0);
    }
}
