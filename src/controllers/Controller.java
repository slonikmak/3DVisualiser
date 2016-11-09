package controllers;

import com.sun.prism.shader.DrawCircle_LinearGradient_PAD_AlphaTest_Loader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import repository.Repository;
import utills.Utills;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class Controller implements Initializable{
    Group axisGroup;
    Group mainGroup;
    Group plainGroup;
    Group cellsGroup;
    Repository repository = Repository.getInstance();

    @FXML
    Pane subPane;

    @FXML
    private Slider xAxisSlider;

    @FXML
    private Slider yAxisSlider;

    @FXML
    private Slider zAxisSlider;

    @FXML
    private Slider zoomSlider;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        axisGroup = initAxis();
        mainGroup = new Group();
        plainGroup = initPlane();
        cellsGroup = initCells();

        xAxisSlider.setMax(180);
        yAxisSlider.setMax(180);
        zAxisSlider.setMax(180);

        Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
        Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);
        Rotate rotateZ = new Rotate(0, Rotate.Z_AXIS);

        rotateX.angleProperty().bind(xAxisSlider.valueProperty());
        rotateY.angleProperty().bind(yAxisSlider.valueProperty());
        rotateZ.angleProperty().bind(zAxisSlider.valueProperty());

        PerspectiveCamera camera = new PerspectiveCamera(false);// создание камеры
        camera.setTranslateX(-100);
        camera.setTranslateY(0);
        camera.setTranslateZ(-600);



        mainGroup.getTransforms().addAll(rotateX, rotateY, rotateZ);

        mainGroup.getChildren().addAll(axisGroup, plainGroup, cellsGroup);

        SubScene subScene = new SubScene(mainGroup, 750, 600, true, SceneAntialiasing.BALANCED);
        subScene.setCamera(camera);


        subPane.getChildren().add(subScene);

    }

    private Group initAxis(){
        Group group = new Group();

        Cylinder xAxis = new Cylinder(5, repository.getAxisSize());
        xAxis.setRotate(90);
        xAxis.translateXProperty().bind(repository.xAxisScaleProperty().multiply(repository.getAxisSize()).divide(2));
        PhongMaterial xAxisMat = new PhongMaterial(Color.RED);
        xAxis.setMaterial(xAxisMat);

        Cylinder yAxis = new Cylinder(5, repository.getAxisSize());
        yAxis.translateYProperty().bind(repository.yAxisScaleProperty().multiply(repository.getAxisSize()).divide(2));
        PhongMaterial yAxisMat = new PhongMaterial(Color.BLUE);
        yAxis.setMaterial(yAxisMat);

        Cylinder zAxis = new Cylinder(5, repository.getAxisSize());
        zAxis.translateZProperty().bind(repository.zAxisScaleProperty().multiply(repository.getAxisSize()).divide(-2));
        PhongMaterial zAxisMat = new PhongMaterial(Color.GREEN);
        Rotate zAxisRotate = new Rotate(90, Rotate.X_AXIS);
        zAxis.getTransforms().add(zAxisRotate);
        zAxis.setMaterial(zAxisMat);

        group.getChildren().addAll(xAxis, yAxis, zAxis);
        return group;
    }

    private Group initPlane(){
        Group group = new Group();

        Rectangle xRect = new Rectangle(100, 100, Color.RED);
        xRect.setOpacity(0.2);
        xRect.widthProperty().bind(repository.xAxisScaleProperty().multiply(repository.getAxisSize()));
        xRect.heightProperty().bind(repository.yAxisScaleProperty().multiply(repository.getAxisSize()));



        Rectangle yRect = new Rectangle(100, 100, Color.BLUE);
        Rotate yRotate = new Rotate(90, Rotate.Y_AXIS);
        yRect.getTransforms().add(yRotate);
        yRect.setOpacity(0.2);
        yRect.widthProperty().bind(repository.zAxisScaleProperty().multiply(repository.getAxisSize()));
        yRect.heightProperty().bind(repository.yAxisScaleProperty().multiply(repository.getAxisSize()));

        Rectangle zRect = new Rectangle(100, 100, Color.GREEN);
        Rotate zRotate = new Rotate(-90, Rotate.X_AXIS);
        zRect.getTransforms().add(zRotate);
        zRect.setOpacity(0.2);
        zRect.widthProperty().bind(repository.xAxisScaleProperty().multiply(repository.getAxisSize()));
        zRect.heightProperty().bind(repository.zAxisScaleProperty().multiply(repository.getAxisSize()));

        group.getChildren().addAll(xRect, yRect, zRect);

        return group;

    }

    public Group initCells(){
        Group group = new Group();
        Group xGroup = new Group();
        Group yGroup = new Group();
        Group zGroup = new Group();

        //create x-axis lines
        for (int i = 0; i < repository.getAxisSize()*repository.getxAxisScale(); i=i+50) {
            Line line = new Line(i, 0, i, repository.getAxisSize()*repository.getxAxisScale());
            Line rotateLine = new Line(i, 0, i, repository.getAxisSize()*repository.getxAxisScale());
            Rotate rotate = new Rotate(-90, Rotate.X_AXIS);
            rotateLine.getTransforms().addAll(rotate);
            xGroup.getChildren().addAll(line, rotateLine);
        }
        //create y-axis lines
        for (int i = 0; i < repository.getAxisSize()*repository.getyAxisScale(); i=i+50) {
            Line line = new Line(0, i,repository.getAxisSize()*repository.getyAxisScale(), i);
            Line rotateLine = new Line(0, i,repository.getAxisSize()*repository.getyAxisScale(), i);
            Rotate rotate = new Rotate(90, Rotate.Y_AXIS);
            rotateLine.getTransforms().addAll(rotate);
            yGroup.getChildren().addAll(line, rotateLine);
        }

        //create z-axis lines
        for (int i = 0; i < repository.getAxisSize()*repository.getzAxisScale(); i=i+50) {
            Line line = new Line(0, 0,0,repository.getAxisSize()*repository.getzAxisScale());
            line.setTranslateZ(-i);
            Line rotateLine = new Line(0, 0,0,repository.getAxisSize()*repository.getzAxisScale());
            rotateLine.setTranslateZ(-i);
            Rotate rotate = new Rotate(-90, Rotate.Z_AXIS);
            rotateLine.getTransforms().addAll(rotate);
            zGroup.getChildren().addAll(line, rotateLine);
        }

        group.getChildren().addAll(xGroup, yGroup, zGroup);
        return group;
    }
}
