package repository;


import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import view.PathDot;

/**
 * Created by Anton on 08.11.2016.
 */
public class Repository {
    public Group dotsGroup = new Group();
    public Group proectionGroup = new Group();
    public LongProperty currentPoint = new SimpleLongProperty(0);

    private static Repository instance = new Repository();
    //масштаб 1 пиксель на сантиметр
    private final float SCALE = 1;
    private final int axisSize = 500;
    private FloatProperty xAxisScale = new SimpleFloatProperty(1.5f);
    private FloatProperty yAxisScale = new SimpleFloatProperty(1.7f);
    private FloatProperty zAxisScale = new SimpleFloatProperty(1);




    private Repository(){

    }

    public static Repository getInstance(){
        return instance;
    }

    public float getSCALE(){
        return SCALE;
    }
    public int getAxisSize(){
        return axisSize;
    }

    public float getxAxisScale() {
        return xAxisScale.get();
    }

    public FloatProperty xAxisScaleProperty() {
        return xAxisScale;
    }

    public void setxAxisScale(float xAxisScale) {
        this.xAxisScale.set(xAxisScale);
    }

    public float getyAxisScale() {
        return yAxisScale.get();
    }

    public FloatProperty yAxisScaleProperty() {
        return yAxisScale;
    }

    public void setyAxisScale(int yAxisScale) {
        this.yAxisScale.set(yAxisScale);
    }

    public float getzAxisScale() {
        return zAxisScale.get();
    }

    public FloatProperty zAxisScaleProperty() {
        return zAxisScale;
    }

    public void setzAxisScale(float zAxisScale) {
        this.zAxisScale.set(zAxisScale);
    }
}
