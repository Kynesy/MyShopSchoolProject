package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class InformationFrame extends JFrame implements IStandardFrame{
    private JPanel jPanel;

    public InformationFrame(String title, JPanel jPanel) throws HeadlessException {
        super(title);
        this.jPanel = jPanel;

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        if(jPanel!=null) {
            add(jPanel);
        }
        setSize(800,600);
        setResizable(false);
        setVisible(true);
    }

    public void setActionListener(ActionListener actionListener) {
    }

    @Override
    public JPanel getMainPanel() {
        return jPanel;
    }

    @Override
    public void setMainPanel(JPanel newPanel) {
        if(jPanel != null){
            remove(jPanel);
        }
        add(newPanel);
        invalidate();
        revalidate();
        repaint();
        this.jPanel = newPanel;
    }
}
