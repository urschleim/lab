package moleculesampleapp;

import javafx.application.Application;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;


public class SphereAndBox extends Application
{
    public static void main( String[] args )
    {

        System.setProperty("prism.dirtyopts", "false");
        Application.launch(SphereAndBox.class, args);
    }

    double anchorX;
    double anchorY;
    double anchorAngle;

    Image bumpMap = new Image(getClass().getResourceAsStream("jfx.png"));

    PerspectiveCamera addCamera(Scene scene)
    {
        var perspectiveCamera = new PerspectiveCamera(false);
        scene.setCamera(perspectiveCamera);
        return perspectiveCamera;
    }
    PerspectiveCamera addCamera(SubScene scene)
    {
        var perspectiveCamera = new PerspectiveCamera(false);
        scene.setCamera(perspectiveCamera);
        return perspectiveCamera;
    }

    public
    SubScene subscene(  )
    {

        var boxMaterial = new PhongMaterial();
        boxMaterial.setDiffuseColor(Color.GREEN);
        boxMaterial.setSpecularColor(Color.WHITESMOKE);

        var sphereMaterial = new PhongMaterial();
        sphereMaterial.setDiffuseColor(Color.BISQUE);
        sphereMaterial.setSpecularColor(Color.LIGHTBLUE);
        sphereMaterial.setBumpMap(bumpMap);

        var box = new Box(400, 400, 400);
        box.setMaterial(boxMaterial);

        var sphere = new Sphere(200);
        sphere.setMaterial(sphereMaterial);

        sphere.setTranslateX(250);
        sphere.setTranslateY(250);
        sphere.setTranslateZ(50);
        box.setTranslateX(250);
        box.setTranslateY(250);
        box.setTranslateZ(450);

        var parent = new Group(box, sphere);
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

        var pointLight = new PointLight(Color.ANTIQUEWHITE);
        pointLight.setTranslateX(15);
        pointLight.setTranslateY(-10);
        pointLight.setTranslateZ(-100);

        root.getChildren().add(pointLight);

        addCamera(result);

        return result;
    }
    public
    void starto( Stage primaryStage )
    {
        primaryStage.setTitle("SphereAndBox");

        var boxMaterial = new PhongMaterial();
        boxMaterial.setDiffuseColor(Color.GREEN);
        boxMaterial.setSpecularColor(Color.WHITESMOKE);

        var sphereMaterial = new PhongMaterial();
        sphereMaterial.setDiffuseColor(Color.BISQUE);
        sphereMaterial.setSpecularColor(Color.LIGHTBLUE);
        sphereMaterial.setBumpMap(bumpMap);

        var box = new Box(400, 400, 400);
        box.setMaterial(boxMaterial);

        var sphere = new Sphere(200);
        sphere.setMaterial(sphereMaterial);

        sphere.setTranslateX(250);
        sphere.setTranslateY(250);
        sphere.setTranslateZ(50);
        box.setTranslateX(250);
        box.setTranslateY(250);
        box.setTranslateZ(450);

        var parent = new Group(box, sphere);
        parent.setTranslateZ(500);
        parent.setRotationAxis(new Point3D(1, 1, 1));

        var root = new Group(parent);

//        var layout = new BorderPane( root );

        var scene = new Scene(root, 500, 500, true);

        scene.setOnMousePressed( event -> {
            anchorX = event.getSceneX();
            anchorY = event.getSceneY();
            anchorAngle = parent.getRotate();
        });

        scene.setOnMouseDragged( event -> {
            parent.setRotate(anchorAngle + anchorX - event.getSceneX());
        });

        var pointLight = new PointLight(Color.ANTIQUEWHITE);
        pointLight.setTranslateX(15);
        pointLight.setTranslateY(-10);
        pointLight.setTranslateZ(-100);

        root.getChildren().add(pointLight);

        addCamera(scene);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    @Override
    public
    void start( Stage primaryStage )
    {
        primaryStage.setTitle("SphereAndBox");

        var subscene = subscene();

        var layout = new BorderPane( subscene );

        layout.setTop( new Button("micbinz") );
        var scene = new Scene(layout, 500, 500, true);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
