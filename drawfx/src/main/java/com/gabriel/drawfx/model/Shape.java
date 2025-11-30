package com.gabriel.drawfx.model;

import com.gabriel.drawfx.SelectionMode;
import com.gabriel.drawfx.renderer.Renderer;
import lombok.Data;

import java.awt.*;

@Data
public abstract class Shape {
    boolean selected = false;
    boolean visible = true;
    private SelectionMode selectionMode = SelectionMode.None;
    private int r = 2;
    private int id = 0;
    private Point location;
    private int width = 0;
    private int height = 0;
    private Color color = Color.RED;
    private Color fill;
    private int thickness;
    String text;
    Font font;
    String imageFilename;

    private Color startColor = Color.WHITE;
    private Color endColor = Color.RED;
    boolean isGradient = false;
    Point start;
    Point end;

    private Renderer rendererService;

    public Shape(){

    }
    public Shape(Point location){
        this.setLocation(location);
        this.start = new Point(0,0);
        this.end = new Point(100,0);
    }
    public Shape(Point location, Point endpoint){
        width = endpoint.x - location.x;
        height = endpoint.y - location.y;
        if(width<0){
            width = -width;
            endpoint.x -= width;
        }
        if(height < 0){
            height = -height;
            endpoint.y -= height;
        }
        this.setLocation(location);
        this.start = new Point(0,0);
        this.end = new Point(100,0);
  }
    public Shape(Point location, int width, int height){
        this.setLocation(location);
        this.width = width;
        this.height = height;
        this.start = new Point(0,0);
        this.end = new Point(100,0);
    }

    public boolean equals (Shape shape){
        return (this.id == shape.id);
    }
}
