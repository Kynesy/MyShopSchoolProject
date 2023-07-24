package View;

import Business.ImageUtils;
import Model.CompositeItem;
import Model.Feedback;
import Model.Item;
import Model.ItemInterface;
import View.Listener.BrowseListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CompositeDetailsPanel extends JPanel implements IArticlePannels {
    private CompositeItem compositeItem;
    private ActionListener actionListener;
    private JTable jTable;

    public CompositeDetailsPanel(CompositeItem compositeItem, ActionListener actionListener) {
        this.compositeItem = compositeItem;
        this.actionListener = actionListener;

        setLayout(new BorderLayout());
        setBackground(Color.darkGray);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel sxPanel = new JPanel();
        sxPanel.setLayout(new GridLayout(3,1));
        JPanel cPanel = new JPanel(new GridLayout(2,1));
        JPanel cPanel1 = new JPanel(new GridLayout(6,2));
        JPanel cPanel2 = new JPanel(new GridLayout(1,1));

        cPanel1.setBackground(Color.darkGray);
        cPanel2.setBackground(Color.darkGray);

        JLabel lblName = new JLabel("Nome: " + compositeItem.getName() + "                ID: " + compositeItem.getId());
        lblName.setForeground(Color.white);
        topPanel.add(lblName);

        ArrayList<File> fotos = compositeItem.getFotos();
        ImageIcon image;
        JLabel lblFoto;
        for(File fotoFile : fotos) {
            try {
                image = ImageUtils.scaleImage(200, 200, fotoFile);
                lblFoto = new JLabel( image);
                sxPanel.add(lblFoto);
            }catch (IOException e){
                System.out.println("non carica.");
            }

        }

        JTextArea txtDescription = new JTextArea(7, 20);
        txtDescription.setLineWrap(true);
        txtDescription.setText("Descrizione:      " + compositeItem.getDescription());
        txtDescription.setEditable(false);
        JLabel lblPrice = new JLabel("Prezzo:                 " + compositeItem.getPrice() + " Euro");
        lblPrice.setForeground(Color.white);
        JLabel lblProducer = new JLabel("Produttore:          " + compositeItem.getItemVendor().toString());
        lblProducer.setForeground(Color.white);
        JLabel lblStore = new JLabel("Punto vendita:     "+compositeItem.getCityStore());
        lblStore.setForeground(Color.white);
        JLabel lblPosition = new JLabel("Posizione:            Corsia-" + compositeItem.getLine() +" Scaffale-" + compositeItem.getShelf());
        lblPosition.setForeground(Color.white);
        JLabel lblAvaiability = new JLabel("Disponibilità:        " + compositeItem.getAvaiability() + " pezzi in magazzino.");
        lblAvaiability.setForeground(Color.white);
        JLabel lblCategory = new JLabel("Categoria: "+compositeItem.getCategory().getName());
        lblCategory.setForeground(Color.white);

        cPanel2.add(txtDescription);
        cPanel1.add(lblPrice);
        cPanel1.add(lblProducer);
        cPanel1.add(lblCategory);
        if(!compositeItem.getCategory().getCategoryTree().isEmpty()) {
            JLabel lblCategoryTree = new JLabel("Albero categorie: " + compositeItem.getCategory().getCategoryTree());
            lblCategoryTree.setForeground(Color.white);
            cPanel1.add(lblCategoryTree);
        }
        cPanel1.add(lblStore);
        cPanel1.add(lblAvaiability);
        cPanel1.add(lblPosition);

        cPanel.add(cPanel2);
        cPanel.add(cPanel1);

        topPanel.setBackground(Color.darkGray);
        sxPanel.setBackground(Color.darkGray);
        sxPanel.setBackground(Color.darkGray);

        add(topPanel, BorderLayout.NORTH);
        add(sxPanel, BorderLayout.WEST);
        add(cPanel, BorderLayout.CENTER);

        String[][] data = getData(compositeItem.getSubItemList());
        jTable = getTable(data);
        JPanel tablePanel = new JPanel(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(jTable);
        scrollPane.setPreferredSize(new Dimension(600,100));
        tablePanel.add(scrollPane,BorderLayout.CENTER);

        JButton btnShowDetailSubitem = new JButton("Visualizza i dettagli del sottoprodotto selezionato");
        btnShowDetailSubitem.setForeground(Color.white);
        btnShowDetailSubitem.setBackground(Color.gray);

        btnShowDetailSubitem.setActionCommand(""+ BrowseListener.CommandKeyBrowse.SHOW_ARTICLE_DETAILS);
        btnShowDetailSubitem.addActionListener(actionListener);
        tablePanel.add(btnShowDetailSubitem, BorderLayout.SOUTH);

        add(tablePanel, BorderLayout.SOUTH);
    }

    @Override
    public int getSelected() {
        int i = jTable.getSelectedRow();
        if (i<0) return -1;
        return Integer.parseInt((String) jTable.getValueAt(i, 0));
    }

    @Override
    public Feedback getSelectedFeedback() {
        return null;
    }

    private String[][] getData(List<ItemInterface> items){
        String[][] result;
        result = new String[items.size()][5];
        for (int i = 0; i < items.size(); i++) {
            Item item = (Item) items.get(i);
            result[i][0] = "" + item.getId();
            result[i][1] = item.getName();
            result[i][2] = "" + item.getPrice() + " Euro";
            result[i][3] = "" + item.getItemVendor().getName();
            result[i][4] = "Prodotto";
        }
        return result;
    }

    private JTable getTable(String[][] data){
        JTable jTable = new JTable(data, new String[]{"ID", "Nome", "Prezzo", "Fornitore", "Tipo"});
        jTable.setBackground(Color.gray);
        jTable.setForeground(Color.white);
        jTable.setDefaultEditor(Object.class, null);
        jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return jTable;
    }

}
