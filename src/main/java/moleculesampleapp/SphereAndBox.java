package moleculesampleapp;

import java.util.List;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Orientation;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;


public class SphereAndBox extends Application
{
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

    /**
     * https://stackoverflow.com/questions/30145414/rotate-a-3d-object-on-3-axis-in-javafx-properly
     * @param n
     * @param alf
     * @param bet
     * @param gam
     */
    private void matrixRotateNode(Node n, double alf, double bet, double gam){
        double A11=Math.cos(alf)*Math.cos(gam);
        double A12=Math.cos(bet)*Math.sin(alf)+Math.cos(alf)*Math.sin(bet)*Math.sin(gam);
        double A13=Math.sin(alf)*Math.sin(bet)-Math.cos(alf)*Math.cos(bet)*Math.sin(gam);
        double A21=-Math.cos(gam)*Math.sin(alf);
        double A22=Math.cos(alf)*Math.cos(bet)-Math.sin(alf)*Math.sin(bet)*Math.sin(gam);
        double A23=Math.cos(alf)*Math.sin(bet)+Math.cos(bet)*Math.sin(alf)*Math.sin(gam);
        double A31=Math.sin(gam);
        double A32=-Math.cos(gam)*Math.sin(bet);
        double A33=Math.cos(bet)*Math.cos(gam);

        double d = Math.acos((A11+A22+A33-1d)/2d);
        if(d!=0d){
            double den=2d*Math.sin(d);
            Point3D p= new Point3D((A32-A23)/den,(A13-A31)/den,(A21-A12)/den);
            n.setRotationAxis(p);
            n.setRotate(Math.toDegrees(d));
        }
    }

    private void inc( double[] a, int idx )
    {
        var v = a[idx] + 1;

        if ( v > 360 )
            v -= 360;
        else if ( v < 0 )
            v += 360;

        a[idx] = v;
    }

    private void dec( double[] a, int idx )
    {
        var v = a[idx] - 1;

        if ( v > 360 )
            v -= 360;
        else if ( v < 0 )
            v += 360;

        a[idx] = v;
    }

    private SubScene subscene( List<Node> toolbar )
    {
        var box = new Box(400, 400, 400);

        var parent = new Group(box);
        parent.setTranslateZ(500);
        parent.setRotationAxis( Rotate.X_AXIS );

        var root = new Group(parent);

        var result = new SubScene(
                root,
                500,
                500,
                true,
                SceneAntialiasing.DISABLED);
        result.setFill( Color.DARKGREEN );

        toolbar.add( makeSpinner( "tz", box.translateZProperty(), 100, 5000 ) );
//        toolbar.add( makeSpinner( "rotate", parent.rotateProperty(), 0, 360 ) );

        // roll z, pitch x, yaw y.
        double[] rotations = new double[3];

        result.setFocusTraversable( true );
        result.setOnKeyPressed( e -> {
            if ( e.getCode().equals( KeyCode.UP ) )
            {
                inc( rotations, 1 );
                System.out.println( "x:" + rotations[1] );
            }
            else if ( e.getCode().equals( KeyCode.DOWN ) )
            {
                dec( rotations, 1 );
                System.out.println( "x:" + rotations[1] );
            }
            else if ( e.getCode().equals( KeyCode.LEFT ) )
            {
                dec( rotations, 2 );
            }
            else if ( e.getCode().equals( KeyCode.RIGHT ) )
            {
                inc( rotations, 2 );
            }
            else {
                return;
            }
            matrixRotateNode(
                    parent,
                    Math.toRadians( rotations[0] ),
                    Math.toRadians( rotations[1] ),
                    Math.toRadians( rotations[2] )
                    );
        });

        result.setCamera(
                new PerspectiveCamera());

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

        var sl = new Slider( 0, 360, 0 );
        sl.setOrientation( Orientation.VERTICAL );

        var layout = new BorderPane(
                subscene,
                null,
                null,
                null,
                null );

        var scene =
                new Scene(layout);
        scene.setFill( Color.BLACK );

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main( String[] args )
    {
        System.setProperty("prism.dirtyopts", "false");
        Application.launch(SphereAndBox.class, args);
    }
}
