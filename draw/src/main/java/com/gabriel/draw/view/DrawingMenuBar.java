package com.gabriel.draw.view;

import com.gabriel.drawfx.ActionCommand;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class DrawingMenuBar extends JMenuBar {

    ActionListener actionListener;

    public DrawingMenuBar(ActionListener actionListener) {
        super();
        this.actionListener = actionListener;

        //file
        JMenu menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_F);

        JMenuItem menuItem = new JMenuItem("New");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
        menuItem.addActionListener(actionListener);
        menuItem.setActionCommand(ActionCommand.NEW);
        menu.add(menuItem);

        menuItem = new JMenuItem("Open");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        menuItem.addActionListener(actionListener);
        menuItem.setActionCommand(ActionCommand.OPEN);
        menu.add(menuItem);

        menuItem = new JMenuItem("Save");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        menuItem.addActionListener(actionListener);
        menuItem.setActionCommand(ActionCommand.SAVE);
        menu.add(menuItem);

        menuItem = new JMenuItem("SaveAs");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
        menuItem.addActionListener(actionListener);
        menuItem.setActionCommand(ActionCommand.SAVEAS);
        menu.add(menuItem);

        add(menu);

        //edit
        menu = new JMenu("Edit");
        menu.setMnemonic(KeyEvent.VK_E);

        menuItem = new JMenuItem("Undo");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK));
        menuItem.addActionListener(actionListener);
        menuItem.setActionCommand(ActionCommand.UNDO);
        menu.add(menuItem);

        menuItem = new JMenuItem("Redo");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
        menuItem.addActionListener(actionListener);
        menuItem.setActionCommand(ActionCommand.REDO);
        menu.add(menuItem);

        menu.addSeparator();

        menuItem = new JMenuItem("Delete");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        menuItem.addActionListener(actionListener);
        menuItem.setActionCommand(ActionCommand.DELETE);
        menu.add(menuItem);

        add(menu);

        //draw
        menu = new JMenu("Draw");
        menu.setMnemonic(KeyEvent.VK_D);

        menuItem = new JMenuItem("Line");
        menuItem.setActionCommand(ActionCommand.LINE);
        menuItem.addActionListener(actionListener);
        menu.add(menuItem);

        menuItem = new JMenuItem("Rectangle");
        menuItem.setActionCommand(ActionCommand.RECT);
        menuItem.addActionListener(actionListener);
        menu.add(menuItem);

        menuItem = new JMenuItem("Ellipse");
        menu.add(menuItem);
        menuItem.setActionCommand(ActionCommand.ELLIPSE);
        menuItem.addActionListener(actionListener);

        menuItem = new JMenuItem("Picture");
        menu.add(menuItem);
        menuItem.setActionCommand(ActionCommand.IMAGE);
        menuItem.addActionListener(actionListener);

        menuItem = new JMenuItem("Text");
        menuItem.setActionCommand(ActionCommand.TEXT);
        menuItem.addActionListener(actionListener);
        menu.add(menuItem);

        menuItem = new JMenuItem("Select");
        menuItem.setActionCommand(ActionCommand.SELECT);
        menuItem.addActionListener(actionListener);
        menu.add(menuItem);

        add(menu);

        //properties
        menu = new JMenu("Properties");
        menu.setMnemonic(KeyEvent.VK_P);

        menuItem = new JMenuItem("Color");
        menuItem.setActionCommand(ActionCommand.COLOR);
        menuItem.addActionListener(actionListener);
        menu.add(menuItem);

        menuItem = new JMenuItem("Fill");
        menuItem.setActionCommand(ActionCommand.FILL);
        menuItem.addActionListener(actionListener);
        menu.add(menuItem);

        menuItem = new JMenuItem("Set Image");
        menuItem.setActionCommand(ActionCommand.SET_IMAGE);
        menuItem.addActionListener(actionListener);
        menu.add(menuItem);

        this.add(menu);
    }
}