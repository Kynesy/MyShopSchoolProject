package View.AddArticlePanel;

import Model.Category;
import Model.ItemVendor;
import Model.Store;
import Model.Vendor;
import View.Listener.AdminListener;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class AddItemSubpanel extends JPanel {
    private ActionListener actionListener;
    protected JTextField name;
    protected JTextArea description;
    protected JTextField price;
    private JComboBox<Store> storeCity;
    private JComboBox<Vendor> itemVendors;
    private JComboBox<Category> categoriesJComboBox;
    protected JTextField line;
    protected JTextField shelf;
    protected int fotoNumber;
    protected ArrayList<File> fotos;
    protected JTextField txtFileName;

    protected JLabel topLabel;
    protected JButton btnCreate;

    public AddItemSubpanel(ActionListener actionListener, Store[] storeCitiesArray, Vendor[] itemVendorsArray, Category[] categories){
        this.actionListener = actionListener;
        setLayout(new BorderLayout());
        setBackground(Color.darkGray);


        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel centerPanel = new JPanel( new BorderLayout());
        JPanel sxCenter = new JPanel(new GridLayout(0,1));
        JPanel dxCenter = new JPanel(new GridLayout(0,1));
        JPanel sxCenter1 = new JPanel(new GridLayout(9,1));
        JPanel dxCenter1 = new JPanel(new GridLayout(9,1));
        JPanel sxCenter2 = new JPanel(new GridLayout(1,1));
        JPanel dxCenter2 = new JPanel(new GridLayout(1,1));
        topPanel.setBackground(Color.darkGray);
        topPanel.setForeground(Color.white);
        centerPanel.setBackground(Color.darkGray);
        centerPanel.setForeground(Color.white);
        sxCenter.setBackground(Color.darkGray);
        sxCenter.setForeground(Color.white);
        sxCenter1.setBackground(Color.darkGray);
        sxCenter1.setForeground(Color.white);
        sxCenter2.setBackground(Color.darkGray);
        sxCenter2.setForeground(Color.white);
        dxCenter.setBackground(Color.darkGray);
        dxCenter.setForeground(Color.white);
        dxCenter1.setBackground(Color.darkGray);
        dxCenter1.setForeground(Color.white);
        dxCenter2.setBackground(Color.darkGray);
        dxCenter2.setForeground(Color.white);
        sxCenter.add(sxCenter1);
        sxCenter.add(sxCenter2);
        dxCenter.add(dxCenter1);
        dxCenter.add(dxCenter2);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.add(sxCenter, BorderLayout.WEST);
        centerPanel.add(dxCenter, BorderLayout.EAST);

        topLabel = new JLabel("Creazione di un Prodotto: ");
        topLabel.setForeground(Color.white);
        topPanel.add(topLabel);

        JLabel lblName = new JLabel("Nome prodotto: ");
        lblName.setForeground(Color.white);
        sxCenter1.add(lblName);
        name = new JTextField(20);
        dxCenter1.add(name);

        JLabel lblDescription = new JLabel("Inserisci una descrizione:");
        lblDescription.setForeground(Color.white);
        sxCenter2.add(lblDescription);
        description = new JTextArea(7, 20);
        description.setLineWrap(true);
        dxCenter2.add(description);

        JLabel lblPrice = new JLabel("Inserisci il prezzo (Euro): ");
        lblPrice.setForeground(Color.white);
        sxCenter1.add(lblPrice);
        price = new JTextField(5);
        dxCenter1.add(price);

        JLabel lblCategory = new JLabel("Seleziona una Categoria: ");
        lblCategory.setForeground(Color.white);
        sxCenter1.add(lblCategory);
        categoriesJComboBox = new JComboBox<>(categories);
        dxCenter1.add(categoriesJComboBox);

        JLabel lblSItemVendor = new JLabel("Seleziona il Produttore: ");
        lblSItemVendor.setForeground(Color.white);
        sxCenter1.add(lblSItemVendor);
        itemVendors = new JComboBox<>(itemVendorsArray);
        dxCenter1.add(itemVendors);

        JLabel lblStore = new JLabel("Seleziona il punto vendita: ");
        lblStore.setForeground(Color.white);
        sxCenter1.add(lblStore);
        storeCity = new JComboBox<>(storeCitiesArray);
        dxCenter1.add(storeCity);

        JLabel lblLine = new JLabel("Posizione - inserisci corsia:");
        lblLine.setForeground(Color.white);
        sxCenter1.add(lblLine);
        line = new JTextField(5);
        dxCenter1.add(line);

        JLabel lblShelf = new JLabel("Posizione - inserisci scaffale:");
        lblShelf.setForeground(Color.white);
        sxCenter1.add(lblShelf);
        shelf = new JTextField(5);
        dxCenter1.add(shelf);

        JButton btnFoto = new JButton("Aggiungi Foto");
        btnFoto.setForeground(Color.white);
        btnFoto.setBackground(Color.gray);
        fotos = new ArrayList<>();
        fotoNumber = 0;
        btnFoto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(fotoNumber < 3){
                    JFileChooser jFileChooser = new JFileChooser(System.getProperty("user.dir"));
                    int choose = jFileChooser.showOpenDialog(null);
                    if (choose == JFileChooser.APPROVE_OPTION){
                        File imageFile = jFileChooser.getSelectedFile();
                        try {
                            if (ImageIO.read(imageFile)== null){
                                throw new IOException("Il file non è un'immagine");
                            }
                            String preText = txtFileName.getText();
                            txtFileName.setText(preText + " "+ imageFile.getName());
                            fotos.add(imageFile);
                            fotoNumber++;
                        }catch (IOException ioException){
                            System.out.println(ioException.getMessage());
                            JOptionPane.showMessageDialog(null, "Il file non è un'immagine leggibile.", "Errore", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Non puoi inserire più di 3 immagini", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        sxCenter1.add(btnFoto);


        JButton btnRemoveFoto = new JButton("Rimuovi tutte le foto.");
        btnRemoveFoto.setBackground(Color.gray);
        btnRemoveFoto.setForeground(Color.white);
        btnRemoveFoto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(fotoNumber>0) {
                    fotos.clear();
                    fotoNumber=0;
                    txtFileName.setText("");
                }
            }
        });
        sxCenter1.add(btnRemoveFoto);

        txtFileName = new JTextField(20);
        txtFileName.setEditable(false);
        dxCenter1.add(txtFileName);

        btnCreate = new JButton("Crea il prodotto");
        btnCreate.setForeground(Color.white);
        btnCreate.setBackground(Color.gray);

        btnCreate.addActionListener(actionListener);
        btnCreate.setActionCommand("" + AdminListener.CommandKeyAdmin.CREATE_ITEM);
        bottomPanel.add(btnCreate);

        topPanel.setBackground(Color.darkGray);
        centerPanel.setBackground(Color.darkGray);
        bottomPanel.setBackground(Color.darkGray);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public String getTxtName() {
        return name.getText();
    }

    public String getTxtDescription() {
        return description.getText();
    }

    public double getTxtPrice() {
        try{
            double num = Double.parseDouble(price.getText());
            return num;
        }catch (NumberFormatException e) {
            return 0;
        }
    }

    public String getTxtStoreCity() {
        return ((Store) storeCity.getSelectedItem()).getCity();
    }

    public ItemVendor getTxtItemVendor(){
        return (ItemVendor) itemVendors.getSelectedItem();
    }

    public int getTxtLine() {
        try{
            int num = Integer.parseInt(line.getText());
            return num;
        }catch (NumberFormatException e) {
            return 0;
        }
    }

    public int getTxtShelf() {
        try{
            int num = Integer.parseInt(shelf.getText());
            return num;
        }catch (NumberFormatException e) {
            return 0;
        }
    }

    public int getCategoryID() {
        return ((Category) categoriesJComboBox.getSelectedItem()).getId();
    }

    public ArrayList<File> getFotos() {
        return fotos;
    }

    public void resetFields(){
        name.setText("");
        description.setText("");
        price.setText("");
        line.setText("");
        shelf.setText("");
        txtFileName.setText("");
        fotoNumber = 0;
        storeCity.setSelectedIndex(0);
        categoriesJComboBox.setSelectedIndex(0);
        fotos.clear();
    }
}
