package com.gabriel.draw;

import com.gabriel.draw.view.Splash;

import javax.swing.*;

public class SplashFrame {
    public static void main(String[] args) {
        JFrame splashFrame = new JFrame();
        Splash splashPanel = new Splash();
        splashFrame.add(splashPanel);
        splashFrame.setExtendedState(splashFrame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        splashFrame.setVisible(true);
        splashFrame.repaint();

    }
}
