package View;

import View.DxMenu.DxPanel;
import View.Listener.DxListener;
import View.Listener.SxListener;
import View.Listener.BrowseListener;
import View.SxMenu.SxPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class DefaultFrame extends JFrame implements IStandardFrame{
    private SxPanel sxPanel;
    private DxPanel dxPanel;
    private TopPanel topPanel;
    private JPanel mainPanel;

    public DefaultFrame(String title){

        super(title);
        setLayout( new BorderLayout() );
        setBackground(Color.darkGray);
        ActionListener sxListener = new SxListener(this);
        ActionListener dxListener = new DxListener(this);

        JMenuItem home = new JMenuItem("Home Page");
        home.setBackground(Color.GRAY);
        home.setForeground(Color.white);
        home.addActionListener(sxListener);
        home.setActionCommand("" + SxListener.CommandKeySx.HOME);

        JMenu createAccounts = new JMenu("Crea profili");
        createAccounts.setBackground(Color.GRAY);
        createAccounts.setForeground(Color.white);
        JMenuItem createManager = new JMenuItem("Crea un Manager");
        createManager.setBackground(Color.GRAY);
        createManager.setForeground(Color.white);
        createManager.addActionListener(sxListener);
        createManager.setActionCommand("" + SxListener.CommandKeySx.SHOW_ADD_MANAGER);
        JMenuItem createAdmin = new JMenuItem("Crea un Amministratore");
        createAdmin.setBackground(Color.GRAY);
        createAdmin.setForeground(Color.white);
        createAdmin.addActionListener(sxListener);
        createAdmin.setActionCommand("" + SxListener.CommandKeySx.SHOW_ADD_ADMIN);
        createAccounts.add(createManager);
        createAccounts.add(createAdmin);
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(Color.GRAY);
        menuBar.setForeground(Color.white);
        menuBar.add(home);
        menuBar.add(createAccounts);
        this.setJMenuBar(menuBar);

        topPanel = new TopPanel(null);
        sxPanel = new SxPanel(sxListener);
        try {
            mainPanel = new StartingPanel(new File("MyshopGif.gif"));
        }catch (IOException exception){
            JPanel tmpPanel= new JPanel();
            tmpPanel.setBackground(Color.darkGray);
            tmpPanel.add(new JLabel("Errore nel caricamento dell'immagine.(Path error)"));
            mainPanel = tmpPanel;
        }

        dxPanel = new DxPanel(dxListener);

        topPanel.setBackground(Color.darkGray);
        sxPanel.setBackground(Color.darkGray);
        mainPanel.setBackground(Color.darkGray);
        dxPanel.setBackground(Color.darkGray);


        add(topPanel, BorderLayout.NORTH);
        add(sxPanel, BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);
        add(dxPanel, BorderLayout.EAST);
        JPanel southPanel = new JPanel();
        southPanel.setBackground(Color.darkGray);
        JLabel southlbl = new JLabel("Esame: Principi di Ingegneria del Software 2022 | " +
                "Gruppo di lavoro: CULCEA Cezar, MARTINA Chiara | " +
                "Docenti: MAINETTI Luca, VERGALLO Roberto");

        southlbl.setForeground(Color.gray);
        southPanel.add(southlbl);
        add(southPanel, BorderLayout.SOUTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(/*Toolkit.getDefaultToolkit().getScreenSize()*/ 1000, 700);
        setResizable(false);
        setVisible(true);
    }
    @Override
    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void setMainPanel(JPanel panel){
        if(mainPanel != null){
            remove(mainPanel);
        }

        add(panel, BorderLayout.CENTER);

        mainPanel = panel;
    }

    public SxPanel getSxPanel() {
        return sxPanel;
    }

    public DxPanel getDxPanel() {
        return dxPanel;
    }

    public TopPanel getTopPanel() {
        return topPanel;
    }
}
