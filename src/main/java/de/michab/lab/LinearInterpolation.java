/* $Id$
 *
 * Laboratory.
 *
 * Released under Gnu Public License
 * Copyright © 2012 Michael G. Binz
 */

package de.michab.lab;

import java.awt.Color;
import java.util.Arrays;

import org.smack.util.MathUtil;

/**
 * Experimental class for polynomial computation.
 *
 * @version $Rev: 781 $
 * @author Michael Binz
 */
public class LinearInterpolation
{
    public static final int X = 0;
    public static final int Y = 1;
    public static final int Z = 2;


    /**
     * Distribute values in a range between a and b evenly.
     * For the values a=0, b=2, slots=3 the result is [0, 1, 2].  If a is
     * equal to b then all values of the result have the same value.
     *
     * @param a The start of the range.
     * @param b The end of the range.
     * @param slots The number of points to interpolate, this includes a and b.
     * Thus slots is to be at least 2.
     * @return An array of evenly distributed values of size 'slots'.
     */
    private static double[] distribute( double a, double b, int slots )
    {
        if ( slots < 2 )
            throw new IllegalArgumentException( "slots < 2" );

        double[] result = new double[slots];

        double step = (b-a) / (slots-1);

        result[0] = a;

        for ( int i = 1 ; i < slots-1 ; i++ )
            result[i] = a + i*step;

        result[slots-1] = b;

        return result;
    }

    /**
     *
     */
    public static double[][] interpolate(
            double x0,
            double y0,
            double x1,
            double y1,
            int n )
    {
        if ( n <= 2 )
            throw new IllegalArgumentException( "n<=2" );

        double[][] result = new double[2][];
        result[X] = distribute( x0, x1, n );
        result[Y] = new double[n];

        double m = (y0-y1)/(x0-x1);
        double t = y0-m*x0;

        // Polynomial f(x) = mx + t;
        double[] polynomial =
            new double[]{ m, t };

        for ( int i = 0 ; i < result[X].length ; i++ )
            result[Y][i] =
                MathUtil.computePolynomial( result[X][i], polynomial );

        return result;
    }

    /**
     *
     */
    public static double[][] interpolate2(
           double x0,
           double y0,
           double x1,
           double y1,
           int n )
    {
        if ( n <= 2 )
            throw new IllegalArgumentException( "n<=2" );

        double[][] result = new double[2][];
        result[X] = distribute( x0, x1, n );
        result[Y] = distribute( y0, y1, n );

        return result;
    }

    public static double[][] interpolate(
            double x0,
            double y0,
            double z0,
            double x1,
            double y1,
            double z1,
            int n
            )
    {
        double[][] twod_xy = interpolate( x0, y0, x1, y1, n );
        double[][] twod_xz = interpolate( x0, y0, z1, z1, n );

        return new double[][]{ twod_xy[X], twod_xy[Y], twod_xz[Y] };
    }
    public static double[][] interpolate2(
            double x0,
            double y0,
            double z0,
            double x1,
            double y1,
            double z1,
            int n
            )
    {
        return new double[][]{
                distribute( x0, x1, n ),
                distribute( y0, y1, n ),
                distribute( z0, z1, n ) };
    }

    public static void main( String[] argv )
    {
        double[] result = distribute( 0, 2, 3 );
        System.err.println( Arrays.toString( result ) );

        double[][] result2 = interpolate( 0, 0, 4, 4, 5 );
        System.err.println( Arrays.toString( result2[X] ) );
        System.err.println( Arrays.toString( result2[Y] ) );

        double[][] result3 = interpolate( 0, 0, 0, 4, 4, 4, 5 );
        System.err.println( Arrays.toString( result3[X] ) );
        System.err.println( Arrays.toString( result3[Y] ) );
        System.err.println( Arrays.toString( result3[Z] ) );

        // big time -- We compute the intermediate values between
        // white and yellow.
        double x0 = Color.WHITE.getRed();
        double y0 = Color.WHITE.getGreen();
        double z0 = Color.WHITE.getBlue();
        double x1 = Color.GREEN.getRed();
        double y1 = Color.GREEN.getGreen()-1;
        double z1 = Color.GREEN.getBlue();

        System.err.println( "Ip2" );

        double[][] result4 = interpolate2( x0, y0, z0, x1, y1, z1, 5 );

        System.err.println( Arrays.toString( result4[X] ) );
        System.err.println( Arrays.toString( result4[Y] ) );
        System.err.println( Arrays.toString( result4[Z] ) );

        System.err.println( "Ip1" );
        if ( x0 == x1 )
            System.exit( 1 );
        if ( y0 == y1 )
            System.exit( 2 );
        if ( z0 == z1 )
            System.exit( 3 );

        result4 = interpolate( x0, y0, z0, x1, y1, z1, 5 );
        System.err.println( Arrays.toString( result4[X] ) );
        System.err.println( Arrays.toString( result4[Y] ) );
        System.err.println( Arrays.toString( result4[Z] ) );
    }
}
