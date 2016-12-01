package controllers;

//import com.interactivemesh.jfx.importer.Importer;
//import com.interactivemesh.jfx.importer.stl.StlMeshImporter;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;
import repository.Repository;
import view.PathDot;
import view.Point3D;
import view.PolyLine3D;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private Stage stage;

    private Group axisGroup;
    private Group mainGroup;
    private Group plainGroup;
    private Group cellsGroup;
    private Group lightGroup;
    private Group cameraGroup;

    private Sphere glider;

    private Repository repository = Repository.getInstance();
    private PerspectiveCamera camera;

    private DoubleProperty cameraRotationX = new SimpleDoubleProperty(0);
    private DoubleProperty cameraRotationY = new SimpleDoubleProperty(0);
    private DoubleProperty zoom = new SimpleDoubleProperty(1);
    private DoubleProperty cameraTranslateX = new SimpleDoubleProperty(0);
    private DoubleProperty cameraTranslateY = new SimpleDoubleProperty(0);

    private Timeline timeLine;

    private long trackLength = 0;
    private long trackStep = 0;

    private double mousePosX;
    private double mousePosY;
    //private double mousePosZ;
    private double mouseOldX;
    private double mouseOldY;
    private double mouseDeltaX;
    private double mouseDeltaY;
    private double CONTROL_MULTIPLIER = 1;
    private double SHIFT_MULTIPLIER = 1;
    private double ROTATION_SPEED = 0.5;
    private double TRACK_SPEED = 1;

    @FXML
    Pane subPane;

    @FXML
    CheckBox showProections;

    @FXML
    CheckBox showPath;

    @FXML
    Slider animationSlider;

    @FXML
    Label animationLabel;

    @FXML
    CheckBox dynamicPath;

    @FXML
    Label xLabel;

    @FXML
    Label yLabel;

    @FXML
    Label zLabel;

    @FXML
    Label speedLabel;

    @FXML
    Slider speedSlider;





    @FXML
    void playTimer() {
        if ((int) repository.currentPoint.get() == repository.dotsGroup.getChildren().size() - 1) {
            repository.currentPoint.set(0);
        }
        setTimeLine();
        timeLine.setRate(speedSlider.getValue());
        timeLine.play();
    }

    @FXML
    void pauseTimer() {
        timeLine.stop();
    }

    @FXML
    void stopTimer() {
        timeLine.stop();
        repository.currentPoint.setValue(0);

    }

    @FXML
    void loadModel() {
       /* FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(stage);
        System.out.println(file);
        Importer importer = new StlMeshImporter();
        importer.read(file);
        TriangleMesh node = (TriangleMesh) importer.getImport();
        MeshView meshView = new MeshView(node);
        mainGroup.getChildren().add(meshView);*/

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initPoints();

        initSlider();

        initGlider();


        repository.proectionGroup.visibleProperty().bind(showProections.selectedProperty());
        repository.dotsGroup.visibleProperty().bind(showPath.selectedProperty());

        showPath.setSelected(true);

        dynamicPath.selectedProperty().addListener(c->{
            if (dynamicPath.isSelected()){
                for (int i = repository.currentPoint.intValue()+1; i < repository.dotsGroup.getChildren().size(); i++) {
                    repository.dotsGroup.getChildren().get(i).setVisible(false);
                }
            } else {
                for (int i = repository.currentPoint.intValue()+1; i < repository.dotsGroup.getChildren().size(); i++) {
                    repository.dotsGroup.getChildren().get(i).setVisible(true);
                }
            }
        });

        //setProection();

        speedSlider.setMax(2);
        speedSlider.setValue(1);
        speedLabel.textProperty().bindBidirectional(speedSlider.valueProperty(), new StringConverter<Number>() {
            @Override
            public String toString(Number object) {
                return String.valueOf( Math.rint(100.0 * object.doubleValue()) / 100.0);
            }

            @Override
            public Number fromString(String string) {
                return null;
            }
        });

        axisGroup = initAxis();
        mainGroup = new Group();
        plainGroup = initPlane();
        cellsGroup = initCells();
        lightGroup = new Group();
        cameraGroup = new Group();


        Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
        Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);
        Rotate rotateZ = new Rotate(0, Rotate.Z_AXIS);
        Scale scale = new Scale(1, 1);

        /*rotateX.angleProperty().bind(xAxisSlider.valueProperty());
        rotateY.angleProperty().bind(yAxisSlider.valueProperty());
        rotateZ.angleProperty().bind(zAxisSlider.valueProperty());*/
        rotateX.angleProperty().bind(cameraRotationY);
        rotateY.angleProperty().bind(cameraRotationX);
        scale.xProperty().bind(zoom);
        scale.yProperty().bind(zoom);
        scale.zProperty().bind(zoom);

        camera = new PerspectiveCamera(false);// создание камеры
        cameraGroup.getChildren().add(camera);
        camera.translateXProperty().bind(cameraTranslateX);
        camera.translateYProperty().bind(cameraTranslateY);
        //camera.setTranslateX(-100);
        //camera.setTranslateY(0);
        camera.setTranslateZ(-600);
        //Rotate cameraRotateX = new Rotate(0, Rotate.X_AXIS);
        //Rotate cameraRotateY = new Rotate(0, Rotate.Y_AXIS);
        /*cameraRotateX.angleProperty().bind(cameraRotationX);
        cameraRotateY.angleProperty().bind(cameraRotationY);
        camera.getTransforms().addAll(cameraRotateX, cameraRotateY);*/

        repository.currentPoint.addListener(this::currentPointChangeListener);


        PointLight light = new PointLight();
        light.setLayoutX(200);
        light.setLayoutY(200);
        lightGroup.getChildren().add(light);

        cameraGroup.getTransforms().addAll(rotateX, rotateY, rotateZ, scale);


        mainGroup.getChildren().addAll(cameraGroup, lightGroup, glider, repository.proectionGroup, repository.dotsGroup, axisGroup, plainGroup, cellsGroup);

                SubScene subScene = new SubScene(mainGroup, 750, 600, true, SceneAntialiasing.BALANCED);
        subScene.setCamera(camera);

        handleMouse(subScene);


        subScene.widthProperty().bind(subPane.widthProperty());
        subScene.heightProperty().bind(subPane.heightProperty());


        subPane.getChildren().add(subScene);

    }

    private void currentPointChangeListener(ObservableValue<? extends Number> observable, Number oldValue, Number newValue){
        cameraGroup.setTranslateX(((PathDot)repository.dotsGroup.getChildren().get(repository.currentPoint.intValue())).getPoint().x);

        xLabel.setText(String.valueOf(((PathDot)repository.dotsGroup.getChildren().get(repository.currentPoint.intValue())).getPoint().x));
        yLabel.setText(String.valueOf(((PathDot)repository.dotsGroup.getChildren().get(repository.currentPoint.intValue())).getPoint().y));
        zLabel.setText(String.valueOf(((PathDot)repository.dotsGroup.getChildren().get(repository.currentPoint.intValue())).getPoint().z));
    }

    private void initGlider() {
        glider = new Sphere(30);
        setGliderPosition(((PathDot) repository.dotsGroup.getChildren().get(0)).getPoint());
        repository.currentPoint.addListener((observable, oldValue, newValue) -> {
            Point3D point = ((PathDot) repository.dotsGroup.getChildren().get(repository.currentPoint.intValue())).getPoint();
            setGliderPosition(point);
        });

    }

    private void initSlider() {
        animationSlider.setMax(repository.dotsGroup.getChildren().size() - 1);
        animationSlider.setShowTickLabels(true);
        animationLabel.textProperty().bindBidirectional(animationSlider.valueProperty(), new StringConverter<Number>() {
            @Override
            public String toString(Number object) {
                return String.valueOf((Math.round((Double) object)));
            }

            @Override
            public Number fromString(String string) {
                return null;
            }
        });
        animationSlider.valueProperty().bindBidirectional(repository.currentPoint);
    }

    private void setGliderPosition(Point3D point) {
        glider.setTranslateX(point.x);
        glider.setTranslateY(point.y);
        glider.setTranslateZ(point.z);
    }

    private Group initAxis() {
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

    private Group initPlane() {
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

    private Group initCells() {
        Group group = new Group();
        Group xGroup = new Group();
        Group yGroup = new Group();
        Group zGroup = new Group();

        //create x-axis lines
        for (int i = 0; i < repository.getAxisSize() * repository.getxAxisScale(); i = i + 50) {
            Line line = new Line(i, 0, i, repository.getAxisSize() * repository.getyAxisScale());
            Text textX = new Text(String.valueOf((float) i / 100));
            textX.setTranslateX(i);
            textX.setTranslateY(20);
            textX.setFont(new Font(20));
            Line rotateLine = new Line(i, 0, i, repository.getAxisSize() * repository.getzAxisScale());
            Rotate rotate = new Rotate(-90, Rotate.X_AXIS);
            rotateLine.getTransforms().addAll(rotate);
            xGroup.getChildren().addAll(line, rotateLine, textX);
        }
        //create y-axis lines
        for (int i = 0; i < repository.getAxisSize() * repository.getyAxisScale(); i = i + 50) {
            Line line = new Line(0, i, repository.getAxisSize() * repository.getxAxisScale(), i);
            Line rotateLine = new Line(0, i, repository.getAxisSize() * repository.getzAxisScale(), i);
            Rotate rotate = new Rotate(90, Rotate.Y_AXIS);
            rotateLine.getTransforms().addAll(rotate);
            Text textY = new Text(String.valueOf((float) i / 100));
            textY.setTranslateY(i);
            textY.setTranslateX(10);
            textY.setFont(new Font(20));
            yGroup.getChildren().addAll(line, rotateLine, textY);
        }

        //create z-axis lines
        for (int i = 0; i < repository.getAxisSize() * repository.getzAxisScale(); i = i + 50) {
            Line line = new Line(0, 0, 0, repository.getAxisSize() * repository.getyAxisScale());
            line.setTranslateZ(-i);
            Line rotateLine = new Line(0, 0, 0, repository.getAxisSize() * repository.getxAxisScale());
            rotateLine.setTranslateZ(-i);
            Rotate rotate = new Rotate(-90, Rotate.Z_AXIS);
            rotateLine.getTransforms().addAll(rotate);
            Text textZ = new Text(String.valueOf((float) i / 100));
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
        scene.setOnScroll(e -> {
            if (e.getDeltaY() > 0) {
                zoom.setValue(zoom.getValue() + 0.1);
            } else {
                zoom.setValue(zoom.getValue() - 0.1);

            }
        });

        scene.setOnMousePressed(me -> {
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            mouseOldX = me.getSceneX();
            mouseOldY = me.getSceneY();
        });
        scene.setOnMouseDragged(me -> {


            if (!me.getTarget().equals(glider)) {

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
                    cameraRotationX.setValue(cameraRotationX.getValue() + mouseDeltaX * ROTATION_SPEED);
                    cameraRotationY.setValue(cameraRotationY.getValue() - mouseDeltaY * ROTATION_SPEED);
                /*cameraXform.ry.setAngle(cameraXform.ry.getAngle() -
                        mouseDeltaX*modifierFactor*modifier*ROTATION_SPEED);  //
                cameraXform.rx.setAngle(cameraXform.rx.getAngle() +
                        mouseDeltaY*modifierFactor*modifier*ROTATION_SPEED);  // -*/
                }
            /*else if (me.isSecondaryButtonDown()) {
                double z = camera.getTranslateZ();
                double newZ = z + mouseDeltaX*MOUSE_SPEED*modifier;
                camera.setTranslateZ(newZ);
            }*/
                else if (me.isSecondaryButtonDown()) {
                    cameraTranslateX.setValue(cameraTranslateX.getValue() - mouseDeltaX * modifier * TRACK_SPEED);
                    cameraTranslateY.setValue(cameraTranslateY.getValue() - mouseDeltaY * modifier * TRACK_SPEED);
                /*cameraXform2.t.setX(cameraXform2.t.getX() +
                        mouseDeltaX*MOUSE_SPEED*modifier*TRACK_SPEED);  // -
                cameraXform2.t.setY(cameraXform2.t.getY() +
                        mouseDeltaY*MOUSE_SPEED*modifier*TRACK_SPEED);  // -*/
                }
            } else {

                mousePosX = me.getSceneX();
                mousePosY = me.getSceneY();
                mouseDeltaX = (mousePosX - mouseOldX);
                mouseDeltaY = (mousePosY - mouseOldY);
                //double deltaPosX =
                //System.out.println(mouseDeltaX);
                if (Math.abs(mouseDeltaX * 10 / zoom.get()) > trackStep) {
                    if (mouseDeltaX > 0) repository.currentPoint.setValue(repository.currentPoint.get() + 1);
                    else if (mouseDeltaX < 0) repository.currentPoint.setValue(repository.currentPoint.get() - 1);
                    mouseOldX = mousePosX;
                }

            }
        }); // setOnMouseDragged
    } //handleMouse

    private void initPoints() {

        for (int i = 0; i < 100; i++) {
            /*if (10 * i > repository.getxAxisScale() * repository.getAxisSize())
                repository.setxAxisScale(repository.getxAxisScale() + 0.5f);*/

            PathDot dot = new PathDot(new view.Point3D(10 * i, (float) (100 * Math.sin(i * (2 * Math.PI / 100)) * 2) + 500, -3 * i), true, 5);
            if (dynamicPath.isSelected()) dot.setVisible(false);

            repository.dotsGroup.getChildren().add(dot);
        }

        repository.dotsGroup.getChildren().addListener((ListChangeListener<Node>) c -> {

        });

        //Group projectionGroup = setProection();

        repository.proectionGroup = setProection();
        setTrackLength();

        repository.currentPoint.addListener((observable, oldValue, newValue) -> {
            if (dynamicPath.isSelected()){
                int diff = newValue.intValue() - oldValue.intValue();
                int dir;
                dir = (diff > 0) ? 1 :-1;
                if (Math.abs(diff) > 1) {
                    System.out.println(diff);
                    for (int i = 0; i < Math.abs(diff); i++) {
                        int val = oldValue.intValue() + dir + i*dir;
                        Node point = repository.dotsGroup.getChildren().get(val);
                        if (point.isVisible()) point.setVisible(false);
                        else point.setVisible(true);
                    }
                } else {
                    Node point = repository.dotsGroup.getChildren().get(newValue.intValue());
                    if (point.isVisible()) point.setVisible(false);
                    else point.setVisible(true);
                }
            }


        });


    }

    private void setTrackLength() {
        for (int i = 1; i < repository.dotsGroup.getChildren().size() - 1; i++) {
            PathDot dot1 = (PathDot) repository.dotsGroup.getChildren().get(i - 1);
            PathDot dot2 = (PathDot) repository.dotsGroup.getChildren().get(i);
            trackLength += Math.sqrt((dot2.getPoint().x * dot2.getPoint().x - dot1.getPoint().x * dot1.getPoint().x) +
                    (dot2.getPoint().y * dot2.getPoint().y - dot1.getPoint().y * dot1.getPoint().y) +
                    (dot2.getPoint().z * dot2.getPoint().z - dot1.getPoint().z * dot1.getPoint().z));
        }
        trackStep = trackLength / repository.dotsGroup.getChildren().size();
        //System.out.println("length " + trackLength);
    }

    private Group setProection() {
        Group group = new Group();
        List<Point3D> listX = new ArrayList<>();
        List<Point3D> listY = new ArrayList<>();
        List<Point3D> listZ = new ArrayList<>();

        for (int i = 0; i < repository.dotsGroup.getChildren().size(); i++) {
            Point3D point = ((PathDot) repository.dotsGroup.getChildren().get(i)).getPoint();
            Point3D pointX = new Point3D(point.x, point.y, 0);
            Point3D pointY = new Point3D(0, point.y, point.z);
            Point3D pointZ = new Point3D(point.x, 0, point.z);
            listX.add(pointX);
            listY.add(pointY);
            listZ.add(pointZ);
        }
        PolyLine3D lineX = new PolyLine3D(listX, 5, Color.RED, PolyLine3D.LineType.TRIANGLE);
        PolyLine3D lineY = new PolyLine3D(listY, 5, Color.BLUE, PolyLine3D.LineType.RIBBON);
        PolyLine3D lineZ = new PolyLine3D(listZ, 5, Color.GREEN, PolyLine3D.LineType.RIBBON);
        group.getChildren().addAll(lineX, lineY, lineZ);
        return group;
    }

    private void setTimeLine() {
        timeLine = new Timeline(new KeyFrame(Duration.millis(100), ae -> {
            repository.currentPoint.set(repository.currentPoint.get() + 1);
            if (((PathDot)repository.dotsGroup.getChildren().get(repository.currentPoint.intValue())).getPoint().x>repository.getxAxisScale() * repository.getAxisSize()) {
                repository.setxAxisScale(repository.getxAxisScale() + 0.5f);
                cellsGroup.getChildren().clear();
                cellsGroup.getChildren().add(initCells());
            }

            //System.out.println(repository.currentPoint.get());
        }));
        timeLine.setCycleCount((int) (repository.dotsGroup.getChildren().size() - 1 - repository.currentPoint.get()));

        speedSlider.valueProperty().addListener(c->{
            timeLine.setRate(speedSlider.getValue());
        });
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
