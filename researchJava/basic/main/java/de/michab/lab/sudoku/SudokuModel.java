/* $Id$
 *
 * Laboratory.
 *
 * Released under Gnu Public License
 * Copyright © 2012 Michael G. Binz
 */

package de.michab.lab.sudoku;

import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @version $Rev: 614 $
 * @author Michael
 */
@SuppressWarnings("serial")
public class SudokuModel
    extends AbstractTableModel
{
    private final Integer[] _cells;

    public SudokuModel()
    {
        this( new Integer[ 9 * 9 ] );
    }
    public SudokuModel( Integer[] sudokuEightyOne )
    {
        if ( sudokuEightyOne.length != 9*9 )
            throw new IllegalArgumentException( "Length must be 9*9" );

        _cells = normalize( sudokuEightyOne.clone() );
    }

    public Integer[] getSudoku()
    {
        return deNormalize( _cells.clone() );
    }

    /**
     * Computes a row index from the cell index.
     *
     * @param cellIdx The cell index.
     * @return The corresponding row index.
     */
    private static int rowIdx( int cellIdx )
    {
        return cellIdx / 9;
    }
    /**
     * Computes a column index from the cell index.
     *
     * @param cellIdx The cell index.
     * @return The corresponding column index.
     */
    private static int colIdx( int cellIdx )
    {
        return cellIdx % 9;
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

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getRowCount()
     */
    @Override
    public int getRowCount()
    {
        return 9;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    @Override
    public int getColumnCount()
    {
        return 9;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getColumnName(int)
     */
    @Override
    public String getColumnName( int columnIndex )
    {
        return Integer.toString( columnIndex );
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getColumnClass(int)
     */
    @Override
    public Class<?> getColumnClass( int columnIndex )
    {
        return Integer.class;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#isCellEditable(int, int)
     */
    @Override
    public boolean isCellEditable( int rowIndex, int columnIndex )
    {
        return true;
    }

    private int toArrayIdx( int r, int c )
    {
        return (9 * r) + c;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    @Override
    public Object getValueAt( int rowIndex, int columnIndex )
    {
        return _cells[ toArrayIdx( rowIndex, columnIndex ) ];
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#setValueAt(java.lang.Object, int, int)
     */
    @Override
    public void setValueAt( Object aValue, int rowIndex, int columnIndex )
    {
        _cells[ toArrayIdx( rowIndex, columnIndex ) ] = (Integer)aValue;
    }

    private static Integer[] normalize( Integer[] in )
    {
        Integer zero = Integer.valueOf( 0 );

        for ( int i = in.length-1 ; i >= 0 ; i-- )
            if ( zero.equals( in[i] ) )
                in[i] = null;

        return in;
    }

    /**
     *
     * @param in
     * @return
     */
    private static Integer[] deNormalize( Integer[] in )
    {
        Set<Integer> prototypes = Sudoku.makeSudokuSet();
        Integer zero = Integer.valueOf( 0 );

        for ( int i = in.length-1 ; i >= 0 ; i-- )
            if ( prototypes.contains( in[i] ) )
                in[i] = zero;

        return in;
    }

    public static void main( String[] args )
    {
        SwingUtilities.invokeLater( new Runnable()
        {

            @Override
            public void run()
            {
                JTable table =
                        new JTable(
                                new SudokuModel(
                                        SudokuDb.RTV_41_2012_S ) );

                JFrame mainFrame = new JFrame( "Sudoku" );
                mainFrame.getContentPane().add( table );
                mainFrame.pack();
                mainFrame.setDefaultCloseOperation(
                        WindowConstants.EXIT_ON_CLOSE );
                mainFrame.setVisible( true );
            }
        } );
    }
}
