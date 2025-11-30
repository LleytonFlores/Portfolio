package com.gabriel.draw.service;

import com.gabriel.draw.command.AddShapeCommand;
import com.gabriel.drawfx.DrawMode;
import com.gabriel.drawfx.ShapeMode;
import com.gabriel.drawfx.command.Command;
import com.gabriel.drawfx.command.CommandService;
import com.gabriel.drawfx.model.Drawing;
import com.gabriel.drawfx.model.Shape;
import com.gabriel.drawfx.service.AppService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DrawingCommandAppService implements AppService {
    public AppService appService;
    protected static AppService drawingCommandAppService = null;

    protected DrawingCommandAppService(AppService appService){
        this.appService = appService;
    }

    public static AppService getInstance(){
        return drawingCommandAppService;
    }

    public static AppService getInstance(AppService appService){
        if(drawingCommandAppService == null){
            drawingCommandAppService = new DrawingCommandAppService(appService);
        };
        return drawingCommandAppService;
    }

    @Override
    public void undo() {
        CommandService.undo();
    }

    @Override
    public void redo() {
        CommandService.redo();
    }

    @Override
    public ShapeMode getShapeMode() {
        return appService.getShapeMode();
    }

    @Override
    public void setShapeMode(ShapeMode shapeMode) {
        appService.setShapeMode(shapeMode);
    }

    @Override
    public DrawMode getDrawMode() {
        return appService.getDrawMode();
    }

    @Override
    public void setDrawMode(DrawMode drawMode) {
        appService.setDrawMode(drawMode);
    }

    @Override
    public Color getColor() {
        return appService.getColor();
    }

    @Override
    public void setColor(Color color) {
        appService.setColor(color);
    }

    @Override
    public Color getFill() {
        return appService.getFill();
    }

    @Override
    public void setFill(Color color) {
        appService.setFill(color);
    }

    @Override
    public void move(Shape shape, Point start, Point end) {
        appService.move(shape, start, end);
    }

    @Override
    public void move(Point start, Point end) {
        appService.move(start, end);
    }

    @Override
    public void scale(Point start, Point end) {
        appService.scale(start, end);
    }

    @Override
    public void scale(Shape shape, Point start, Point end) {
        appService.scale(shape, start, end);
    }

    @Override
    public void scale(Shape shape, Point end) {
        appService.scale(shape, end);
    }

    @Override
    public void create(Shape shape) {
        Command command = new AddShapeCommand(appService, shape);
        CommandService.ExecuteCommand(command);
    }

    @Override
    public void delete(Shape shape) {
        appService.delete(shape);
    }

    @Override
    public void close() {
        appService.close();
    }

    @Override
    public Drawing getDrawing() {
        return appService.getDrawing();
    }

    @Override
    public void setDrawing(Drawing drawing) {
        appService.setDrawing(drawing);
    }

    @Override
    public int getSearchRadius() {
        return appService.getSearchRadius();
    }

    @Override
    public void setSearchRadius(int radius) {
        appService.setSearchRadius(radius);
    }

    @Override
    public void search(Point p) {
        appService.search(p);
    }

    @Override
    public void search(Point p, boolean single) {
        appService.search(p, single);
    }

    @Override
    public void open(String filename) {
        appService.open(filename);
    }


    @Override
    public void save() {
        appService.save();
    }

    @Override
    public String getFileName() {
        return appService.getFileName();
    }

    @Override
    public void select(Shape selectedShape) {
        appService.select(selectedShape);
    }

    @Override
    public void unSelect(Shape selectedShape) {
        appService.unSelect(selectedShape);
    }

    @Override
    public Shape getSelectedShape() {
        return appService.getSelectedShape();
    }

    @Override
    public List<Shape> getSelectedShapes() {
        return appService.getSelectedShapes();
    }

    @Override
    public void clearSelections(){
        appService.clearSelections();
    }

    @Override
    public void setThickness(int thickness) {
        appService.setThickness(thickness);
    }

    @Override
    public int getThickness() {
        return appService.getThickness();
    }

    @Override
    public void setXLocation(int xLocation) {
        appService.setXLocation(xLocation);
    }

    @Override
    public int getXLocation() {
        return appService.getXLocation();
    }

    @Override
    public void setYLocation(int yLocation) {
        appService.setYLocation(yLocation);
    }

    @Override
    public int getYLocation() {
        return appService.getYLocation();
    }

    @Override
    public void setWidth(int width) {
        appService.setWidth(width);
    }

    @Override
    public int getWidth() {
        return appService.getWidth();
    }

    @Override
    public void setHeight(int height) {
        appService.setHeight(height);
    }

    @Override
    public int getHeight() {
        return appService.getHeight();
    }

    @Override
    public void setImageFilename(String imageFilename) {
        appService.setImageFilename(imageFilename);
    }

    @Override
    public String getImageFilename() {
        return appService.getImageFilename();
    }

    @Override
    public void setText(String text) {
        appService.setText(text);
    }

    @Override
    public void setFontSize(int fontSize) {
        appService.setFontSize(fontSize);
    }

    @Override
    public Color getStartColor() {
        return appService.getStartColor();
    }

    @Override
    public void setStartColor(Color color) {
        appService.setStartColor(color);
    }

    @Override
    public Color getEndColor() {
        return appService.getEndColor();
    }

    @Override
    public void setEndColor(Color color) {
        appService.setEndColor(color);
    }

    @Override
    public boolean isGradient() {
        return appService.isGradient();
    }

    @Override
    public void setIsGradient(boolean yes) {
        appService.setIsGradient(yes);
    }

    @Override
    public boolean isVisible() {
        return appService.isVisible();
    }

    @Override
    public void setIsVisible(boolean yes) {
        appService.setIsVisible(yes);
    }

    @Override
    public void delete() {
        appService.delete();
    }

    @Override
    public void setStartX(int startx) {
        appService.setStartX(startx);
    }

    @Override
    public int getStartX() {
        return appService.getStartX();
    }

    @Override
    public void setStarty(int starty) {
        appService.setStarty(starty);
    }

    @Override
    public int getStarty() {
        return appService.getStarty();
    }

    @Override
    public void setEndx(int endx) {
        appService.setEndx(endx);
    }

    @Override
    public int getEndx() {
        return appService.getEndx();
    }

    @Override
    public void setEndy(int endy) {
        appService.setEndy(endy);
    }

    @Override
    public int getEndy() {
        return appService.getEndy();
    }

    @Override
    public String getText() {
        return appService.getText();
    }

    @Override
    public Font getFont() {
        return appService.getFont();
    }

    @Override
    public void setFont(Font font) {
        appService.setFont(font);
    }
}