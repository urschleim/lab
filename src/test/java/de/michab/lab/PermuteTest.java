package de.michab.lab;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

public class PermuteTest
{
    private int factorial( int v )
    {
        if ( v <= 1 )
            return 1;

        return v * factorial( v-1 );
    }


    @Test
    public void testFactorial()
    {
        assertEquals( 1, factorial( 0 ) );
        assertEquals( 1, factorial( 1 ) );
        assertEquals( 2, factorial( 2 ) );
        assertEquals( 6, factorial( 3 ) );
        assertEquals( 6, factorial( 3 ) );
        assertEquals( 3628800, factorial( 10 ) );
    }

    @Test
    public void testBasicA()
    {
        int count = 0;

        char[] xs = "b".toCharArray();

        do
        {
            count++;
        } while ( Permute.next_permutation( xs, 0, xs.length ) );

        assertEquals( 1, count );

        count = 0;

        do
        {
            count++;
        } while ( Permute.next_permutation( xs ) );

        assertEquals( 1, count );
    }

    @Test
    public void testBasic()
    {
        int count = 0;

        List<Character> xs = Arrays.asList( 'b' );

        do
        {
            count++;
        } while ( Permute.next_permutation( xs, 0, xs.size() ) );

        assertEquals( 1, count );

        count = 0;

        do
        {
            count++;
        } while ( Permute.next_permutation( xs ) );

        assertEquals( 1, count );
    }

    @Test
    public void test4A()
    {
        int count = 0;

        char[] xs = "binz".toCharArray();

        int expected = factorial( xs.length );

        do
        {
            count++;
        } while ( Permute.next_permutation( xs, 0, xs.length ) );

        assertEquals( expected, count );

        count = 0;

        do
        {
            count++;
        } while ( Permute.next_permutation( xs ) );

        assertEquals( expected, count );
    }

    @Test
    public void test4()
    {
        int count = 0;

        List<Character> xs = Arrays.asList( 'b', 'i', 'n', 'z' );

        int expected = factorial( xs.size() );

        do
        {
            count++;
        } while ( Permute.next_permutation( xs, 0, xs.size() ) );

        assertEquals( expected, count );

        count = 0;

        do
        {
            count++;
        } while ( Permute.next_permutation( xs ) );

        assertEquals( expected, count );
    }

    @Test
    public void testEmptyInput()
    {
        int count = 0;

        List<Character> empty = Collections.emptyList();

        do
        {
            count++;
        } while ( Permute.next_permutation( empty, 0, empty.size() ) );

        assertEquals( 1, count );

        count = 0;

        do
        {
            count++;
        } while ( Permute.next_permutation( empty ) );

        assertEquals( 1, count );
    }

    @Test
    public void testIterator()
    {
        var itr = Permute.iterator( Arrays.asList( 'm' ) );

        int count = 0;
        while ( itr.hasNext() )
        {
            count++;
            var next = itr.next();
            assertEquals( 'm', (char)next.get( 0 ) );
        }
        assertEquals( 1, count );
    }

//    @Test
//    public void testIteratorEmpty()
//    {
//        var itr = Permute.iterator(
//                Collections.emptyList() );
//
//        int count = 0;
//        while ( itr.hasNext() )
//        {
//            count++;
//            var next = itr.next();
//            assertEquals( 0, next.size() );
//        }
//        assertEquals( 1, count );
//    }

    @Test
    public void testIteratorOverrun()
    {
        var itr = Permute.iterator( Arrays.asList( 'm' ) );
        // Read the single element.
        itr.next();
        try
        {
            itr.next();
            fail();
        }
        catch ( NoSuchElementException expected )
        {
        }
    }

    @Test
    public void testIteratorNull()
    {
        try
        {
            Permute.iterator( null );
        }
        catch ( NullPointerException expected )
        {
        }
    }

    @Test
    public void testIteratorSorted()
    {
        var values = Arrays.asList( 1,2,3 );
        var itr = Permute.iterator( values );

        int count = 0;
        while ( itr.hasNext() )
        {
            count++;
            itr.next();
        }

        assertEquals( factorial( values.size() ), count );
    }

    @Test
    public void testIteratorUnsorted()
    {
        var values = Arrays.asList( 3,2,1 );
        var itr = Permute.iterator( values );

        int count = 0;
        while ( itr.hasNext() )
        {
            count++;
            itr.next();
        }

        assertEquals( factorial( values.size() ), count );
    }

    @Test
    public void testIterator11()
    {
        // Duplicate.
        var values = Arrays.asList( 1,1 );
        var itr = Permute.iterator( values );

        int count = 0;
        while ( itr.hasNext() )
        {
            count++;
            itr.next();
        }

        assertEquals( factorial( 1 ), count );
    }

    @Test
    public void testIterable()
    {
        var values = Arrays.asList( 1,2,3 );

        int count = 0;

        for ( var c : Permute.elements( values ) )
            count++;

        assertEquals( factorial( values.size() ), count );
    }

    @Test
    public void testStream()
    {
        var values = Arrays.asList( 1,2,3,4,5,6,7,8,9 );

        assertEquals(
                factorial( values.size() ),
                Permute.stream( values ).count() );
    }
}
