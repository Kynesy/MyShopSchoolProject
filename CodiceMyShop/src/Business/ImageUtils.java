package Business;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageUtils {
    public static ImageIcon scaleImage(int WIDTH, int HEIGHT, File file) throws IOException {
        BufferedImage im = ImageIO.read(new File(file.getName()));
        Image tmpImage = im.getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH);
        file.delete();
        return new ImageIcon(tmpImage);
    }
}
