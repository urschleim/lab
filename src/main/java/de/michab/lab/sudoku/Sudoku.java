/* $Id$
 *
 * Laboratory.
 *
 * Released under Gnu Public License
 * Copyright © 2012 Michael G. Binz
 */

package de.michab.lab.sudoku;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jdesktop.smack.util.CollectionUtils;
import org.jdesktop.smack.util.MathExt;
import org.jdesktop.util.TimeProbe;


/**
 * Brute force sudoku.
 *
 * @version $Rev: 1992 $
 * @author Michael Binz
 */
public class Sudoku
{
    private TimeProbe _timer =
            new TimeProbe( Sudoku.class.getSimpleName()  );

    private static final int SIZE = 9;

    private List<Set<Integer>> rowSets =
        new ArrayList<>( SIZE );
    private List<Set<Integer>> colSets =
        new ArrayList<>( SIZE );
    private List<Set<Integer>> grpSets =
        new ArrayList<>( SIZE );

    private Integer[] _cells = new Integer[ SIZE * SIZE ];

    private final Integer[] _preset;

    /**
     *
     * @param preset
     */
    public Sudoku( Integer[] preset )
    {
        _preset = preset;

        for ( int i = 0 ; i < 9 ; i++ )
        {
            rowSets.add( makeSudokuSet() );
            colSets.add( makeSudokuSet() );
            grpSets.add( makeSudokuSet() );
        }
    }

    /**
     * Creates a set containing the integer numbers [1..9].
     *
     * @return A set.
     */
    public static Set<Integer> makeSudokuSet()
    {
        Set<Integer> result = new HashSet<>();

        for ( int i = 1 ; i < 10 ; i++ )
            result.add( i );

        return result;
    }


    /**
     * Computes a row index from the cell index.
     *
     * @param cellIdx The cell index.
     * @return The corresponding row index.
     */
    private static int rowIdx( int cellIdx )
    {
        return cellIdx / SIZE;
    }
    /**
     * Computes a column index from the cell index.
     *
     * @param cellIdx The cell index.
     * @return The corresponding column index.
     */
    private static int colIdx( int cellIdx )
    {
        return cellIdx % SIZE;
    }

    /**
     * Computes a group (3*3) index from the cell index.
     *
     * @param cellIdx The cell index.
     * @return The corresponding group index.
     */
    private static int grpIdx( int cellIdx )
    {
        int grpColIdx = cellIdx / 3;

        int grpMod = grpColIdx % 3;
        int grpDiv = grpColIdx / 9;

        return (grpDiv * 3) + grpMod;
    }



    /**
     * Print the (81) integers in the passed array nicely as a sudoku.
     *
     * @param c The sudoku cells.
     */
    private void printSolution( Integer[] c )
    {
        int toEighty = 0;

        for ( int x = 0 ; x < 9 ; x++ )
        {
            for ( int y = 0 ; y < 9 ; y++ )
            {
                System.out.print( c[toEighty++] );
                System.out.print( ' ' );
            }
            System.out.println();
        }
        System.out.println( "-----------------" + _timer );
    }



    /**
     * The current maximum index.  This grows while the solver works.
     */
    private int maxIdx = 0;



    /**
     * Solve the sudoku starting at the passed index.
     *
     * @param cellIdx The start index.
     */
    private void solveImpl( int cellIdx )
    {
        maxIdx = MathExt.max( cellIdx, maxIdx );

        final int rowIdx = rowIdx( cellIdx );
        final int colIdx = colIdx( cellIdx );
        final int grpIdx = grpIdx( cellIdx );

        Set<Integer> rowSet = rowSets.get( rowIdx );
        Set<Integer> colSet = colSets.get( colIdx );
        Set<Integer> grpSet = grpSets.get( grpIdx );

        try
        {
            Set<Integer> intersection =
                CollectionUtils.intersection(
                        grpSet,
                        CollectionUtils.intersection(
                                rowSet,
                                colSet ) );

            if ( _preset[cellIdx] != 0 )
            {
                if ( intersection.contains( _preset[cellIdx] ))
                {
                    intersection.clear();
                    intersection.add( _preset[cellIdx] );
                }
                else
                    intersection = Collections.emptySet();
            }

            if ( intersection.isEmpty() )
                return;

            for ( Integer c : intersection )
            {
                Set<Integer> nrowSet =
                    new HashSet<>( rowSet );
                Set<Integer> ncolSet =
                    new HashSet<>( colSet );
                Set<Integer> ngrpSet =
                    new HashSet<>( grpSet );

                nrowSet.remove( c );
                ncolSet.remove( c );
                ngrpSet.remove( c );

                rowSets.set( rowIdx, nrowSet );
                colSets.set( colIdx, ncolSet );
                grpSets.set( grpIdx, ngrpSet );

                _cells[cellIdx] = c;

                if ( cellIdx == 80 )
                    printSolution( _cells );
                else
                    solveImpl( cellIdx+1 );
            }
        }
        finally
        {
            _cells[cellIdx] = null;
            rowSets.set( rowIdx, rowSet );
            colSets.set( colIdx, colSet );
            grpSets.set( grpIdx, grpSet );
        }
    }

    /**
     *
     */
    public void solve()
    {
        _timer.start();
        solveImpl( 0 );
        _timer.stop();

        System.err.println( "Full run took " + _timer );
    }

    /**
     * External entry.
     *
     * @param argv Command line -- not used.
     */
    public static void main( String[] argv )
    {
        Sudoku s = new Sudoku( SudokuDb.RTV_41_2012_S );

        s.solve();
    }
}
