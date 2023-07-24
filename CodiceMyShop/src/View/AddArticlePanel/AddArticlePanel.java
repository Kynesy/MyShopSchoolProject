package View.AddArticlePanel;

import Model.Category;
import Model.Item;
import Model.Store;
import Model.Vendor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class AddArticlePanel extends JPanel {
    private ActionListener actionListener;
    private JPanel mainPanel;

    public AddArticlePanel(ActionListener actionListener, ArrayList<Item> items, Store[] cities, Vendor[] itemVendors, Vendor[] serviceVendors, Category[] categories){
        this.actionListener = actionListener;
        setLayout( new BorderLayout() );
        setBackground(Color.darkGray);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        mainPanel = new AddItemSubpanel(actionListener, cities, itemVendors, categories);

        JLabel lblSelection = new JLabel("Stai inserendo un: ");
        lblSelection.setForeground(Color.white);
        topPanel.add(lblSelection);
        JComboBox selection = new JComboBox( new String[]{"Prodotto", "Prodotto Composito", "Servizio"});
        selection.addActionListener(e -> {
            if(selection.getSelectedIndex() == 0){
                setMainPanel(new AddItemSubpanel(actionListener, cities, itemVendors, categories));
            } else if(selection.getSelectedIndex() == 1){
                setMainPanel(new AddCompositeSubpanel(actionListener, items, cities, itemVendors, categories));
            } else if (selection.getSelectedIndex() == 2){
                setMainPanel(new AddServiceSubpanel(actionListener, serviceVendors, categories));
            }
        });
        selection.setBackground(Color.gray);
        selection.setForeground(Color.white);
        topPanel.add(selection);

        topPanel.setBackground(Color.darkGray);
        mainPanel.setBackground(Color.darkGray);

        add(topPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private void setMainPanel(JPanel panel){
        if(mainPanel != null){
            remove(mainPanel);
        }
        add(panel, BorderLayout.CENTER);
        mainPanel = panel;
        invalidate();
        revalidate();
        repaint();
    }
}
