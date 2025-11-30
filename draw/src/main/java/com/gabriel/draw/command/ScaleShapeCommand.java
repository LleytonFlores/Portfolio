package com.gabriel.draw.command;

import com.gabriel.drawfx.command.Command;
import com.gabriel.drawfx.model.Shape;
import com.gabriel.drawfx.service.AppService;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class ScaleShapeCommand implements Command {
    private AppService appService;
    private List<ShapeTransform> originalTransforms;
    private List<ShapeTransform> newTransforms;
    private List<Shape> shapes;

    //for single shape only
    public ScaleShapeCommand(AppService appService, Shape shape) {
        this.appService = appService;
        this.shapes = new ArrayList<>();
        this.shapes.add(shape);
        this.originalTransforms = new ArrayList<>();
        this.newTransforms = new ArrayList<>();
    }

    //for multiple shapes
    public ScaleShapeCommand(AppService appService, List<Shape> shapes) {
        this.appService = appService;
        this.shapes = new ArrayList<>(shapes);
        this.originalTransforms = new ArrayList<>();
        this.newTransforms = new ArrayList<>();
    }

    public void captureOriginalState() {
        originalTransforms.clear();
        for (Shape shape : shapes) {
            originalTransforms.add(new ShapeTransform(shape));
        }
    }

    public void captureNewState() {
        newTransforms.clear();
        for (Shape shape : shapes) {
            newTransforms.add(new ShapeTransform(shape));
        }
    }

    @Override
    public void execute() {
        if (newTransforms.isEmpty()) {
            captureNewState();
        }
        applyTransforms(newTransforms);
    }

    @Override
    public void undo() {
        applyTransforms(originalTransforms);
    }

    @Override
    public void redo() {
        applyTransforms(newTransforms);
    }

    private void applyTransforms(List<ShapeTransform> transforms) {
        for (int i = 0; i < shapes.size() && i < transforms.size(); i++) {
            ShapeTransform transform = transforms.get(i);
            Shape shape = shapes.get(i);

            shape.getLocation().x = transform.x;
            shape.getLocation().y = transform.y;
            shape.setWidth(transform.width);
            shape.setHeight(transform.height);
        }
    }

    private static class ShapeTransform {
        int x, y, width, height;

        ShapeTransform(Shape s) {
            this.x = s.getLocation().x;
            this.y = s.getLocation().y;
            this.width = s.getWidth();
            this.height = s.getHeight();
        }
    }
}