package View;

import Business.TableFactory.TableArticleFactory;
import Model.Feedback;
import Model.WishList;
import View.Listener.BrowseListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class WishlistPanel extends JPanel implements IArticlePannels{
    private ActionListener actionListener;
    private JPanel listPanel;
    private JComboBox<WishList> comboBoxWishlist;
    private JTable table;

    public WishlistPanel(ActionListener actionListener, WishList[] wishListArrayList){

        this.actionListener = actionListener;
        setLayout( new BorderLayout() );
        setBackground(Color.darkGray);

        listPanel = new JPanel();
        listPanel.setForeground(Color.white);
        listPanel.setBackground(Color.darkGray);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.setForeground(Color.white);
        topPanel.setBackground(Color.darkGray);
        JLabel lblWishlist = new JLabel("Stai visualizzando la lista: ");
        lblWishlist.setForeground(Color.white);
        comboBoxWishlist = new JComboBox<>( wishListArrayList );
        comboBoxWishlist.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                table = createTable((WishList) comboBoxWishlist.getSelectedItem());
                setListPanel(table);
            }
        });

        table = createTable(comboBoxWishlist.getItemAt(0));
        setListPanel(table);
        topPanel.add(lblWishlist);
        topPanel.add(comboBoxWishlist);

        add(topPanel, BorderLayout.NORTH);
        add(listPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnDetails = new JButton("Dettagli Articolo");
        btnDetails.setBackground(Color.gray);
        btnDetails.setForeground(Color.white);
        btnDetails.addActionListener(actionListener);
        btnDetails.setActionCommand("" + BrowseListener.CommandKeyBrowse.SHOW_ARTICLE_DETAILS);
        bottomPanel.add(btnDetails);

        JButton btnItemRemove = new JButton("Rimuovi articolo");
        btnItemRemove.setBackground(Color.gray);
        btnItemRemove.setForeground(Color.white);
        btnItemRemove.addActionListener(actionListener);
        btnItemRemove.setActionCommand("" + BrowseListener.CommandKeyBrowse.REMOVE_ARTICLE_FROM_WISHLIST);
        bottomPanel.add(btnItemRemove);

        JButton btnEmptyWishlist = new JButton("Svuota lista");
        btnEmptyWishlist.setBackground(Color.gray);
        btnEmptyWishlist.setForeground(Color.white);
        btnEmptyWishlist.addActionListener(actionListener);
        btnEmptyWishlist.setActionCommand("" + BrowseListener.CommandKeyBrowse.EMPTY_WISHLIST);
        bottomPanel.add(btnEmptyWishlist);

        JButton btnDeleteWishlist = new JButton("Cancella lista");
        btnDeleteWishlist.setBackground(Color.gray);
        btnDeleteWishlist.setForeground(Color.white);
        btnDeleteWishlist.addActionListener(actionListener);
        btnDeleteWishlist.setActionCommand("" + BrowseListener.CommandKeyBrowse.DELETE_WISHLIST);
        bottomPanel.add(btnDeleteWishlist);

        JButton btnPDF = new JButton("Avvia Ordine");
        btnPDF.setBackground(Color.gray);
        btnPDF.setForeground(Color.white);
        btnPDF.addActionListener(actionListener);
        btnPDF.setActionCommand("" + BrowseListener.CommandKeyBrowse.ORDER);
        bottomPanel.add(btnPDF);

        bottomPanel.setBackground(Color.darkGray);
        add(bottomPanel, BorderLayout.SOUTH);

    }

    private void setListPanel(JTable jTable){
        listPanel.removeAll();
        table = jTable;
        listPanel.add(new JScrollPane(jTable));
        listPanel.invalidate();
        listPanel.revalidate();
        listPanel.repaint();
    }

    @Override
    public int getSelected() {
        int i = table.getSelectedRow();
        if (i<0) return -1;
        return Integer.parseInt((String) table.getValueAt(i, 0));
    }

    @Override
    public Feedback getSelectedFeedback() {
        return null;
    }

    public WishList getWishlist(){
        return (WishList) comboBoxWishlist.getSelectedItem();
    }

    private JTable createTable(WishList wishList){
        TableArticleFactory tableArticleFactory = new TableArticleFactory();
        String[][] data = tableArticleFactory.getData(TableArticleFactory.TableTypeArticles.WISHLIST_TYPE, wishList.getItems(), wishList.getServices());
        return tableArticleFactory.create(TableArticleFactory.TableTypeArticles.WISHLIST_TYPE, data);
    }

}
