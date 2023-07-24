package View;

import Business.ImageUtils;
import Business.UserBusiness;
import Model.Feedback;
import Model.Item;
import View.Listener.BrowseListener;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ItemDetailsPanel extends JPanel implements IArticlePannels {
    private Item item;
    private ActionListener actionListener;
    private JTable jTable;
    ArrayList<Feedback> feedbackArrayList;


    public ItemDetailsPanel(Item item, ArrayList<Feedback> feedbackArrayList, ActionListener actionListener, UserBusiness.UserPrivilege userPrivilege) {
        this.item = item;
        this.actionListener = actionListener;
        this.feedbackArrayList = feedbackArrayList;

        setLayout(new BorderLayout());
        setBackground(Color.darkGray);
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel sxPanel = new JPanel();
        sxPanel.setLayout(new GridLayout(3,1));
        JPanel dxPanel = new JPanel(new GridLayout(3,1));
        JPanel dxPanel1 = new JPanel(new GridLayout(7,2));
        JPanel dxPanel2 = new JPanel(new GridLayout(1,1));

        dxPanel1.setBackground(Color.darkGray);
        dxPanel2.setBackground(Color.darkGray);

        JLabel lblName = new JLabel("Nome: " + item.getName() + "                ID: " + item.getId());
        lblName.setForeground(Color.white);
        topPanel.add(lblName);

        ArrayList<File> fotos = item.getFotos();
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
        txtDescription.setText("Descrizione:      " + item.getDescription());
        txtDescription.setForeground(Color.white);
        txtDescription.setBackground(Color.gray);
        txtDescription.setEditable(false);
        JLabel lblPrice = new JLabel("Prezzo:                 " + item.getPrice() + " Euro");
        lblPrice.setForeground(Color.white);
        JLabel lblProducer = new JLabel("Produttore:          " + item.getItemVendor().toString());
        lblProducer.setForeground(Color.white);
        JLabel lblStore = new JLabel("Punto vendita:     "+item.getCityStore());
        lblStore.setForeground(Color.white);
        JLabel lblPosition = new JLabel("Posizione:            Corsia-" + item.getLine() +" Scaffale-" + item.getShelf());
        lblPosition.setForeground(Color.white);
        JLabel lblAvaiability = new JLabel("Disponibilità:        " + item.getAvaiability() + " pezzi in magazzino.");
        lblAvaiability.setForeground(Color.white);
        JLabel lblCategory = new JLabel("Categoria: "+item.getCategory().getName());
        lblCategory.setForeground(Color.white);

        dxPanel2.add(txtDescription);
        dxPanel1.add(lblPrice);
        dxPanel1.add(lblProducer);
        dxPanel1.add(lblCategory);
        if (!item.getCategory().getCategoryTree().isEmpty())
        {
            JLabel lblCategoryTree = new JLabel("Albero categorie: " + item.getCategory().getCategoryTree());
            lblCategoryTree.setForeground(Color.white);
            dxPanel1.add(lblCategoryTree);
        }
        dxPanel1.add(lblStore);
        dxPanel1.add(lblAvaiability);
        dxPanel1.add(lblPosition);

        String[][] data = getData(feedbackArrayList);
        jTable = getTable(data);
        JPanel tablePanel = new JPanel(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(jTable);
        scrollPane.setPreferredSize(new Dimension(600,100));
        tablePanel.add(new JLabel("Recensioni utenti:"), BorderLayout.NORTH);
        tablePanel.add(scrollPane,BorderLayout.CENTER);
        tablePanel.setForeground(Color.white);
        tablePanel.setBackground(Color.gray);


        dxPanel.add(dxPanel2);
        dxPanel.add(dxPanel1);
        dxPanel.add(tablePanel);

        topPanel.setBackground(Color.darkGray);
        sxPanel.setBackground(Color.darkGray);
        dxPanel.setBackground(Color.darkGray);

        add(topPanel, BorderLayout.NORTH);
        add(sxPanel, BorderLayout.WEST);
        add(dxPanel, BorderLayout.CENTER);
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        if(userPrivilege!=null) {
            switch (userPrivilege) {
                case USER: {
                    JButton btnWriteFeedback = new JButton("Lascia la tua recensione qui!");
                    btnWriteFeedback.setForeground(Color.white);
                    btnWriteFeedback.setBackground(Color.gray);

                    btnWriteFeedback.setActionCommand("" + BrowseListener.CommandKeyBrowse.SHOW_FEEDBACK_FORM);
                    btnWriteFeedback.addActionListener(actionListener);
                    southPanel.add(btnWriteFeedback);
                    break;
                }
                case MANAGER:{
                    JButton btnReply = new JButton("Rispodi alla recensione!");
                    btnReply.setForeground(Color.white);
                    btnReply.setBackground(Color.gray);
                    btnReply.setActionCommand("" + BrowseListener.CommandKeyBrowse.REPLY_FEEDBACK);
                    btnReply.addActionListener(actionListener);
                    southPanel.add(btnReply);
                    break;
                }
            }
        }
        JButton btnOpenFeedback = new JButton("Dettagli Recensione");
        btnOpenFeedback.setForeground(Color.white);
        btnOpenFeedback.setBackground(Color.gray);
        btnOpenFeedback.setActionCommand("" + BrowseListener.CommandKeyBrowse.OPEN_FEEDBACK);
        btnOpenFeedback.addActionListener(actionListener);
        southPanel.add(btnOpenFeedback);

        southPanel.setBackground(Color.darkGray);
        add(southPanel, BorderLayout.SOUTH);
    }

    @Override
    public int getSelected() {
        return item.getId();
    }

    private String[][] getData(ArrayList<Feedback> fArray){
        String[][] result;
        result = new String[fArray.size()][3];
        for (int i = 0; i < fArray.size(); i++) {
            Feedback tmpF = fArray.get(i);
            result[i][0] = "" + tmpF.getUsername();
            result[i][1] = tmpF.getGraphicalVote();
            result[i][2] = tmpF.getMessage();
        }
        return result;
    }

    private JTable getTable(String[][] data){
        JTable jTable = new JTable(data, new String[]{"Utente", "Voto", "Messaggio"});
        jTable.setForeground(Color.white);
        jTable.setBackground(Color.gray);
        jTable.setDefaultEditor(Object.class, null);
        jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return jTable;
    }
    @Override
    public Feedback getSelectedFeedback() {
        int i = jTable.getSelectedRow();
        if (i<0) return null;
        return feedbackArrayList.get(i);
    }
}
