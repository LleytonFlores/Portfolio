package com.gabriel.draw.command;

import com.gabriel.drawfx.command.Command;
import com.gabriel.drawfx.model.Shape;
import com.gabriel.drawfx.service.AppService;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SetFillCommand implements Command {
    private AppService appService;
    private List<Shape> shapes;
    private Color newFill;
    private Map<Shape, Color> originalFills;

    //for single shape only
    public SetFillCommand(AppService appService, Shape shape, Color newFill) {
        this.appService = appService;
        this.shapes = new ArrayList<>();
        this.shapes.add(shape);
        this.newFill = newFill;
        this.originalFills = new HashMap<>();
    }

    //for multiple shapes
    public SetFillCommand(AppService appService, List<Shape> shapes, Color newFill) {
        this.appService = appService;
        this.shapes = new ArrayList<>(shapes);
        this.newFill = newFill;
        this.originalFills = new HashMap<>();
    }

    @Override
    public void execute() {
        //store fill
        originalFills.clear();
        for (Shape shape : shapes) {
            originalFills.put(shape, shape.getFill());
            shape.setFill(newFill);
        }
    }

    @Override
    public void undo() {
        //restore fill
        for (Shape shape : shapes) {
            Color originalFill = originalFills.get(shape);
            if (originalFill != null) {
                shape.setFill(originalFill);
            }
        }
    }

    @Override
    public void redo() {
        for (Shape shape : shapes) {
            shape.setFill(newFill);
        }
    }
}