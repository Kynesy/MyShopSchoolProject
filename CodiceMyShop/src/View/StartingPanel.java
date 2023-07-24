package View;

import Business.ImageUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class StartingPanel extends JPanel {
    private File foto;

    public StartingPanel(File foto) throws IOException {
        this.foto = foto;

        setBackground(Color.darkGray);
        Icon icon  = new ImageIcon(foto.getPath());
        JLabel lblGif = new JLabel(icon);
        lblGif.setBackground(Color.darkGray);
        lblGif.setForeground(Color.white);
        add(lblGif);
//
//        BufferedImage im = ImageIO.read(new File(foto.getName()));
//        Image tmpImage = im.getScaledInstance(700, 400, Image.SCALE_SMOOTH);
//        JLabel lblFoto = new JLabel(new ImageIcon(tmpImage));
  //      add(lblFoto);
    }
}
