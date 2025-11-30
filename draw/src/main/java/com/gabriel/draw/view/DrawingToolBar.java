package com.gabriel.draw.view;

import com.gabriel.draw.controller.ActionController;
import com.gabriel.drawfx.ActionCommand;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;

public class DrawingToolBar extends JToolBar {

    ActionListener actionListener;

    public DrawingToolBar(ActionListener actionListener) {
        setFloatable(false);
        setRollover(true);
        this.actionListener = actionListener;
        addButtons();

        setPreferredSize(new Dimension(200, 40));
    }

    protected void addButtons() {
        JButton button = null;

        button = makeNavigationButton("undoButton", ActionCommand.UNDO, "Undo (Ctrl+Z)", "Undo");
        add(button);

        button = makeNavigationButton("redoButton", ActionCommand.REDO, "Redo (Ctrl+Shift+Z)", "Redo");
        add(button);

        addSeparator();

        button = makeNavigationButton("rect", ActionCommand.RECT, "Draw a rectangle", ActionCommand.RECT);
        add(button);

        button = makeNavigationButton("line", ActionCommand.LINE, "Draw a line", ActionCommand.LINE);
        add(button);

        button = makeNavigationButton("ellipse", ActionCommand.ELLIPSE, "Draw an ellipse", ActionCommand.ELLIPSE);
        add(button);

        button = makeNavigationButton("text", ActionCommand.TEXT, "Add a text", ActionCommand.TEXT);
        add(button);

        button = makeNavigationButton("image", ActionCommand.IMAGE, "Add an image", ActionCommand.IMAGE);
        add(button);

        button = makeNavigationButton("select", ActionCommand.SELECT, "Switch to select", ActionCommand.SELECT);
        add(button);

        addSeparator();

        button = makeNavigationButton("outlineColor", ActionCommand.COLOR, "Set outline color", "Outline");
        add(button);

        button = makeNavigationButton("fillColor", ActionCommand.FILL, "Set fill color", "Fill");
        add(button);

        button = makeNavigationButton("noFill", "NO_FILL", "Remove fill color", "No Fill");
        add(button);

        addSeparator();

        button = makeNavigationButton("insertimage", ActionCommand.SET_IMAGE, "Insert image into selected shape", "Insert Image");
        add(button);

        addSeparator();

        button = makeNavigationButton("imagefile", ActionCommand.IMAGEFILE, "Select another image", ActionCommand.IMAGEFILE);
        add(button);

        button = makeNavigationButton("font", ActionCommand.FONT, "Select another font", ActionCommand.FONT);
        add(button);
    }

    protected JButton makeNavigationButton(String imageName,
                                           String actionCommand,
                                           String toolTipText,
                                           String altText) {
        //find image
        String imgLocation = "images/" + imageName + ".png";
        URL imageURL = DrawingToolBar.class.getResource(imgLocation);

        //create button
        JButton button = new JButton();
        button.setActionCommand(actionCommand);
        button.setToolTipText(toolTipText);
        button.addActionListener(actionListener);

        if (imageURL != null) {
            //use image
            button.setIcon(new ImageIcon(imageURL, altText));
        } else {
            //if no image, use text
            button.setText(altText);
            System.err.println("Resource not found: " + imgLocation);
        }
        return button;
    }
}
