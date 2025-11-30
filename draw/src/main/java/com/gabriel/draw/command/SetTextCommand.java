package com.gabriel.draw.command;

import com.gabriel.drawfx.command.Command;
import com.gabriel.drawfx.model.Shape;
import com.gabriel.drawfx.service.AppService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SetTextCommand implements Command {
    private AppService appService;
    private List<Shape> shapes;
    private String newText;
    private Map<Shape, String> originalText;

    public SetTextCommand(AppService appService, List<Shape> shapes, String newText) {
        this.appService = appService;
        this.shapes = new ArrayList<>(shapes);
        this.newText = newText;
        this.originalText = new HashMap<>();
    }

    @Override
    public void execute() {
        originalText.clear();
        for (Shape shape : shapes) {
            originalText.put(shape, shape.getText());
            shape.setText(newText);
        }
    }

    @Override
    public void undo() {
        for (Shape shape : shapes) {
            String orig = originalText.get(shape);
            if (orig != null) {
                shape.setText(orig);
            }
        }
    }

    @Override
    public void redo() {
        for (Shape shape : shapes) {
            shape.setText(newText);
        }
    }
}
