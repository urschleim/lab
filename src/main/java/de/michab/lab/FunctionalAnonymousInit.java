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
        PhongMaterial a =
                ((Init<PhongMaterial>)(c) -> {
                    c.setDiffuseColor(Color.GREEN);
                    c.setSpecularColor(Color.WHITESMOKE);
                    return c;
                }).init( new PhongMaterial() );

        PhongMaterial b =
                ((Function<PhongMaterial,PhongMaterial>)(c) -> {
                    c.setDiffuseColor(Color.GREEN);
                    c.setSpecularColor(Color.WHITESMOKE);
                    return c;
                }).apply( new PhongMaterial() );

        PhongMaterial c =
                ((Init2<PhongMaterial>)(d) -> {
                    d.setDiffuseColor(Color.GREEN);
                    d.setSpecularColor(Color.WHITESMOKE);
                    return d;
                }).apply( new PhongMaterial() );
    }
}
