package View;

import Model.Feedback;
import Model.Item;
import Model.Service;
import View.BrowseButtons.BrowseButtons;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class BrowsePanel extends JPanel implements IArticlePannels{
    private ActionListener listener;
    private JTable articleTable;

    public BrowsePanel(ActionListener listener, JTable articleTable) {
        this.listener = listener;
        this.articleTable = articleTable;

        setLayout( new BorderLayout() );
        setBackground(Color.darkGray);
        JLabel lblItemList = new JLabel("Di seguito gli Articoli");
        lblItemList.setForeground(Color.white);
        add(lblItemList, BorderLayout.NORTH);

        add(new JScrollPane(articleTable), BorderLayout.CENTER);

        JPanel browseButtons = new BrowseButtons(listener);
        add(browseButtons, BorderLayout.SOUTH);
    }

    @Override
    public int getSelected() {
        int i = articleTable.getSelectedRow();
        if (i<0) return -1;
        return Integer.parseInt((String) articleTable.getValueAt(i, 0));
    }

    @Override
    public Feedback getSelectedFeedback() {
        return null;
    }
}
