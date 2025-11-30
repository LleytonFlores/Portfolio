package com.gabriel.draw.command;

import com.gabriel.drawfx.command.Command;
import com.gabriel.drawfx.model.Shape;
import com.gabriel.drawfx.service.AppService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SetSizeCommand implements Command {
    private AppService appService;
    private List<Shape> shapes;
    private Map<Shape, Size> originalSizes;
    private Map<Shape, Size> newSizes;

    public SetSizeCommand(AppService appService, List<Shape> shapes) {
        this.appService = appService;
        this.shapes = new ArrayList<>(shapes);
        this.originalSizes = new HashMap<>();
        this.newSizes = new HashMap<>();
    }

    public void captureOriginal() {
        for (Shape shape : shapes) {
            originalSizes.put(shape, new Size(shape.getWidth(), shape.getHeight()));
        }
    }

    public void captureNew() {
        for (Shape shape : shapes) {
            newSizes.put(shape, new Size(shape.getWidth(), shape.getHeight()));
        }
    }

    @Override
    public void execute() {
        if (newSizes.isEmpty()) {
            captureNew();
        }
        for (Shape shape : shapes) {
            Size newSize = newSizes.get(shape);
            if (newSize != null) {
                shape.setWidth(newSize.width);
                shape.setHeight(newSize.height);
            }
        }
    }

    @Override
    public void undo() {
        for (Shape shape : shapes) {
            Size origSize = originalSizes.get(shape);
            if (origSize != null) {
                shape.setWidth(origSize.width);
                shape.setHeight(origSize.height);
            }
        }
    }

    @Override
    public void redo() {
        execute();
    }

    private static class Size {
        int width, height;
        Size(int w, int h) { width = w; height = h; }
    }
}