package moleculesampleapp;

import java.util.List;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Spinner;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.stage.Stage;


public class SphereAndBox extends Application
{
    private double anchorX;
    private double anchorY;
    private double anchorAngle;

    private Spinner<Double> makeSpinner( String name, DoubleProperty dp, double min, double max )
    {
        var result = new Spinner<Double>(
                min,
                max,
                dp.doubleValue());
        result.setTooltip(
                new Tooltip( name ) );
        result.setEditable(
                true );
        dp.bind(
                result.getValueFactory().valueProperty() );
        return result;
    }

    private SubScene subscene( List<Node> toolbar )
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

        //        result.setOnMousePressed( event -> {
        //            anchorX = event.getSceneX();
        //            anchorY = event.getSceneY();
        //            anchorAngle = parent.getRotate();
        //        });
        //
        //        result.setOnMouseDragged( event -> {
        //            parent.setRotate(anchorAngle + anchorX - event.getSceneX());
        //        });

        toolbar.add( makeSpinner( "tz", box.translateZProperty(), 100, 5000 ) );
        toolbar.add( makeSpinner( "rotate", parent.rotateProperty(), 0, 360 ) );

        {
            var pointLight = new PointLight(Color.ANTIQUEWHITE);
            pointLight.setTranslateX(15);
            pointLight.setTranslateY(-10);
            pointLight.setTranslateZ(-100);
            root.getChildren().add(pointLight);
        }

        result.setCamera(
                new PerspectiveCamera(false));

        return result;
    }

    @Override
    public void start( Stage primaryStage )
    {
        primaryStage.setTitle("SphereAndBox");

        ToolBar toolbar =
                new ToolBar();
        var subscene =
                subscene(toolbar.getItems());
        var layout = new BorderPane(
                subscene,
                toolbar,
                null,
                null,
                null );
        layout.setTop(
                toolbar );

        var scene = new Scene(layout, 500, 500, true);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main( String[] args )
    {
        System.setProperty("prism.dirtyopts", "false");
        Application.launch(SphereAndBox.class, args);
    }
}
