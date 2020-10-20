package moleculesampleapp;

import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

public class Tetrahedron extends MeshView
{
    Tetrahedron()
    {
        float[] points = {
                10,10,10,
                20,20,0,
                0,20,0,
                10,20,20
        };
        // Reuse a single texture mapping.
        float[] texCoo = {
                0f,0f,
                0f,1f,
                1f,0f
        };
        int[] faces = {
                0,0,2,1,1,2,
                0,0,1,2,2,1,
                0,0,1,1,3,2,
                0,0,3,2,1,1,
                0,0,3,1,2,2,
                0,0,2,2,3,1,
                1,0,3,1,2,2,
                1,0,2,2,3,1
        };

        var mesh =
                new TriangleMesh();
        mesh.getPoints().addAll(
                points );
        mesh.getTexCoords().addAll(
                texCoo );
        mesh.getFaces().addAll(
                faces );
        setMesh(
                mesh );
    }
}
