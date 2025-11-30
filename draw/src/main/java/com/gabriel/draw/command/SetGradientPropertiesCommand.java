package com.gabriel.draw.command;

import com.gabriel.drawfx.command.Command;
import com.gabriel.drawfx.model.Shape;
import com.gabriel.drawfx.service.AppService;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SetGradientPropertiesCommand implements Command {
    private AppService appService;
    private List<Shape> shapes;
    private Color newStartColor;
    private Color newEndColor;
    private Boolean newIsGradient;

    private Map<Shape, GradientState> originalStates;

    public SetGradientPropertiesCommand(AppService appService, List<Shape> shapes,
                                        Color startColor, Color endColor, Boolean isGradient) {
        this.appService = appService;
        this.shapes = new ArrayList<>(shapes);
        this.newStartColor = startColor;
        this.newEndColor = endColor;
        this.newIsGradient = isGradient;
        this.originalStates = new HashMap<>();
    }

    @Override
    public void execute() {
        //store old state
        originalStates.clear();
        for (Shape shape : shapes) {
            originalStates.put(shape, new GradientState(shape));

            if (newStartColor != null) {
                shape.setStartColor(newStartColor);
            }
            if (newEndColor != null) {
                shape.setEndColor(newEndColor);
            }
            if (newIsGradient != null) {
                shape.setGradient(newIsGradient);
            }
        }
    }

    @Override
    public void undo() {
        for (Shape shape : shapes) {
            GradientState state = originalStates.get(shape);
            if (state != null) {
                shape.setStartColor(state.startColor);
                shape.setEndColor(state.endColor);
                shape.setGradient(state.isGradient);
            }
        }
    }

    @Override
    public void redo() {
        for (Shape shape : shapes) {
            if (newStartColor != null) {
                shape.setStartColor(newStartColor);
            }
            if (newEndColor != null) {
                shape.setEndColor(newEndColor);
            }
            if (newIsGradient != null) {
                shape.setGradient(newIsGradient);
            }
        }
    }

    private static class GradientState {
        Color startColor;
        Color endColor;
        boolean isGradient;

        GradientState(Shape s) {
            this.startColor = s.getStartColor();
            this.endColor = s.getEndColor();
            this.isGradient = s.isGradient();
        }
    }
}
