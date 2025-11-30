package com.gabriel.drawfx.service;

import com.gabriel.drawfx.DrawMode;
import com.gabriel.drawfx.ShapeMode;
import com.gabriel.drawfx.model.Drawing;
import com.gabriel.drawfx.model.Shape;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public interface AppService {
    void undo();
    void redo();

    ShapeMode getShapeMode();
    void setShapeMode(ShapeMode shapeMode);

    DrawMode getDrawMode();
    void setDrawMode(DrawMode drawMode);

    Color getColor();
    void setColor(Color color);

    Color getFill();
    void setFill(Color color);

    void move (Shape shape, Point Start, Point newLoc);
    void move(Point start, Point newLoc) ;

    void scale(Point start, Point end);
    void scale(Shape shape, Point start, Point end);
    void scale(Shape shape, Point end);

    void create(Shape shape);
    void delete(Shape shape);

    void close();

    Drawing getDrawing();
    void setDrawing(Drawing drawing);

    int getSearchRadius();
    void setSearchRadius(int radius);

    void search(Point p);
    void search(Point p, boolean single);

    void open(String filename);
    void save();
    String getFileName();

    void select(Shape shape);
    void unSelect(Shape shape);

    Shape getSelectedShape();
    List<Shape> getSelectedShapes();
    void clearSelections();

    void setThickness(int thickness);
    int getThickness();

    void setXLocation(int xLocation);
    int getXLocation();
    void setYLocation(int yLocation);
    int getYLocation();

    void setWidth(int width);
    int getWidth();
    void setHeight(int height);
    int getHeight();

    void setImageFilename(String filename);
    String getImageFilename();
    void setText(String text);

    void setFontSize(int fontSize);

    Color getStartColor();
    void setStartColor( Color color);
    Color getEndColor();
    void setEndColor( Color color);
    boolean isGradient();
    void setIsGradient(boolean yes);
    boolean isVisible();
    void setIsVisible(boolean yes);
    void delete();

    void setStartX(int startx);
    int  getStartX();
    void setStarty(int starty);
    int  getStarty();

    void setEndx(int endxy);
    int  getEndx();
    void setEndy(int endy);
    int  getEndy();

    String getText();
    Font getFont();
    void setFont(Font font);
}
