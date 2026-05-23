package io.github.toshiara.simla;

import javax.swing.JPanel;
import java.awt.Image;
import java.awt.Graphics;


public class ImageCanvasPanel extends JPanel {
    private Image image;

    // method for change image
    public void setImage(Image image) {
        this.image = image;
        this.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, this);
        }
    }
}
