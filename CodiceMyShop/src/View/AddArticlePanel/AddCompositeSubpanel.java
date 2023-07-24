package View.AddArticlePanel;

import Model.*;
import View.Listener.AdminListener;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddCompositeSubpanel extends JPanel {
    private ActionListener actionListener;
    protected JTextField name;
    protected JTextArea description;
    private JComboBox<Store> storeCity;
    private JComboBox<Vendor> itemVendors;
    private JComboBox<Category> categoriesJComboBox;
    protected JTextField line;
    protected JTextField shelf;
    protected int fotoNumber;
    protected ArrayList<File> fotos;
    protected JTextField txtFileName;
    private JTable jTable;

    protected JLabel topLabel;
    protected JButton btnCreate;

    public AddCompositeSubpanel(ActionListener actionListener,ArrayList<Item> items, Store[] storeCitiesArray, Vendor[] itemVendorsArray, Category[] categories){
        this.actionListener = actionListener;
        setLayout(new BorderLayout());
        setBackground(Color.darkGray);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel centerPanel = new JPanel( new BorderLayout());
        JPanel sxCenter = new JPanel(new GridLayout(2,1));
        JPanel dxCenter = new JPanel(new GridLayout(2,1));
        JPanel sxCenter1 = new JPanel(new GridLayout(10,1));
        JPanel dxCenter1 = new JPanel(new GridLayout(10,1));
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
        bottomPanel.setBackground(Color.darkGray);
        centerPanel.add(sxCenter, BorderLayout.WEST);
        centerPanel.add(dxCenter, BorderLayout.EAST);

        topLabel = new JLabel("Creazione di un Prodotto Composito: ");
        topLabel.setForeground(Color.white);
        topPanel.add(topLabel);

        JLabel lblName = new JLabel("Nome Prodotto composito: ");
        lblName.setForeground(Color.white);
        sxCenter1.add(lblName);
        name = new JTextField(20);
        dxCenter1.add(name);

        JLabel lblDescription = new JLabel("Inserisci una descrizione:");
        lblDescription.setForeground(Color.white);
        lblDescription.setBackground(Color.darkGray);
        description = new JTextArea(7, 20);
        description.setLineWrap(true);
        description.setPreferredSize(new Dimension(20,35));
        JPanel tmp = new JPanel();

        tmp.setBackground(Color.darkGray);
        tmp.add(description);
        dxCenter2.add(tmp);

//        JLabel lblPrice = new JLabel("Inserisci il prezzo (Euro): ");
//        sxCenter1.add(lblPrice);
//        price = new JTextField(5);
//        dxCenter1.add(price);

        JLabel lblCategory = new JLabel("Seleziona una Categoria: ");
        lblCategory.setForeground(Color.white);
        sxCenter1.add(lblCategory);
        categoriesJComboBox = new JComboBox<>(categories);
        categoriesJComboBox.setForeground(Color.white);
        categoriesJComboBox.setBackground(Color.gray);
        dxCenter1.add(categoriesJComboBox);

        JLabel lblSItemVendor = new JLabel("Seleziona il Produttore: ");
        lblSItemVendor.setForeground(Color.white);
        sxCenter1.add(lblSItemVendor);
        itemVendors = new JComboBox<>(itemVendorsArray);
        itemVendors.setBackground(Color.gray);
        itemVendors.setBackground(Color.white);
        dxCenter1.add(itemVendors);

        JLabel lblStore = new JLabel("Seleziona il punto vendita: ");
        lblStore.setForeground(Color.white);
        sxCenter1.add(lblStore);
        storeCity = new JComboBox<>(storeCitiesArray);
        storeCity.setBackground(Color.gray);
        storeCity.setForeground(Color.white);
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
        btnFoto.setBackground(Color.gray);
        btnFoto.setForeground(Color.white);
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
        btnRemoveFoto.setBackground(Color.gray);
        btnRemoveFoto.setForeground(Color.white);
        sxCenter1.add(btnRemoveFoto);

        txtFileName = new JTextField(20);
        txtFileName.setEditable(false);
        dxCenter1.add(txtFileName);

        sxCenter1.add(lblDescription);

        btnCreate = new JButton("Crea il prodotto");
        btnCreate.addActionListener(actionListener);
        btnCreate.setActionCommand("" + AdminListener.CommandKeyAdmin.CREATE_COMPOSITE);
        btnCreate.setForeground(Color.white);
        btnCreate.setBackground(Color.gray);
        bottomPanel.add(btnCreate);


        String[][] data = getData(items);
        jTable = getTable(data);
        JPanel tablePanel = new JPanel(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(jTable);
        scrollPane.setPreferredSize(new Dimension(600,100));
        JLabel lblSelection = new JLabel("Seleziona gli articoli da inserire nel Composito (Usare CTRL per la selezione multipla)");
        tablePanel.add(lblSelection, BorderLayout.NORTH);
        tablePanel.add(scrollPane,BorderLayout.CENTER);
        centerPanel.add(tablePanel, BorderLayout.SOUTH);

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

    public List<ItemInterface> getSubitemsList(){
        int[] subItemsIdList = jTable.getSelectedRows();
        List<ItemInterface> array = new ArrayList<>();
        for(int i=0; i<subItemsIdList.length; i++){
            Item tmpItem = new Item();
            int id = Integer.parseInt((String) jTable.getValueAt(subItemsIdList[i], 0));
            double price = Double.parseDouble((String) jTable.getValueAt(subItemsIdList[i],2));
            tmpItem.setId(id);
            tmpItem.setPrice(price);
            array.add(tmpItem);
        }
        return array;
    }

    private String[][] getData(List<Item> items){
        String[][] result;
        result = new String[items.size()][5];
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            result[i][0] = "" + item.getId();
            result[i][1] = item.getName();
            result[i][2] = "" + item.getPrice();
            result[i][3] = "" + item.getItemVendor().getName();
            result[i][4] = "Prodotto";
        }
        return result;
    }

    private JTable getTable(String[][] data){
        JTable jTable = new JTable(data, new String[]{"ID", "Nome", "Prezzo (In Euro)", "Fornitore", "Tipo"});
        jTable.setDefaultEditor(Object.class, null);
        jTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        return jTable;
    }

    public void resetFields(){
        name.setText("");
        description.setText("");
//        price.setText("");
        line.setText("");
        shelf.setText("");
        txtFileName.setText("");
        fotoNumber = 0;
        fotos.clear();
        storeCity.setSelectedIndex(0);
    }
}
