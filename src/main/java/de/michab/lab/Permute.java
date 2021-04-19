package de.michab.lab;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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
     * Permutes the elements in the passed list in-place.  Each call results in the next
     * permutation in the list.  Does not return duplicates.
     *
     * @param <T> The type of the elements.
     * @param elements A list of elements that is permuted in-place.  Note that this
     * list is required to be sorted in ascending order for the first call.
     * @param first The first element index to be part of the permutation.
     * @param last The last element index to be part of the permutation.  Note that this is excluding,
     * ie, the element at this index is not used.
     * @return true if there a are more permutations available, false otherwise.
     */
    static <T extends Comparable<T>> boolean next_permutation( List<T> elements, int first, int last )
    {
        // A translation of the C++ implementation of std::next_permutation.
        // See https://en.cppreference.com/w/cpp/algorithm/next_permutation for
        // the algorithm.
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

    /**
     * Permutes the elements in the passed list in-place.  Each call results in the next
     * permutation in the list.  Does not return duplicates.
     *
     * @param <T> The type of the elements.
     * @param elements A list of elements that is permuted in-place.  Note that this
     * list is required to be sorted in ascending order for the first call.
     * @return true if there a are more permutations available, false otherwise.
     */
    public static <T extends Comparable<T>> boolean next_permutation( List<T> elements )
    {
        return next_permutation( elements, 0, elements.size() );
    }

    private static class itr<T extends Comparable<T>>
        implements Iterator<List<T>>
    {
        private final List<T> _state;

        private boolean _hasNext = true;

        public itr( Collection<T> toPermute )
        {
            _state = new ArrayList<>( toPermute );

            Collections.sort( _state );
        }

        @Override
        public boolean hasNext()
        {
            return _hasNext;
        }

        @Override
        public List<T> next()
        {
            if ( ! _hasNext )
                throw new NoSuchElementException();

            var result =
                    new ArrayList<T>( _state );
            _hasNext =
                    next_permutation( _state );
            return result;
        }
    }

    public static <T extends Comparable<T>>
    Iterator<List<T>> iterator( List<T> list )
    {
        return new itr<>( list );
    }

    public static <T extends Comparable<T>>
    Iterable<List<T>> elements( List<T> list )
    {
        return () -> Permute.iterator( list );
    }

    public static <T extends Comparable<T>>
    Stream<List<T>> stream( List<T> list )
    {
        return StreamSupport.stream(
                elements( list ).spliterator(),
                false );
    }
}
