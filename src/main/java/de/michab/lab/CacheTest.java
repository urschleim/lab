/* $Id$
 *
 * Experimental.
 *
 * Released under Gnu Public License
 * Copyright © 2010 Michael G. Binz
 */
package de.michab.lab;

import java.util.concurrent.ExecutionException;

import org.jdesktop.smack.util.MathExt;
import org.jdesktop.util.Duration;



/**
 * @author Michael Binz
 */
public class CacheTest
{
    private static Cache<Integer, Double> elCache =
        new Cache<>( new Cache.ValueFactory<Integer, Double>()
        {

            @Override
            public Double create( Integer pKey )
            {
                try
                {
                    System.err.println( Thread.currentThread().getName() + ": Init : " + pKey );
                    Thread.sleep( org.jdesktop.util.Duration.MS_SEC );
                }
                catch ( InterruptedException e )
                {

                }

                return Double.valueOf( pKey.intValue() );
            }
        } );

    private static Runnable cacheStresser = new Runnable() {

        @Override
        public void run()
        {
            try
            {
                runImpl();
            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }
        }

        public void runImpl() throws InterruptedException, ExecutionException
        {
            long endTime =
                System.currentTimeMillis() +
                10 * Duration.MS_SEC;

            while ( System.currentTimeMillis() < endTime )
            {
                Double result =
                    elCache.get(  new Integer( MathExt.randomBetween( 0, 100 ) ) );

                if ( result == null )
                    System.err.print( "bah" );
            }
            System.err.println( Thread.currentThread().getName() + " ended." );
        }
    };



    /**
     *
     * @param argv
     */
    public static void main( String[] argv )
    {
        int numThreads = 9;

        for ( int i = 0 ; i <= numThreads ; i++ )
        {
            new Thread( cacheStresser, "Stresser " + i ).start();
        }
    }
}
