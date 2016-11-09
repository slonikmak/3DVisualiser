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
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import repository.Repository;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable{
    Group axisGroup;
    Group mainGroup;
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
        camera.setTranslateX(-400);
        camera.setTranslateY(-300);
        camera.setTranslateZ(-400);

        Rectangle xRect = new Rectangle(100, 100, Color.GRAY);
        xRect.setOpacity(0.2);
        xRect.widthProperty().bind(repository.xAxisScaleProperty().multiply(repository.getAxisSize()));
        xRect.heightProperty().bind(repository.yAxisScaleProperty().multiply(repository.getAxisSize()));

        mainGroup.getTransforms().addAll(rotateX, rotateY, rotateZ);

        mainGroup.getChildren().addAll(axisGroup, xRect);

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
}
