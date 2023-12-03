/*
 * Unpublished work.
 * Copyright © 2019 Michael G. Binz
 */
package de.michab.lab.tools.xslt;

import java.net.URL;

import org.jdesktop.application.ApplicationInfo;
import org.jdesktop.util.ResourceManager;
import org.jdesktop.util.ResourceManager.Resource;
import org.jdesktop.util.ServiceManager;

import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The MMT security administration application.
 *
 * @author Michael Binz
 */
public class XsltLabUi extends Application
{
    @Resource
    private static String _title = "n/a";

    static
    {
        ServiceManager.initApplicationService(
                new ApplicationInfo( XsltLabUi.class ) );

        ServiceManager.getApplicationService(
                ResourceManager.class )
        .injectResources( XsltLabUi.class );
    }

    private final SimpleDoubleProperty sceneWidth =
            new SimpleDoubleProperty( this, "sceneWidth", -1 );
    private final SimpleDoubleProperty sceneHeight =
            new SimpleDoubleProperty( this, "sceneHeight", -1 );
    private final SimpleDoubleProperty stageX =
            new SimpleDoubleProperty( this, "stageX", -1 );
    private final SimpleDoubleProperty stageY =
            new SimpleDoubleProperty( this, "stageY", -1 );

    @Override
    public void start(
            Stage stage ) throws Exception
    {
        // Set the application icon.
//        stage.getIcons().add( ServiceManager.getApplicationService(
//                ApplicationInfo.class ).getIcon() );

        String resourceName =
                String.format( "resources/%s.",
                        getClass().getSimpleName() );
        URL is =
                this.getClass().getResource(
                        resourceName + "fxml" );

        Parent root =
                FXMLLoader.load( is );

//        PropertyLink.persist2(
//                sceneWidth,
//                new NumberStringConverter() );
//        PropertyLink.persist2(
//                sceneHeight,
//                new NumberStringConverter() );
//        PropertyLink.persist2(
//                stageX,
//                new NumberStringConverter() );
//        PropertyLink.persist2(
//                stageY,
//                new NumberStringConverter() );

        if ( stageX.get() > 0 )
            stage.setX( stageX.get() );
        if ( stageY.get() > 0 )
            stage.setY( stageY.get() );

        Scene scene = new Scene(
                root,
                sceneWidth.get(),
                sceneHeight.get() );

        URL css =
                this.getClass().getResource(
                        resourceName + "css" );
        scene.getStylesheets().add( css.toString() );

        sceneHeight.bind(
                scene.heightProperty() );
        sceneWidth.bind(
                scene.widthProperty() );
        stageX.bind(
                stage.xProperty() );
        stageY.bind(
                stage.yProperty() );

        stage.setTitle(
                _title );

        stage.setScene(
                scene );

        stage.show();
    }

    public static void main( String[] argv )
    {
        launch( XsltLabUi.class, argv );
    }
}
