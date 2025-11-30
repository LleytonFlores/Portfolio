package com.gabriel.draw.command;

import com.gabriel.drawfx.command.Command;
import com.gabriel.drawfx.model.Shape;
import com.gabriel.drawfx.service.AppService;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class MoveShapeCommand implements Command {
    private AppService appService;
    private List<ShapePosition> originalPositions;
    private List<Shape> shapes;
    private int dx;
    private int dy;

    //for single shape only
    public MoveShapeCommand(AppService appService, Shape shape, Point start, Point end) {
        this.appService = appService;
        this.shapes = new ArrayList<>();
        this.shapes.add(shape);
        this.dx = end.x - start.x;
        this.dy = end.y - start.y;
        this.originalPositions = new ArrayList<>();
    }

    //for multiple shapes
    public MoveShapeCommand(AppService appService, List<Shape> shapes, Point start, Point end) {
        this.appService = appService;
        this.shapes = new ArrayList<>(shapes);
        this.dx = end.x - start.x;
        this.dy = end.y - start.y;
        this.originalPositions = new ArrayList<>();
    }

    @Override
    public void execute() {
        //store orig position
        originalPositions.clear();
        for (Shape shape : shapes) {
            originalPositions.add(new ShapePosition(shape));
        }

        //main move formula
        for (Shape shape : shapes) {
            shape.getLocation().x += dx;
            shape.getLocation().y += dy;
        }
    }

    @Override
    public void undo() {
        //restore orig position
        for (int i = 0; i < shapes.size() && i < originalPositions.size(); i++) {
            ShapePosition orig = originalPositions.get(i);
            shapes.get(i).getLocation().x = orig.x;
            shapes.get(i).getLocation().y = orig.y;
        }
    }

    @Override
    public void redo() {
        //redo move
        for (Shape shape : shapes) {
            shape.getLocation().x += dx;
            shape.getLocation().y += dy;
        }
    }

    private static class ShapePosition {
        int x, y;
        ShapePosition(Shape s) {
            this.x = s.getLocation().x;
            this.y = s.getLocation().y;
        }
    }
}