package com.gabriel.draw.command;

import com.gabriel.drawfx.command.Command;
import com.gabriel.drawfx.model.Shape;
import com.gabriel.drawfx.service.AppService;

import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SetFontCommand implements Command {
    private AppService appService;
    private List<Shape> shapes;
    private Font newFont;
    private Map<Shape, Font> originalFonts;

    public SetFontCommand(AppService appService, List<Shape> shapes, Font newFont) {
        this.appService = appService;
        this.shapes = new ArrayList<>(shapes);
        this.newFont = newFont;
        this.originalFonts = new HashMap<>();
    }

    @Override
    public void execute() {
        originalFonts.clear();
        for (Shape shape : shapes) {
            originalFonts.put(shape, shape.getFont());
            shape.setFont(newFont);
        }
    }

    @Override
    public void undo() {
        for (Shape shape : shapes) {
            Font orig = originalFonts.get(shape);
            if (orig != null) {
                shape.setFont(orig);
            }
        }
    }

    @Override
    public void redo() {
        for (Shape shape : shapes) {
            shape.setFont(newFont);
        }
    }
}
