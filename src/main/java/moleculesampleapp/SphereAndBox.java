package moleculesampleapp;

import java.util.List;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.shape.Box;


public class SphereAndBox extends Lab3dApplication
{
    @Override
    protected Parent createActors( List<Node> toolbar )
    {
        var box = new Box(40, 40, 40);

        toolbar.add( makeSpinner(
                "width",
                box.widthProperty(),
                10,
                500 ) );

        return  new Group(box);
    }

    public static void main( String[] args )
    {
        System.setProperty("prism.dirtyopts", "false");
        Application.launch(SphereAndBox.class, args);
    }
}
