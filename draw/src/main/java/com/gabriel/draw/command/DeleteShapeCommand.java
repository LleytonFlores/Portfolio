package com.gabriel.draw.command;

import com.gabriel.drawfx.command.Command;
import com.gabriel.drawfx.model.Shape;
import com.gabriel.drawfx.service.AppService;

import java.util.ArrayList;
import java.util.List;

public class DeleteShapeCommand implements Command {
    private AppService appService;
    private List<Shape> deletedShapes;
    private List<Integer> originalIndices;

    public DeleteShapeCommand(AppService appService, Shape shape) {
        this.appService = appService;
        this.deletedShapes = new ArrayList<>();
        this.deletedShapes.add(shape);
        this.originalIndices = new ArrayList<>();
    }

    public DeleteShapeCommand(AppService appService, List<Shape> shapes) {
        this.appService = appService;
        this.deletedShapes = new ArrayList<>(shapes);
        this.originalIndices = new ArrayList<>();
    }

    @Override
    public void execute() {
        List<Shape> allShapes = appService.getDrawing().getShapes();

        //store before delete
        originalIndices.clear();
        for (Shape shape : deletedShapes) {
            originalIndices.add(allShapes.indexOf(shape));
            appService.delete(shape);
        }
    }

    @Override
    public void undo() {
        //revert to old position
        List<Shape> allShapes = appService.getDrawing().getShapes();
        for (int i = 0; i < deletedShapes.size(); i++) {
            Shape shape = deletedShapes.get(i);
            int originalIndex = originalIndices.get(i);

            //insert if possible
            if (originalIndex >= 0 && originalIndex <= allShapes.size()) {
                allShapes.add(originalIndex, shape);
            } else {
                allShapes.add(shape);
            }
        }
    }

    @Override
    public void redo() {
        execute();
    }
}