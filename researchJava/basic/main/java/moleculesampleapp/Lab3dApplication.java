/*
 * $Id$
 * Released under Gnu Public License
 * Copyright © 2021 Michael G. Binz
 */
package moleculesampleapp;

import java.util.List;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Orientation;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.Parent;
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
import javafx.stage.Stage;

public abstract class Lab3dApplication extends Application
{
    protected abstract Parent createActors( List<Node> toolbar );

    protected Spinner<Double> makeSpinner( String name, DoubleProperty dp, double min, double max )
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

    protected Slider makeSlider(
            String name,
            Orientation orientation,
            DoubleProperty dp,
            double min,
            double max )
    {
        var result = new Slider(
                min,
                max,
                dp.doubleValue());

        result.setOrientation(
                orientation );
        result.setTooltip(
                new Tooltip( name ) );
        result.setBlockIncrement( 1 );
        dp.bind(
                result.valueProperty() );
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
        var parent = createActors( toolbar );

        var result = new SubScene(
                parent,
                500,
                500,
                true,
                SceneAntialiasing.DISABLED);
        result.setFill( Color.DARKGREEN );

        // roll z, pitch x, yaw y.
        double[] rotations = new double[3];

        result.setFocusTraversable( true );

        result.setOnKeyPressed( e -> {
            if ( e.getCode().equals( KeyCode.UP ) )
            {
                inc( rotations, 1 );
            }
            else if ( e.getCode().equals( KeyCode.DOWN ) )
            {
                dec( rotations, 1 );
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

            e.consume();

            matrixRotateNode(
                    parent,
                    Math.toRadians( rotations[0] ),
                    Math.toRadians( rotations[1] ),
                    Math.toRadians( rotations[2] ) );
        });

        var camera1 = new PerspectiveCamera( true );
        camera1.setTranslateZ( -500 );
        camera1.setNearClip(1);
        camera1.setFarClip(1000);

        result.setCamera(
                camera1 );

        return result;
    }

    private Parent _parent;
    private DoubleProperty _x = new SimpleDoubleProperty();
    private DoubleProperty _y = new SimpleDoubleProperty();
    private DoubleProperty _z = new SimpleDoubleProperty();

    private void rotationHandler(
            ObservableValue<? extends Number> observable,
            Number oldValue,
            Number newValue )
    {
        matrixRotateNode(
                _parent,
                Math.toRadians( _z.get() ),
                Math.toRadians( _x.get() ),
                Math.toRadians( _y.get() ) );
    }

    @Override
    public void start( Stage primaryStage )
    {
        primaryStage.setTitle("3D lab");

        ToolBar toolbar =
                new ToolBar();
        var subscene =
                subscene(toolbar.getItems());

        _parent = subscene.getRoot();

        _x = new SimpleDoubleProperty( _parent, "x" );
        _y = new SimpleDoubleProperty( _parent, "y" );
        _z = new SimpleDoubleProperty( _parent, "z" );

        _x.addListener( this::rotationHandler );
        _y.addListener( this::rotationHandler );
        _z.addListener( this::rotationHandler );

        var layout = new BorderPane(
                subscene,
                toolbar,
                makeSlider( "x", Orientation.VERTICAL, _x, 0, 359 ),
                makeSlider( "y", Orientation.HORIZONTAL, _y, 0, 359 ),
                null );

        var scene =
                new Scene(layout);
        scene.setFill(
                Color.PINK );

        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
