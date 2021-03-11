package de.michab.lab;

import java.util.Collections;
import java.util.List;

public class Permute
{

    static void swap( char[] array, int first, int last )
    {
        char tmp = array[last];
        array[last] = array[first];
        array[first] = tmp;
    }

    static void reverse( char[] array, int first, int last )
    {
        last--;

        if ( first > last )
            throw new IllegalArgumentException();
        if ( first == last )
            return;

        do
        {
            swap( array, first++, last-- );
        } while ( first < last );
    }

    static <T> void reverse( List<T> array, int first, int last )
    {
        last--;

        if ( first > last )
            throw new IllegalArgumentException();
        if ( first == last )
            return;

        do
        {
            Collections.swap( array, first++, last-- );
        } while ( first < last );
    }

    static boolean next_permutation( char[] array, int first, int last )
    {
        if (first == last )
            return false;
        int i = first;
        ++i;
        if ( i == last )
            return false;
        i = last;
        --i;

        while ( true )
        {
            int ii = i;
            --i;
            if ( array[i] < array[ii] )
            {
                int j = last;
                while (!(array[i] < array[--j]))
                    ;
                swap( array, i, j );
                reverse( array, ii, last );
                return true;
            }
            if (i == first)
            {
                reverse( array, first, last);
                return false;
            }
        }
    }

    static <T extends Comparable<T>> boolean next_permutation( List<T> array, int first, int last )
    {
        if (first == last )
            return false;
        int i = first;
        ++i;
        if ( i == last )
            return false;
        i = last;
        --i;

        while ( true )
        {
            int ii = i;
            --i;
            if ( array.get( i ).compareTo( array.get( ii ) ) < 0 )
            {
                int j = last;
                while (!(array.get( i ).compareTo( array.get( --j ) ) < 0 ) )
                    ;

                Collections.swap( array, i, j );
                reverse( array, ii, last );
                return true;
            }
            if (i == first)
            {
                reverse( array, first, last);
                return false;
            }
        }
    }

    public static <T extends Comparable<T>> boolean next_permutation( List<T> array )
    {
        return next_permutation( array, 0, array.size() );
    }

    public static void main( String[] args )
    {
        char[] xs = "binz".toCharArray();
        do
        {
            System.out.println( new String( xs ) );
        } while ( next_permutation(xs, 0, xs.length ) );
    }
}
