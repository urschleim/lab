package de.michab.lab;

import java.util.Collections;
import java.util.List;

public class Permute
{
    private static void swap( char[] array, int first, int last )
    {
        char tmp = array[last];
        array[last] = array[first];
        array[first] = tmp;
    }

    private static void reverse( char[] array, int first, int last )
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

    /**
     * In-place reversal of the elements in the passed list between
     * borders first, last.
     *
     * @param <T>
     * @param elements
     * @param first
     * @param last
     */
    public static <T> void reverse( List<T> elements, int first, int last )
    {
        last--;

        if ( first > last )
            throw new IllegalArgumentException();
        if ( first == last )
            return;

        do
        {
            Collections.swap( elements, first++, last-- );
        } while ( first < last );
    }

    // Special case implementation for character arrays.
    public static boolean next_permutation( char[] array, int first, int last )
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

    public static boolean next_permutation( char[] array )
    {
        return next_permutation( array, 0, array.length );
    }

    /**
     * A translation of the C++ implementation of std::next_permutation.
     * See https://en.cppreference.com/w/cpp/algorithm/next_permutation for
     * the algorithm.
     *
     * @param <T>
     * @param elements
     * @param first
     * @param last
     * @return
     */
    static <T extends Comparable<T>> boolean next_permutation( List<T> elements, int first, int last )
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
            if ( elements.get( i ).compareTo( elements.get( ii ) ) < 0 )
            {
                int j = last;
                while (!(elements.get( i ).compareTo( elements.get( --j ) ) < 0 ) )
                    ;

                Collections.swap( elements, i, j );
                reverse( elements, ii, last );
                return true;
            }
            if (i == first)
            {
                reverse( elements, first, last);
                return false;
            }
        }
    }

    public static <T extends Comparable<T>> boolean next_permutation( List<T> elements )
    {
        return next_permutation( elements, 0, elements.size() );
    }
}
