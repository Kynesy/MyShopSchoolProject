package View;

import Business.ImageUtils;
import Business.UserBusiness;
import Model.Feedback;
import Model.Service;
import View.Listener.BrowseListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ServiceDetailsPanel extends JPanel implements IArticlePannels{
    private ActionListener listener;
    private Service service;
    private ArrayList<Feedback> feedbackArrayList;
    private JTable jTable;

    public ServiceDetailsPanel(Service service, ArrayList<Feedback> feedbackArrayList, ActionListener listener, UserBusiness.UserPrivilege userPrivilege){
        this.listener = listener;
        this.service = service;
        this.feedbackArrayList = feedbackArrayList;

        setLayout(new BorderLayout());
        setBackground(Color.darkGray);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel centerPanel1 = new JPanel(new GridLayout(8,2));
        JPanel centerPanel2 = new JPanel(new GridLayout(1,1));
        JPanel centerPanel = new JPanel(new GridLayout(3,1));
        JPanel sxPanel = new JPanel(new GridLayout(3,1));

        centerPanel1.setBackground(Color.darkGray);
        centerPanel2.setBackground(Color.darkGray);

        JLabel lblName = new JLabel("Nome: " + service.getName());
        lblName.setForeground(Color.white);
        JLabel lblID = new JLabel("ID: " + service.getId());
        lblID.setForeground(Color.white);
        JTextArea txtDescription = new JTextArea(7,20);
        txtDescription.setLineWrap(true);
        txtDescription.setText("Descrizione: " + service.getDescription());
        txtDescription.setEditable(false);
        JLabel lblPrice = new JLabel("Prezzo: " + service.getPrice() + " Euro");
        lblPrice.setForeground(Color.white);
        JLabel lblVendor = new JLabel("Fornitore: " + service.getVendorName());
        lblVendor.setForeground(Color.white);
        JLabel lblCategory = new JLabel("Categoria: "+service.getCategory().getName());
        lblCategory.setForeground(Color.white);

        ArrayList<File> fotos = service.getFotos();
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

        topPanel.add(lblName);
        topPanel.add(new JLabel("           "));
        topPanel.add(lblID);

        centerPanel2.add(txtDescription);
        centerPanel1.add(lblPrice);
        centerPanel1.add(lblVendor);
        centerPanel1.add(lblCategory);
        if(!service.getCategory().getCategoryTree().isEmpty()) {
            JLabel lblCategoryTree = new JLabel("Albero categorie: " + service.getCategory().getCategoryTree());
            lblCategoryTree.setForeground(Color.white);
            centerPanel1.add(lblCategoryTree);
        }
        add(topPanel, BorderLayout.NORTH);

        String[][] data = getData(feedbackArrayList);
        jTable = getTable(data);
        JPanel tablePanel = new JPanel(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(jTable);
        scrollPane.setPreferredSize(new Dimension(600,100));
        tablePanel.add(new JLabel("Recensioni utenti:"), BorderLayout.NORTH);
        tablePanel.setBackground(Color.gray);
        tablePanel.setForeground(Color.white);
        tablePanel.add(scrollPane,BorderLayout.CENTER);

        centerPanel.add(tablePanel);
        centerPanel.add(centerPanel2);
        centerPanel.add(centerPanel1);
        centerPanel.add(tablePanel);

        topPanel.setBackground(Color.darkGray);
        sxPanel.setBackground(Color.darkGray);

        add(topPanel, BorderLayout.NORTH);
        add(sxPanel, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);

        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        if(userPrivilege!=null) {
            switch (userPrivilege) {
                case USER: {
                    JButton btnWriteFeedback = new JButton("Lascia la tua recensione qui!");
                    btnWriteFeedback.setBackground(Color.gray);
                    btnWriteFeedback.setForeground(Color.white);
                    btnWriteFeedback.setActionCommand("" + BrowseListener.CommandKeyBrowse.SHOW_FEEDBACK_FORM);
                    btnWriteFeedback.addActionListener(listener);
                    southPanel.add(btnWriteFeedback);
                    break;
                }
            }
        }

        JButton btnOpenFeedback = new JButton("Dettagli Recensioni");
        btnOpenFeedback.setBackground(Color.gray);
        btnOpenFeedback.setForeground(Color.white);
        btnOpenFeedback.setActionCommand("" + BrowseListener.CommandKeyBrowse.OPEN_FEEDBACK);
        btnOpenFeedback.addActionListener(listener);
        southPanel.add(btnOpenFeedback);

        southPanel.setBackground(Color.darkGray);
        add(southPanel, BorderLayout.SOUTH);
    }

    @Override
    public int getSelected() {
        return service.getId();
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
        jTable.setBackground(Color.gray);
        jTable.setForeground(Color.white);
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
