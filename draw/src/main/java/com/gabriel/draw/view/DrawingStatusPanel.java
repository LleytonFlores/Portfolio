package com.gabriel.draw.view;

import com.gabriel.drawfx.service.AppService;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

public class DrawingStatusPanel extends JPanel {
    JLabel coordinateLabel = new JLabel("Command");
    JTextField xText = new JTextField();
    JTextField yText = new JTextField();

    public DrawingStatusPanel(){
        super.setBorder(new BevelBorder(BevelBorder.LOWERED));
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        JLabel statusLabel = new JLabel("status");
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        add(statusLabel);
        add(new JLabel("x: "));
        add(xText);
        add(new JLabel("y: "));
        add(yText);
        xText.setSize(16,36);
        yText.setSize(16,36);
        xText.setText("100");
        yText.setText("250");

        setPreferredSize(new Dimension( 1440,16));
        this.setBackground(Color.DARK_GRAY);
    }
    public void setPoint(Point p){
        xText.setText("x = " + String.valueOf(p.x));
        yText.setText("y = " + String.valueOf(p.y));
    }
}
