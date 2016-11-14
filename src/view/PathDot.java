package view;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Shape;
import javafx.scene.shape.Sphere;
import model.MyPoint;

/**
 * Created by Oceanos on 14.11.2016.
 */
public class PathDot extends Sphere{
    MyPoint point;
    BooleanProperty showing = new SimpleBooleanProperty(false);
    PhongMaterial material;


    public PathDot() {
        super();
    }

    public PathDot(MyPoint point, boolean showing, double radius) {
        super(radius);
        this.point = point;
        this.showing.setValue(showing);
        setTranslateY(point.getY());
        setTranslateZ(point.getZ());
        setTranslateX(point.getX());
        setVisible(showing);
        material = new PhongMaterial(Color.web("rgba(0, 102, 255, 0.3)"));
        setMaterial(material);
    }

    public MyPoint getPoint() {
        return point;
    }

    public void setPoint(MyPoint point) {
        this.point = point;
    }

    public boolean isShowing() {
        return showing.get();
    }

    public BooleanProperty showingProperty() {
        return showing;
    }

    public void setShowing(boolean showing) {
        this.showing.set(showing);
    }
}
