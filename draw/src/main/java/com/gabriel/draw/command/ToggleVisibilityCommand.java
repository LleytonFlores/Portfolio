package com.gabriel.draw.command;

import com.gabriel.drawfx.command.Command;
import com.gabriel.drawfx.model.Shape;
import com.gabriel.drawfx.service.AppService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ToggleVisibilityCommand implements Command {
    private AppService appService;
    private List<Shape> shapes;
    private Map<Shape, Boolean> originalVisibility;
    private boolean newVisibility;

    public ToggleVisibilityCommand(AppService appService, List<Shape> shapes, boolean newVisibility) {
        this.appService = appService;
        this.shapes = new ArrayList<>(shapes);
        this.newVisibility = newVisibility;
        this.originalVisibility = new HashMap<>();
    }

    @Override
    public void execute() {
        originalVisibility.clear();
        for (Shape shape : shapes) {
            originalVisibility.put(shape, shape.isVisible());
            shape.setVisible(newVisibility);
        }
    }

    @Override
    public void undo() {
        for (Shape shape : shapes) {
            Boolean orig = originalVisibility.get(shape);
            if (orig != null) {
                shape.setVisible(orig);
            }
        }
    }

    @Override
    public void redo() {
        for (Shape shape : shapes) {
            shape.setVisible(newVisibility);
        }
    }
}