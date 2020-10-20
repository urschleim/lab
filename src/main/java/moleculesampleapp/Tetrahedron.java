package moleculesampleapp;

import javafx.geometry.Point3D;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

public class Tetrahedron extends MeshView
{
    static float[] getPoints( Point3D ...point3ds )
    {
        var result = new float[ 3 * point3ds.length ];

        for ( var i = 0 ; i < point3ds.length ; i++ )
        {
            var c = point3ds[i];

            var targetIdx = i*3;

            result[targetIdx] = (float)c.getX();
            result[targetIdx+1] = (float)c.getY();
            result[targetIdx+2] = (float)c.getZ();
        }

        return result;
    }

    Tetrahedron()
    {
        float[] points = {
                10,10,10,
                20,20,0,
                0,20,0,
                10,20,20
        };
        float[] points2 = getPoints(
                new Point3D( 10,10,10 ),
                new Point3D( 20,20,0 ),
                new Point3D( 0,20,0 ),
                new Point3D( 10,20,20 ) );

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
                points2 );
        mesh.getTexCoords().addAll(
                texCoo );
        mesh.getFaces().addAll(
                faces );
        setMesh(
                mesh );
    }
}
