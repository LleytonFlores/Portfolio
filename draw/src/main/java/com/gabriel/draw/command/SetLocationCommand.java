package com.gabriel.draw.command;

import com.gabriel.drawfx.command.Command;
import com.gabriel.drawfx.model.Shape;
import com.gabriel.drawfx.service.AppService;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SetLocationCommand implements Command {
    private AppService appService;
    private List<Shape> shapes;
    private Map<Shape, Point> originalLocations;
    private Map<Shape, Point> newLocations;

    public SetLocationCommand(AppService appService, List<Shape> shapes) {
        this.appService = appService;
        this.shapes = new ArrayList<>(shapes);
        this.originalLocations = new HashMap<>();
        this.newLocations = new HashMap<>();
    }

    public void captureOriginal() {
        for (Shape shape : shapes) {
            originalLocations.put(shape, new Point(shape.getLocation().x, shape.getLocation().y));
        }
    }

    public void captureNew() {
        for (Shape shape : shapes) {
            newLocations.put(shape, new Point(shape.getLocation().x, shape.getLocation().y));
        }
    }

    @Override
    public void execute() {
        if (newLocations.isEmpty()) {
            captureNew();
        }
        for (Shape shape : shapes) {
            Point newLoc = newLocations.get(shape);
            if (newLoc != null) {
                shape.getLocation().x = newLoc.x;
                shape.getLocation().y = newLoc.y;
            }
        }
    }

    @Override
    public void undo() {
        for (Shape shape : shapes) {
            Point origLoc = originalLocations.get(shape);
            if (origLoc != null) {
                shape.getLocation().x = origLoc.x;
                shape.getLocation().y = origLoc.y;
            }
        }
    }

    @Override
    public void redo() {
        execute();
    }
}

