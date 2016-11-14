package controllers;

import com.sun.prism.shader.DrawCircle_LinearGradient_PAD_AlphaTest_Loader;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import model.MyPoint;
import repository.Repository;
import utills.Utills;
import view.PathDot;

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
    PerspectiveCamera camera;

    DoubleProperty cameraRotationX = new SimpleDoubleProperty(0);
    DoubleProperty cameraRotationY = new SimpleDoubleProperty(0);

    double mousePosX;
    double mousePosY;
    double mousePosZ;
    double mouseOldX;
    double mouseOldY;
    double mouseDeltaX;
    double mouseDeltaY;
    double CONTROL_MULTIPLIER = 1;
    double SHIFT_MULTIPLIER = 1;
    double ROTATION_SPEED = 1;
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
        initPoints();


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

        camera = new PerspectiveCamera(false);// создание камеры
        camera.setTranslateX(-100);
        camera.setTranslateY(0);
        camera.setTranslateZ(-600);
        Rotate cameraRotateX = new Rotate(0, Rotate.X_AXIS);
        Rotate cameraRotateY = new Rotate(0, Rotate.Y_AXIS);
        cameraRotateX.angleProperty().bind(cameraRotationX);
        cameraRotateY.angleProperty().bind(cameraRotationY);
        camera.getTransforms().addAll(cameraRotateX, cameraRotateY);




        mainGroup.getTransforms().addAll(rotateX, rotateY, rotateZ);

        mainGroup.getChildren().addAll(repository.dotsGroup, axisGroup, plainGroup, cellsGroup);

        SubScene subScene = new SubScene(mainGroup, 750, 600, true, SceneAntialiasing.BALANCED);
        subScene.setCamera(camera);
        handleMouse(subScene);


        subPane.getChildren().add(subScene);

    }

    private Group initAxis(){
        Group group = new Group();

        Cylinder xAxis = new Cylinder(5, repository.getAxisSize());

        xAxis.setRotate(90);
        xAxis.translateXProperty().bind(repository.xAxisScaleProperty().multiply(repository.getAxisSize()).divide(2));
        xAxis.heightProperty().bind(repository.xAxisScaleProperty().multiply(repository.getAxisSize()));
        PhongMaterial xAxisMat = new PhongMaterial(Color.RED);
        xAxis.setMaterial(xAxisMat);

        Cylinder yAxis = new Cylinder(5, repository.getAxisSize());
        yAxis.translateYProperty().bind(repository.yAxisScaleProperty().multiply(repository.getAxisSize()).divide(2));
        yAxis.heightProperty().bind(repository.yAxisScaleProperty().multiply(repository.getAxisSize()));
        PhongMaterial yAxisMat = new PhongMaterial(Color.BLUE);
        yAxis.setMaterial(yAxisMat);

        Cylinder zAxis = new Cylinder(5, repository.getAxisSize());
        zAxis.translateZProperty().bind(repository.zAxisScaleProperty().multiply(repository.getAxisSize()).divide(-2));
        zAxis.heightProperty().bind(repository.zAxisScaleProperty().multiply(repository.getAxisSize()));
        PhongMaterial zAxisMat = new PhongMaterial(Color.GREEN);
        Rotate zAxisRotate = new Rotate(90, Rotate.X_AXIS);
        zAxis.getTransforms().add(zAxisRotate);
        zAxis.setMaterial(zAxisMat);

        group.getChildren().addAll(xAxis, yAxis, zAxis);
        return group;
    }

    private Group initPlane(){
        Group group = new Group();

        Rectangle xRect = new Rectangle(100, 100, Color.web("rgba(255, 51, 0, 0.2)"));
        //xRect.setOpacity(0.2);
        xRect.widthProperty().bind(repository.xAxisScaleProperty().multiply(repository.getAxisSize()));
        xRect.heightProperty().bind(repository.yAxisScaleProperty().multiply(repository.getAxisSize()));



        Rectangle yRect = new Rectangle(100, 100, Color.web("rgba(0, 102, 255, 0.3)"));
        Rotate yRotate = new Rotate(90, Rotate.Y_AXIS);
        yRect.getTransforms().add(yRotate);
        //yRect.setOpacity(0.2);
        yRect.widthProperty().bind(repository.zAxisScaleProperty().multiply(repository.getAxisSize()));
        yRect.heightProperty().bind(repository.yAxisScaleProperty().multiply(repository.getAxisSize()));

        Rectangle zRect = new Rectangle(100, 100, Color.web("rgba(0, 204, 0, 0.3)"));
        Rotate zRotate = new Rotate(-90, Rotate.X_AXIS);
        zRect.getTransforms().add(zRotate);
        //zRect.setOpacity(0.2);
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
            Line line = new Line(i, 0, i, repository.getAxisSize()*repository.getyAxisScale());
            Text textX = new Text(String.valueOf((float)i/100));
            textX.setTranslateX(i);
            textX.setTranslateY(20);
            textX.setFont(new Font(20));
            Line rotateLine = new Line(i, 0, i, repository.getAxisSize()*repository.getzAxisScale());
            Rotate rotate = new Rotate(-90, Rotate.X_AXIS);
            rotateLine.getTransforms().addAll(rotate);
            xGroup.getChildren().addAll(line, rotateLine, textX);
        }
        //create y-axis lines
        for (int i = 0; i < repository.getAxisSize()*repository.getyAxisScale(); i=i+50) {
            Line line = new Line(0, i,repository.getAxisSize()*repository.getxAxisScale(), i);
            Line rotateLine = new Line(0, i,repository.getAxisSize()*repository.getzAxisScale(), i);
            Rotate rotate = new Rotate(90, Rotate.Y_AXIS);
            rotateLine.getTransforms().addAll(rotate);
            Text textY = new Text(String.valueOf((float)i/100));
            textY.setTranslateY(i);
            textY.setTranslateX(10);
            textY.setFont(new Font(20));
            yGroup.getChildren().addAll(line, rotateLine, textY);
        }

        //create z-axis lines
        for (int i = 0; i < repository.getAxisSize()*repository.getzAxisScale(); i=i+50) {
            Line line = new Line(0, 0,0,repository.getAxisSize()*repository.getyAxisScale());
            line.setTranslateZ(-i);
            Line rotateLine = new Line(0, 0,0,repository.getAxisSize()*repository.getxAxisScale());
            rotateLine.setTranslateZ(-i);
            Rotate rotate = new Rotate(-90, Rotate.Z_AXIS);
            rotateLine.getTransforms().addAll(rotate);
            Text textZ = new Text(String.valueOf((float)i/100));
            textZ.setTranslateZ(-i);
            textZ.setTranslateX(10);
            textZ.setTranslateY(15);
            Rotate textRotate = new Rotate(-90, Rotate.X_AXIS);
            textZ.getTransforms().add(textRotate);
            textZ.setFont(new Font(20));
            zGroup.getChildren().addAll(line, rotateLine, textZ);
        }

        group.getChildren().addAll(xGroup, yGroup, zGroup);
        return group;
    }

    private void handleMouse(SubScene scene) {

        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent me) {
                mousePosX = me.getSceneX();
                mousePosY = me.getSceneY();
                mouseOldX = me.getSceneX();
                mouseOldY = me.getSceneY();
            }
        });
        scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent me) {
                mouseOldX = mousePosX;
                mouseOldY = mousePosY;
                mousePosX = me.getSceneX();
                mousePosY = me.getSceneY();
                mouseDeltaX = (mousePosX - mouseOldX);
                mouseDeltaY = (mousePosY - mouseOldY);

                double modifier = 1.0;

                if (me.isControlDown()) {
                    modifier = CONTROL_MULTIPLIER;
                }
                if (me.isShiftDown()) {
                    modifier = SHIFT_MULTIPLIER;
                }
                if (me.isPrimaryButtonDown()) {
                    cameraRotationX.setValue(cameraRotationX.getValue() + mouseDeltaX*ROTATION_SPEED);
                    cameraRotationY.setValue(cameraRotationY.getValue() - mouseDeltaY*ROTATION_SPEED);
                    /*cameraXform.ry.setAngle(cameraXform.ry.getAngle() -
                            mouseDeltaX*modifierFactor*modifier*ROTATION_SPEED);  //
                    cameraXform.rx.setAngle(cameraXform.rx.getAngle() +
                            mouseDeltaY*modifierFactor*modifier*ROTATION_SPEED);  // -*/
                }
                /*else if (me.isSecondaryButtonDown()) {
                    double z = camera.getTranslateZ();
                    double newZ = z + mouseDeltaX*MOUSE_SPEED*modifier;
                    camera.setTranslateZ(newZ);
                }
                else if (me.isMiddleButtonDown()) {
                    cameraXform2.t.setX(cameraXform2.t.getX() +
                            mouseDeltaX*MOUSE_SPEED*modifier*TRACK_SPEED);  // -
                    cameraXform2.t.setY(cameraXform2.t.getY() +
                            mouseDeltaY*MOUSE_SPEED*modifier*TRACK_SPEED);  // -
                }*/
            }
        }); // setOnMouseDragged
    } //handleMouse

    public void initPoints(){

        for (int i = 0; i < 100; i++) {

            repository.dotsGroup.getChildren().add(new PathDot(new MyPoint(10*i, 100*Math.sin(i*(2*Math.PI/100))*2, -3*i), true, 5));
        }

    }
}
