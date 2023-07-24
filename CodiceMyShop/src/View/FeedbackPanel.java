package View;

import View.Listener.BrowseListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class FeedbackPanel extends JPanel {
    private ActionListener actionListener;
    private JComboBox<String> ratingComboBox;
    private JTextArea txtMessage;
    private int articleId;

    public FeedbackPanel(ActionListener actionListener, int articleId){
        this.actionListener = actionListener;
        this.articleId = articleId;
        setLayout(new BorderLayout());
        setBackground(Color.darkGray);

        JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel centerPanel = new JPanel(new GridLayout(2,1));
        JPanel centerPanelUp = new JPanel(new GridLayout(6,2));
        JPanel centerPanelDown = new JPanel(new GridLayout(3,1));
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JLabel lblTitle = new JLabel("Lascia il tuo feedback qui!");
        lblTitle.setForeground(Color.white);

        JLabel lblVote = new JLabel("Indice di gradimento:");
        lblVote.setForeground(Color.white);
        ratingComboBox = new JComboBox<>(new String[]{"★","★★","★★★","★★★★","★★★★★"});
        ratingComboBox.setSelectedIndex(4);
        ratingComboBox.setForeground(Color.white);
        ratingComboBox.setBackground(Color.gray);

        JLabel lblMessage = new JLabel("Scrivi la tua recensione (Max 150 caratteri):");
        lblMessage.setForeground(Color.white);
        txtMessage = new JTextArea(20,5);
        txtMessage.setPreferredSize(new Dimension(600,100));
        txtMessage.setLineWrap(true);

        JButton btnCreateFeedback = new JButton("Pubblica recensione!");
        btnCreateFeedback.setForeground(Color.white);
        btnCreateFeedback.setBackground(Color.gray);
        btnCreateFeedback.addActionListener(actionListener);
        btnCreateFeedback.setActionCommand("" + BrowseListener.CommandKeyBrowse.CREATE_FEEDBACK);

        northPanel.add(lblTitle);
        centerPanelUp.add(lblVote);
        centerPanelUp.add(ratingComboBox);
        centerPanelUp.add(lblMessage);
        centerPanelDown.add(txtMessage);
        southPanel.add(btnCreateFeedback);

        centerPanelUp.setBackground(Color.darkGray);
        centerPanelDown.setBackground(Color.darkGray);
        centerPanel.add(centerPanelUp);
        centerPanel.add(centerPanelDown);

        northPanel.setBackground(Color.darkGray);
        centerPanel.setBackground(Color.darkGray);
        southPanel.setBackground(Color.darkGray);

        add(northPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
    }

    public int getRatingComboBox() {
        return ratingComboBox.getSelectedIndex();
    }

    public String getTxtMessage() {
        return txtMessage.getText();
    }

    public int getArticleId() {
        return articleId;
    }
}
