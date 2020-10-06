/* $Id$
 *
 * Laboratory.
 *
 * Released under Gnu Public License
 * Copyright © 2012 Michael G. Binz
 */

package de.michab.lab;

import java.awt.Point;
import java.awt.geom.Ellipse2D;

import org.jdesktop.smack.util.MathExt;


/**
 * Allows to iterate step-wise on an ellipse.
 *
 * @version $Rev: 781 $
 * @author Michael Binz
 */
public class EllipticPath
{
    private final double _x;
    private final double _y;
    private final double _a;
    private final double _b;

    /**
     *
     * @param e
     */
    public EllipticPath( Ellipse2D e )
    {
        _x = e.getX();
        _y = e.getY();
        _a = e.getWidth() / 2.0;
        _b = e.getHeight() / 2.0;
    }

    /**
     *
     * @param x
     * @return
     */
    private double yFor( double x )
    {
        // TODO Think about the possibility of overflows when squaring.
        double x2 = x * x;
        double a2 = _a * _a;
        double b2 = _b * _b;

        double result = Math.sqrt(
              (1.0 - (x2 / a2)) * b2 );

//       double xda = x / _a;
//
//       double result2 = Math.sqrt(
//               (1.0 - (xda*xda)) * b2 );
//
//       System.err.println( "Epsilon: "  + Math.abs(result-result2)  );

        return result;
    }

    /**
     *
     * @param current
     * @param radius
     */
    private Point nextImpl( Point current, float arc )
    {
        // Deliberately use integers in x direction.
        int rx = MathExt.round( current.x + arc );
        double ry = yFor( rx );

        // TODO Note that we could limit the number of required
        // iterations if we swap x and y in case the rectangle spanned
        // by current and arc is higher than wide.
        if ( arc < (Math.abs( current.getY() - ry ) ) )
            System.err.println( "Could optimize." );

        double epsilon = Math.abs(
                arc -
                MathExt.distance(
                        current.x, current.y, rx, ry ) );

        // TODO Make sure we terminate always.
        while ( true )
        {
            int nrx = rx -1;
            double nry = yFor( nrx );

            double nepsilon = Math.abs(
                    arc -
                    MathExt.distance(
                            current.x, current.y, nrx, nry ) );

            // If the error is getting bigger...
            if ( nepsilon >= epsilon )
                // ...we are done.
                return new Point( rx, MathExt.round( ry ) );

            rx = nrx;
            ry = nry;
            epsilon = nepsilon;
        }
    }

    /**
     * Get the point that has a distance of arc from the current
     * point and that lies clockwise on the ellipse.
     *
     * @param current The current point.
     * @param arc The distance.
     * @return A newly allocated point.
     */
    public Point next( Point current, float radius )
    {
        // TODO Make sure the passed point is on the ellipse.

        // Translate into our internal, normalized coordinate system.
        Point translated = new Point(
                (int)(current.x - _x - _a),
                (int)(_b + _y - current.y) );

        Point next = nextImpl( translated, radius );

        translated.x = (int)(next.x + _x + _a);
        translated.y = (int)(_b + _y - next.y);

        return translated;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return getClass().getSimpleName() +
            "[x=" + _x + ",y=" + _y + ",a=" + _a + ",b=" + _b + "]";
    }
}
