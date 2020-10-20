package moleculesampleapp;

import java.util.List;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;


public class SphereAndBox extends Lab3dApplication
{
    @Override
    protected Parent createActors( List<Node> toolbar )
    {
        var meshView = new Tetrahedron();

        var result = new Group( meshView );
        var scale = 4d;
        result.setScaleX( scale );
        result.setScaleY( scale );
        result.setScaleZ( scale );

        return result;
    }

    public static void main( String[] args )
    {
        System.setProperty("prism.dirtyopts", "false");
        Application.launch(SphereAndBox.class, args);
    }
}
