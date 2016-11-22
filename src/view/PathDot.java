package view;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

/**
 * Created by Oceanos on 14.11.2016.
 */
public class PathDot extends Sphere{
    Point3D point;
    BooleanProperty showing = new SimpleBooleanProperty(false);
    PhongMaterial material;


    public PathDot() {
        super();
    }

    public PathDot(Point3D point, boolean showing, double radius) {
        super(radius);
        this.point = point;
        this.showing.setValue(showing);
        setTranslateY(point.y);
        setTranslateZ(point.z);
        setTranslateX(point.x);
        setVisible(showing);
        material = new PhongMaterial(Color.web("rgba(0, 102, 255, 0.3)"));
        setMaterial(material);
    }

    public Point3D getPoint() {
        return point;
    }

    public void setPoint(Point3D point) {
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
