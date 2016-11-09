package repository;


import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Created by Anton on 08.11.2016.
 */
public class Repository {
    private static Repository instance = new Repository();
    //масштаб 1 пиксель на сантиметр
    private final int SCALE = 1;
    private final int axisSize = 500;
    private IntegerProperty xAxisScale = new SimpleIntegerProperty(1);
    private IntegerProperty yAxisScale = new SimpleIntegerProperty(1);
    private IntegerProperty zAxisScale = new SimpleIntegerProperty(1);




    private Repository(){

    }

    public static Repository getInstance(){
        return instance;
    }

    public int getSCALE(){
        return SCALE;
    }
    public int getAxisSize(){
        return axisSize;
    }

    public int getxAxisScale() {
        return xAxisScale.get();
    }

    public IntegerProperty xAxisScaleProperty() {
        return xAxisScale;
    }

    public void setxAxisScale(int xAxisScale) {
        this.xAxisScale.set(xAxisScale);
    }

    public int getyAxisScale() {
        return yAxisScale.get();
    }

    public IntegerProperty yAxisScaleProperty() {
        return yAxisScale;
    }

    public void setyAxisScale(int yAxisScale) {
        this.yAxisScale.set(yAxisScale);
    }

    public int getzAxisScale() {
        return zAxisScale.get();
    }

    public IntegerProperty zAxisScaleProperty() {
        return zAxisScale;
    }

    public void setzAxisScale(int zAxisScale) {
        this.zAxisScale.set(zAxisScale);
    }
}
