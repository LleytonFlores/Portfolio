package com.gabriel.draw.command;

import com.gabriel.drawfx.command.Command;
import com.gabriel.drawfx.model.Shape;
import com.gabriel.drawfx.service.AppService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SetThicknessCommand implements Command {
    private AppService appService;
    private List<Shape> shapes;
    private int newThickness;
    private Map<Shape, Integer> originalThickness;

    public SetThicknessCommand(AppService appService, List<Shape> shapes, int newThickness) {
        this.appService = appService;
        this.shapes = new ArrayList<>(shapes);
        this.newThickness = newThickness;
        this.originalThickness = new HashMap<>();
    }

    @Override
    public void execute() {
        originalThickness.clear();
        for (Shape shape : shapes) {
            originalThickness.put(shape, shape.getThickness());
            shape.setThickness(newThickness);
        }
    }

    @Override
    public void undo() {
        for (Shape shape : shapes) {
            Integer orig = originalThickness.get(shape);
            if (orig != null) {
                shape.setThickness(orig);
            }
        }
    }

    @Override
    public void redo() {
        for (Shape shape : shapes) {
            shape.setThickness(newThickness);
        }
    }
}

