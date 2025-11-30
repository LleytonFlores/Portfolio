package com.gabriel.draw.command;

import com.gabriel.drawfx.command.Command;
import com.gabriel.drawfx.model.Shape;
import com.gabriel.drawfx.service.AppService;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SetColorCommand implements Command {
    private AppService appService;
    private List<Shape> shapes;
    private Color newColor;
    private Map<Shape, Color> originalColors;

    //for single shape only
    public SetColorCommand(AppService appService, Shape shape, Color newColor) {
        this.appService = appService;
        this.shapes = new ArrayList<>();
        this.shapes.add(shape);
        this.newColor = newColor;
        this.originalColors = new HashMap<>();
    }

    //for multiple shapes
    public SetColorCommand(AppService appService, List<Shape> shapes, Color newColor) {
        this.appService = appService;
        this.shapes = new ArrayList<>(shapes);
        this.newColor = newColor;
        this.originalColors = new HashMap<>();
    }

    @Override
    public void execute() {
        //store colors
        originalColors.clear();
        for (Shape shape : shapes) {
            originalColors.put(shape, shape.getColor());
            shape.setColor(newColor);
        }
    }

    @Override
    public void undo() {
        //restore colors
        for (Shape shape : shapes) {
            Color originalColor = originalColors.get(shape);
            if (originalColor != null) {
                shape.setColor(originalColor);
            }
        }
    }

    @Override
    public void redo() {
        for (Shape shape : shapes) {
            shape.setColor(newColor);
        }
    }
}