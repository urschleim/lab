package de.michab.lab;

import java.util.function.Function;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;

public class FunctionalAnonymousInit
{
    private interface Init<O> {
        public O init(O chars);
    }
    private interface Init2<O> extends Function<O, O> {
    }

    public static void main( String[] args )
    {
        PhongMaterial pm1 =
                ((Init<PhongMaterial>)(c) -> {
                    c.setDiffuseColor(Color.GREEN);
                    c.setSpecularColor(Color.WHITESMOKE);
                    return c;
                }).init( new PhongMaterial() );

        PhongMaterial pm2 =
                ((Function<PhongMaterial,PhongMaterial>)(c) -> {
                    c.setDiffuseColor(Color.GREEN);
                    c.setSpecularColor(Color.WHITESMOKE);
                    return c;
                }).apply( new PhongMaterial() );

        PhongMaterial pm3 =
                ((Init2<PhongMaterial>)(c) -> {
                    c.setDiffuseColor(Color.GREEN);
                    c.setSpecularColor(Color.WHITESMOKE);
                    return c;
                }).apply( new PhongMaterial() );

        System.out.printf( "pm1=%s%npm2=%s%npm3=%s%n", pm1, pm2, pm3 );
    }
}
