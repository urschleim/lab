package moleculesampleapp;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Spinner;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.stage.Stage;


public class SphereAndBox extends Application
{
    public static void main( String[] args )
    {
        System.setProperty("prism.dirtyopts", "false");
        Application.launch(SphereAndBox.class, args);
    }

    private double anchorX;
    private double anchorY;
    private double anchorAngle;

    private PerspectiveCamera addCamera(SubScene scene)
    {
        var perspectiveCamera = new PerspectiveCamera(false);
        scene.setCamera(perspectiveCamera);
        return perspectiveCamera;
    }

    private Spinner<Double> makeSpinner()
    {
        var objectProp = new SimpleObjectProperty<>(0.0d);
        var dProperty = DoubleProperty.doubleProperty(objectProp);
        _doublep.bind( dProperty );

        var spinner = new Spinner<Double>(0, 360, 0);

        _doublep.bind( spinner.getValueFactory().valueProperty() );
        return null;
    }

    private SubScene subscene(ToolBar toolbar  )
    {
        var boxMaterial = new PhongMaterial();
        boxMaterial.setDiffuseColor(Color.GREEN);
        boxMaterial.setSpecularColor(Color.WHITESMOKE);

        var box = new Box(400, 400, 400);
        box.setMaterial(boxMaterial);

        box.setTranslateX(250);
        box.setTranslateY(250);
        box.setTranslateZ(4500);

        var parent = new Group(box);
        parent.setTranslateZ(500);
        parent.setRotationAxis(new Point3D(1, 1, 1));

        var root = new Group(parent);

        var result = new SubScene(root, 500, 500, true, SceneAntialiasing.DISABLED);

        result.setOnMousePressed( event -> {
            anchorX = event.getSceneX();
            anchorY = event.getSceneY();
            anchorAngle = parent.getRotate();
        });

        result.setOnMouseDragged( event -> {
            parent.setRotate(anchorAngle + anchorX - event.getSceneX());
        });

        parent.rotateProperty().bind( _doublep );

        var pointLight = new PointLight(Color.ANTIQUEWHITE);
        pointLight.setTranslateX(15);
        pointLight.setTranslateY(-10);
        pointLight.setTranslateZ(-100);

        root.getChildren().add(pointLight);

        addCamera(result);

        return result;
    }

    private final SimpleDoubleProperty _doublep =
            new SimpleDoubleProperty( this, "intp", 0 );

    @Override
    public void start( Stage primaryStage )
    {
        primaryStage.setTitle("SphereAndBox");

        ToolBar toolbar =
                new ToolBar();
        var subscene =
                subscene(toolbar);
        var layout = new BorderPane(
                subscene,
                toolbar,
                null,
                null,
                null );

        var objectProp = new SimpleObjectProperty<>(0.0d);
        var dProperty = DoubleProperty.doubleProperty(objectProp);
        _doublep.bind( dProperty );

        var spinner = new Spinner<Double>(0, 360, 0);

        _doublep.bind( spinner.getValueFactory().valueProperty() );

        toolbar.getItems().add( spinner );
        layout.setTop( toolbar );

        var scene = new Scene(layout, 500, 500, true);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
