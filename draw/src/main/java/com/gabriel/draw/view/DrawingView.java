package com.gabriel.draw.view;

import com.gabriel.draw.renderer.SelectionRenderer;
import com.gabriel.drawfx.model.Drawing;
import com.gabriel.drawfx.model.Shape;
import com.gabriel.drawfx.service.AppService;

import java.util.List;
import javax.swing.*;
import java.awt.*;

public class DrawingView extends JPanel {

    AppService appService;

    public DrawingView(AppService appService){
        this.appService = appService;
        JTextArea textArea = new JTextArea();
        add(textArea);
        textArea.setVisible(true);
        setLayout(null);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Drawing drawing = appService.getDrawing();
        List<Shape> shapes = drawing.getShapes();

        //render shape without handles
        for(Shape shape : shapes){
            shape.getRendererService().render(g, shape, false);
        }

        //render both single and multi shape handles
        List<Shape> selectedShapes = appService.getSelectedShapes();
        if (!selectedShapes.isEmpty()) {
            SelectionRenderer.renderSelection(g, selectedShapes);
        }
    }
}