package com.gabriel.draw.view;

import com.gabriel.draw.util.ImageLoader;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Splash extends JPanel implements MouseListener {
    private BufferedImage image;
    private GPanel gPanel;
    ImageLoader imageLoader;
    int width;
    int height;
    public Splash() {
        try {
            imageLoader = new ImageLoader();
            image = imageLoader.loadImage("/SplashScreen.png");
//            image = ImageIO.read(new File("src\\main\\resources\\nette1440_800.png"));
            height = image.getHeight();
            width = image.getWidth();

        } catch (IOException ex) {
        }
        setSize(width, height);
        setLayout(null);

        gPanel = new GPanel("ItsDrawingTime");
        gPanel.setBounds(750,400,334,188);
        gPanel.addMouseListener(this);
        gPanel.setOpaque(false);
        this.add(gPanel);
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0,this.getWidth(), this.getHeight(),null); // see javadoc for more info on the parameters
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(e.getSource()==gPanel){
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);

            DrawingFrame mf = new DrawingFrame();
            mf.setExtendedState(mf.getExtendedState() | JFrame.MAXIMIZED_BOTH);
            mf.setVisible(true);

            topFrame.setVisible(false);;
            this.dispatchEvent(new WindowEvent(topFrame, WindowEvent.WINDOW_CLOSING));
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
