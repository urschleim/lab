package de.michab.lab.sudoku;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.smack.application.CliApplication;
import org.smack.util.TimeProbe;

/**
 *
 */
final public class Decompose extends CliApplication
{
    private List<List<Integer>> dc2(
            List<Integer> pool,
            int number,
            int count )
    {
        if ( count < 1 )
            throw new AssertionError();

        List<List<Integer>> results = new ArrayList<>();

        if ( pool.size() < count )
            return results;

        // Recursion termination.
        if ( count == 1 )
        {
            List<Integer> result = new ArrayList<>();

            boolean contained =
                    pool.remove( Integer.valueOf( number ) );

            if ( contained )
            {
                result.add( Integer.valueOf( number ) );
                results.add( result );
            }

            return results;
        }

        while ( ! pool.isEmpty() )
        {
            // Get first element from pool.
            int currentValue =
                    pool.remove( 0 );

            // If this is larger than the target number we're done.
            // This will never work since being the smallest element.
            if ( currentValue > number )
                return results;

            List<List<Integer>> result = dc2(
                    // Clone.
                    new ArrayList<>( pool ),
                    number-currentValue,
                    count-1 );

            for ( List<Integer> c : result )
            {
                c.add( currentValue );
                results.add( c );
            }
        }

        return results;
    }

    @Command
    protected void dec2( int number, int count )
    {
        List<Integer> all =
                Arrays.asList( 1,2,3,4,5,6,7,8,9 );
        // Clone.
        all = new ArrayList<>( all );

        TimeProbe tp = new TimeProbe("dc2").start();
        List<List<Integer>> permutations =
                    dc2( all, number, count );
        out( "%s\n", tp );

        out( "%d -> %d : (%d)\n", number, count, permutations.size() );
        for ( List<Integer> c : permutations )
            out( c.toString() + "\n" );
    }

    @Command
    protected void full( int count )
    {
        Integer[] aa =
            {1,2,3,4,5,6,7,8,9};

        List<Integer> all =
                Arrays.asList( aa );

        for ( int i = 1 ; i < 45 ; i++ )
        {
            TimeProbe tp = new TimeProbe("dc2").start();
            List<List<Integer>> permutations =
                        dc2( new ArrayList<>( all ), i, count );
            out( "%s\n", tp );

            out( "%d -> %d\n : %d\n", i, count, permutations.size() );
            for ( List<Integer> c : permutations )
                out( c.toString() + "\n" );
        }
    }

    public static void main( String[] argv )
    {
        launch( Decompose.class, argv );
    }
}
