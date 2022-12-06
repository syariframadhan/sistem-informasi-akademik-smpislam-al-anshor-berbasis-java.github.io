package Image;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 *
 * @author taopik
 */
public class menu_login extends JPanel {
    private BufferedImage image;
    
    public menu_login() {
       try {                
          image = ImageIO.read(getClass().getResource("form-login.jpg"));
       } catch (IOException ex) {
            // handle exception...
       }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);        
    }
}