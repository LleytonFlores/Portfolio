// CORRECTED XmlDocumentService.java
// Fixed line 98 - setAttribute with String instead of boolean

package com.gabriel.draw.service;

import com.gabriel.draw.model.Ellipse;
import com.gabriel.draw.model.Line;
import com.gabriel.draw.model.Rectangle;
import com.gabriel.drawfx.SelectionMode;
import com.gabriel.drawfx.ShapeMode;
import com.gabriel.drawfx.model.Drawing;
import com.gabriel.drawfx.service.DocumentService;
import org.w3c.dom.*;
import com.gabriel.drawfx.model.Shape;
import com.gabriel.draw.model.Text;
import com.gabriel.draw.model.Image;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.awt.*;
import java.io.File;

public class XmlDocumentService implements DocumentService {

    Drawing drawing;

    public XmlDocumentService(Drawing drawing) {
        this.drawing = drawing;
    }

    @Override
    public void save() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();

            Element root = document.createElement("Drawing");
            document.appendChild(root);

            //save properties
            setAttribute(root, "filename", drawing.getFilename());
            setAttribute(root, "id", String.valueOf(drawing.getId()));
            setAttribute(root, "mode", String.valueOf(drawing.getShapeMode()));
            setAttribute(root, "visible", String.valueOf(drawing.isVisible()));
            setAttribute(root, "r", String.valueOf(drawing.getSearchRadius()));
            setAttribute(root, "thickness", String.valueOf(drawing.getThickness()));
            setAttribute(root, "gradient", String.valueOf(drawing.isGradient()));

            //drawing location
            setAttribute(root, "x", String.valueOf(drawing.getLocation().x));
            setAttribute(root, "y", String.valueOf(drawing.getLocation().y));
            setAttribute(root, "width", String.valueOf(drawing.getWidth()));
            setAttribute(root, "height", String.valueOf(drawing.getHeight()));

            //colors
            saveColor(root, "color", drawing.getColor());
            saveColor(root, "fill", drawing.getFill());
            saveColor(root, "startColor", drawing.getStartColor());
            saveColor(root, "endColor", drawing.getEndColor());

            //gradient points
            setAttribute(root, "startx", String.valueOf(drawing.getStart().x));
            setAttribute(root, "starty", String.valueOf(drawing.getStart().y));
            setAttribute(root, "endx", String.valueOf(drawing.getEnd().x));
            setAttribute(root, "endy", String.valueOf(drawing.getEnd().y));

            //text and image
            setAttribute(root, "text", safeString(drawing.getText()));
            setAttribute(root, "image", safeString(drawing.getImageFilename()));

            //font
            Font font = drawing.getFont();
            if (font != null) {
                setAttribute(root, "font-family", font.getFamily());
                setAttribute(root, "font-style", String.valueOf(font.getStyle()));
                setAttribute(root, "font-size", String.valueOf(font.getSize()));
            } else {
                setAttribute(root, "font-family", "Arial");
                setAttribute(root, "font-style", "0");
                setAttribute(root, "font-size", "12");
            }

            //save all shapes
            for (Shape shape : drawing.getShapes()) {
                Element shapeElement = document.createElement("Shape");

                //shape type
                String shapeType = getShapeType(shape);
                setAttribute(shapeElement, "type", shapeType);

                //basic properties
                setAttribute(shapeElement, "id", String.valueOf(shape.getId()));
                // FIXED: Changed from boolean to String "false"
                setAttribute(shapeElement, "selected", "false");
                setAttribute(shapeElement, "visible", String.valueOf(shape.isVisible()));
                setAttribute(shapeElement, "r", String.valueOf(shape.getR()));
                setAttribute(shapeElement, "thickness", String.valueOf(shape.getThickness()));
                setAttribute(shapeElement, "gradient", String.valueOf(shape.isGradient()));
                setAttribute(shapeElement, "selection-mode", String.valueOf(shape.getSelectionMode()));

                //location and size
                setAttribute(shapeElement, "x", String.valueOf(shape.getLocation().x));
                setAttribute(shapeElement, "y", String.valueOf(shape.getLocation().y));
                setAttribute(shapeElement, "width", String.valueOf(shape.getWidth()));
                setAttribute(shapeElement, "height", String.valueOf(shape.getHeight()));

                //colors with alpha
                saveColor(shapeElement, "color", shape.getColor());
                saveColor(shapeElement, "fill", shape.getFill());
                saveColor(shapeElement, "start-color", shape.getStartColor());
                saveColor(shapeElement, "end-color", shape.getEndColor());

                //gradient points
                setAttribute(shapeElement, "startx", String.valueOf(shape.getStart().x));
                setAttribute(shapeElement, "starty", String.valueOf(shape.getStart().y));
                setAttribute(shapeElement, "endx", String.valueOf(shape.getEnd().x));
                setAttribute(shapeElement, "endy", String.valueOf(shape.getEnd().y));

                //text and image
                setAttribute(shapeElement, "text", safeString(shape.getText()));
                setAttribute(shapeElement, "image", safeString(shape.getImageFilename()));

                //font
                Font shapeFont = shape.getFont();
                if (shapeFont != null) {
                    setAttribute(shapeElement, "font-family", shapeFont.getFamily());
                    setAttribute(shapeElement, "font-style", String.valueOf(shapeFont.getStyle()));
                    setAttribute(shapeElement, "font-size", String.valueOf(shapeFont.getSize()));
                } else {
                    setAttribute(shapeElement, "font-family", "Arial");
                    setAttribute(shapeElement, "font-style", "0");
                    setAttribute(shapeElement, "font-size", "12");
                }

                root.appendChild(shapeElement);
            }

            //xml format
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            DOMSource source = new DOMSource(document);

            String filename = drawing.getFilename();
            if (filename == null || filename.isEmpty()) {
                filename = "drawing.xml";
            }
            if (!filename.toUpperCase().endsWith(".XML")) {
                filename = filename + ".xml";
            }

            StreamResult result = new StreamResult(new File(filename));
            transformer.transform(source, result);

            System.out.println("XML file saved successfully: " + filename);

        } catch (Exception e) {
            System.err.println("Error saving XML: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void open() {
        drawing.getShapes().clear();

        try {
            File xmlFile = new File(drawing.getFilename());
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlFile);
            document.getDocumentElement().normalize();

            //load properties
            Element root = document.getDocumentElement();
            NamedNodeMap map = root.getAttributes();

            //drawing properties
            drawing.setId(getIntAttribute(map, "id", 0));
            drawing.setVisible(getBoolAttribute(map, "visible", true));
            drawing.setSearchRadius(getIntAttribute(map, "r", 5));
            drawing.setThickness(getIntAttribute(map, "thickness", 1));
            drawing.setGradient(getBoolAttribute(map, "gradient", false));

            //location and size
            drawing.getLocation().x = getIntAttribute(map, "x", 0);
            drawing.getLocation().y = getIntAttribute(map, "y", 0);
            drawing.setWidth(getIntAttribute(map, "width", 0));
            drawing.setHeight(getIntAttribute(map, "height", 0));

            //colors
            drawing.setColor(loadColor(map, "color", Color.BLACK));
            drawing.setFill(loadColor(map, "fill", Color.WHITE));
            drawing.setStartColor(loadColor(map, "startColor", Color.WHITE));
            drawing.setEndColor(loadColor(map, "endColor", Color.BLACK));

            //gradient points
            drawing.getStart().x = getIntAttribute(map, "startx", 0);
            drawing.getStart().y = getIntAttribute(map, "starty", 0);
            drawing.getEnd().x = getIntAttribute(map, "endx", 100);
            drawing.getEnd().y = getIntAttribute(map, "endy", 0);

            //text and image
            drawing.setText(getStringAttribute(map, "text", "Default text"));
            drawing.setImageFilename(getStringAttribute(map, "image", null));

            //font
            String fontFamily = getStringAttribute(map, "font-family", "Arial");
            int fontStyle = getIntAttribute(map, "font-style", Font.PLAIN);
            int fontSize = getIntAttribute(map, "font-size", 12);
            drawing.setFont(new Font(fontFamily, fontStyle, fontSize));

            //mode
            String mode = getStringAttribute(map, "mode", "Select");
            drawing.setShapeMode(parseShapeMode(mode));

            //load all shapes
            NodeList nodeList = document.getElementsByTagName("Shape");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                map = node.getAttributes();

                //shape type and creation
                String type = getStringAttribute(map, "type", "Rectangle");
                int x = getIntAttribute(map, "x", 0);
                int y = getIntAttribute(map, "y", 0);
                int width = getIntAttribute(map, "width", 100);
                int height = getIntAttribute(map, "height", 100);

                Point location = new Point(x, y);
                Shape shape = createShape(type, location, width, height);

                if (shape == null) continue;

                //basic properties
                shape.setId(getIntAttribute(map, "id", 0));
                shape.setSelected(getBoolAttribute(map, "selected", false));
                shape.setVisible(getBoolAttribute(map, "visible", true));
                shape.setR(getIntAttribute(map, "r", 2));
                shape.setThickness(getIntAttribute(map, "thickness", 1));
                shape.setGradient(getBoolAttribute(map, "gradient", false));

                //select mode
                String selectionMode = getStringAttribute(map, "selection-mode", "None");
                shape.setSelectionMode(parseSelectionMode(selectionMode));

                //colors
                shape.setColor(loadColor(map, "color", Color.RED));
                shape.setFill(loadColor(map, "fill", null));
                shape.setStartColor(loadColor(map, "start-color", Color.WHITE));
                shape.setEndColor(loadColor(map, "end-color", Color.BLACK));

                //gradient points
                shape.getStart().x = getIntAttribute(map, "startx", 0);
                shape.getStart().y = getIntAttribute(map, "starty", 0);
                shape.getEnd().x = getIntAttribute(map, "endx", 100);
                shape.getEnd().y = getIntAttribute(map, "endy", 0);

                //text and image
                shape.setText(getStringAttribute(map, "text", null));
                shape.setImageFilename(getStringAttribute(map, "image", null));

                //font
                fontFamily = getStringAttribute(map, "font-family", "Arial");
                fontStyle = getIntAttribute(map, "font-style", Font.PLAIN);
                fontSize = getIntAttribute(map, "font-size", 12);
                shape.setFont(new Font(fontFamily, fontStyle, fontSize));

                drawing.getShapes().add(shape);
            }

            //remove selection mode when saved
            for (Shape shape : drawing.getShapes()) {
                shape.setSelected(false);
                shape.setSelectionMode(SelectionMode.None);
            }
            drawing.clearSelection();

            System.out.println("XML file loaded successfully: " + drawing.getFilename());
            System.out.println("Loaded " + drawing.getShapes().size() + " shapes");

        } catch (Exception e) {
            System.err.println("Error loading XML: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setAttribute(Element element, String name, String value) {
        Attr attr = element.getOwnerDocument().createAttribute(name);
        attr.setValue(value != null ? value : "");
        element.setAttributeNode(attr);
    }

    private void saveColor(Element element, String baseName, Color color) {
        if (color != null) {
            setAttribute(element, baseName, colorToString(color));
            setAttribute(element, baseName + "-alpha", String.valueOf(color.getAlpha()));
        } else {
            setAttribute(element, baseName, "null");
            setAttribute(element, baseName + "-alpha", "255");
        }
    }

    private Color loadColor(NamedNodeMap map, String baseName, Color defaultColor) {
        Node colorNode = map.getNamedItem(baseName);
        if (colorNode == null || "null".equals(colorNode.getNodeValue())) {
            return defaultColor;
        }

        Color color = parseColor(colorNode.getNodeValue());
        if (color == null) return defaultColor;

        Node alphaNode = map.getNamedItem(baseName + "-alpha");
        if (alphaNode != null) {
            try {
                int alpha = Integer.parseInt(alphaNode.getNodeValue());
                return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
            } catch (NumberFormatException e) {
                return color;
            }
        }
        return color;
    }

    private String colorToString(Color color) {
        return String.format("java.awt.Color[r=%d,g=%d,b=%d]",
                color.getRed(), color.getGreen(), color.getBlue());
    }

    private Color parseColor(String colorStr) {
        try {
            if (colorStr == null || colorStr.equals("null")) return null;

            int rIndex = colorStr.indexOf("r=");
            if (rIndex == -1) return null;

            colorStr = colorStr.substring(rIndex + 2);

            String[] parts = colorStr.split("[,\\]]");
            int r = Integer.parseInt(parts[0].trim());
            int g = Integer.parseInt(parts[1].substring(parts[1].indexOf('=') + 1).trim());
            int b = Integer.parseInt(parts[2].substring(parts[2].indexOf('=') + 1).trim());

            return new Color(r, g, b);
        } catch (Exception e) {
            System.err.println("Error parsing color: " + colorStr);
            return Color.BLACK;
        }
    }

    private String getShapeType(Shape shape) {
        if (shape instanceof Line) return "Line";
        if (shape instanceof Ellipse) return "Ellipse";
        if (shape instanceof Rectangle) return "Rectangle";
        if (shape instanceof Text) return "Text";
        if (shape instanceof Image) return "Image";
        return "Unknown";
    }

    private Shape createShape(String type, Point location, int width, int height) {
        switch (type) {
            case "Rectangle":
                return new Rectangle(location, width, height);
            case "Ellipse":
                return new Ellipse(location, width, height);
            case "Line":
                return new Line(location, width, height);
            case "Text":
                return new Text(location, width, height);
            case "Image":
                return new Image(location, width, height);
            default:
                System.err.println("Unknown shape type: " + type);
                return null;
        }
    }

    private ShapeMode parseShapeMode(String mode) {
        try {
            return ShapeMode.valueOf(mode);
        } catch (IllegalArgumentException e) {
            return ShapeMode.Select;
        }
    }

    private SelectionMode parseSelectionMode(String mode) {
        try {
            return SelectionMode.valueOf(mode);
        } catch (IllegalArgumentException e) {
            return SelectionMode.None;
        }
    }

    private String safeString(String value) {
        return value != null ? value : "";
    }

    private int getIntAttribute(NamedNodeMap map, String name, int defaultValue) {
        Node node = map.getNamedItem(name);
        if (node == null) return defaultValue;
        try {
            return Integer.parseInt(node.getNodeValue());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private boolean getBoolAttribute(NamedNodeMap map, String name, boolean defaultValue) {
        Node node = map.getNamedItem(name);
        if (node == null) return defaultValue;
        return Boolean.parseBoolean(node.getNodeValue());
    }

    private String getStringAttribute(NamedNodeMap map, String name, String defaultValue) {
        Node node = map.getNamedItem(name);
        if (node == null) return defaultValue;
        String value = node.getNodeValue();
        return (value == null || value.isEmpty() || value.equals("null")) ? defaultValue : value;
    }
}