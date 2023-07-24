package View;

import Model.Category;
import View.Listener.AdminListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class AddCategoryPanel extends JPanel {
    private ActionListener actionListener;
    private JComboBox<Category> comboBoxParentCategory;
    private JTextField txtCategoryName;
    public enum CategoryType{ ITEM_CATEGORY, SERVICE_CATEGORY }
    private JComboBox<CategoryType> selection;

    public AddCategoryPanel(ActionListener actionListener, Category[] categoryArray) {
        this.actionListener = actionListener;

        setLayout( new BorderLayout());
        setBackground(Color.darkGray);

        JLabel topLabel = new JLabel("Creazione di una Categoria");
        topLabel.setForeground(Color.white);
        JPanel topPanel = new JPanel();
        topPanel.add(topLabel);
        topPanel.setLayout( new FlowLayout(FlowLayout.CENTER));

        JPanel sxCenterPanel = new JPanel(new GridLayout(16,1));
        JPanel dxCenterPanel = new JPanel(new GridLayout(16,1));
        JLabel lblParent = new JLabel("Categoria Padre: ");
        lblParent.setForeground(Color.white);
        JLabel lblNewCategory = new JLabel("Nome nuova categoria: ");
        lblNewCategory.setForeground(Color.white);
        sxCenterPanel.add(lblParent);
        sxCenterPanel.add(lblNewCategory);

        //Creo una categoria da mettere nell'elenco per fornire la scelta di non impostare una categoria padre
        Category noCategory = new Category();
        noCategory.setName("Nessuna Categoria Padre");
        noCategory.setId(-1); //metto id -1, così sicuramente non va in conflitto con altri id
        Category[] c = new Category[categoryArray.length + 1];
        c[0] = noCategory; //metto la noCategory in prima posizione e ricopio l'array con le categorie
        for(int i=1; i<c.length; i++){
            c[i] = categoryArray[i-1];
        }
        comboBoxParentCategory = new JComboBox<>(c);
        txtCategoryName = new JTextField(20);
        dxCenterPanel.add(comboBoxParentCategory);
        dxCenterPanel.add(txtCategoryName);

        JButton btnCreate = new JButton("Crea la nuova Categoria!");
        btnCreate.setForeground(Color.white);
        btnCreate.setBackground(Color.GRAY);
        btnCreate.addActionListener(actionListener);
        btnCreate.setActionCommand("" + AdminListener.CommandKeyAdmin.CREATE_CATEGORY);
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

    public String getCategoryName(){
        return txtCategoryName.getText();
    }

    public int getParentID(){
        Category parentCategory = (Category) comboBoxParentCategory.getSelectedItem();
        return parentCategory.getId();
    }

    public void resetFields() {
        txtCategoryName.setText("");
        comboBoxParentCategory.setSelectedIndex(0);
    }
}
