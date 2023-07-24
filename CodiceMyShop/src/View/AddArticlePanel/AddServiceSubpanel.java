package View.AddArticlePanel;

import Model.Category;
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

public class AddServiceSubpanel extends JPanel {
    private ActionListener actionListener;
    protected JTextField name;
    protected JTextArea description;
    protected JTextField price;
    private JComboBox<Vendor> serviceVendor;
    private JComboBox<Category> categoriesJComboBox;
    protected int fotoNumber;
    protected ArrayList<File> fotos;
    protected JTextField txtFileName;

    protected JButton btnCreate;
    protected JLabel topLabel;

    public AddServiceSubpanel(ActionListener actionListener, Vendor[] serviceVendorArray, Category[] categories){
        this.actionListener = actionListener;
        setLayout( new BorderLayout() );
        setBackground(Color.darkGray);


        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel centerPanel = new JPanel( new BorderLayout());
        JPanel sxCenter = new JPanel(new GridLayout(0,1));
        JPanel dxCenter = new JPanel(new GridLayout(0,1));
        JPanel sxCenter1 = new JPanel(new GridLayout(7,1));
        JPanel dxCenter1 = new JPanel(new GridLayout(7,1));
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

        topLabel = new JLabel("Creazione di un Servizio: ");
        topLabel.setForeground(Color.white);
        topPanel.add(topLabel);

        JLabel lblName = new JLabel("Nome Servizio: ");
        lblName.setForeground(Color.white);
        sxCenter1.add(lblName);
        name = new JTextField(20);
        dxCenter1.add(name);

        JLabel lblPrice = new JLabel("Inserisci il prezzo: ");
        lblPrice.setForeground(Color.white);
        sxCenter1.add(lblPrice);
        price = new JTextField(5);
        dxCenter1.add(price);

        JLabel lblCategory = new JLabel("Seleziona una Categoria: ");
        lblCategory.setForeground(Color.white);
        sxCenter1.add(lblCategory);
        categoriesJComboBox = new JComboBox<>(categories);
        dxCenter1.add(categoriesJComboBox);

        JLabel lblDescription = new JLabel("Inserisci una descrizione:");
        lblDescription.setForeground(Color.white);
        sxCenter2.add(lblDescription);
        description = new JTextArea(7, 20);
        description.setLineWrap(true);
        dxCenter2.add(description);

        JLabel lblStore = new JLabel("Seleziona il Fornitore: ");
        lblStore.setForeground(Color.white);
        sxCenter1.add(lblStore);
        serviceVendor = new JComboBox<>(serviceVendorArray);
        dxCenter1.add(serviceVendor);


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
        btnRemoveFoto.setForeground(Color.white);
        btnRemoveFoto.setBackground(Color.gray);
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

        btnCreate = new JButton("Crea il servizio");
        btnCreate.setBackground(Color.gray);
        btnCreate.setForeground(Color.white);
        btnCreate.addActionListener(actionListener);
        btnCreate.setActionCommand("" + AdminListener.CommandKeyAdmin.CREATE_SERVICE);
        bottomPanel.add(btnCreate);

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
        try {
            double num = Double.parseDouble(price.getText());
            return num;
        }catch (NumberFormatException e){
            return 0;
        }
    }

    public String getTxtServiceVendor() {
        return ((Vendor) serviceVendor.getSelectedItem()).getName();
    }

    public ArrayList<File> getFotos() {
        return fotos;
    }

    public int getCategoryID() {
        return ((Category) categoriesJComboBox.getSelectedItem()).getId();
    }


    public void resetFields(){
        name.setText("");
        description.setText("");
        price.setText("");
        serviceVendor.setSelectedIndex(0);
        categoriesJComboBox.setSelectedIndex(0);
        fotos.clear();
    }
}
