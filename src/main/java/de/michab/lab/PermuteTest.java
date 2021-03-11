package de.michab.lab;

import java.util.Arrays;
import java.util.List;

class PermuteTest{
    static float compute( List<Float> p )
    {
        return compute(
                p.get( 0 ),
                p.get( 1 ),
                p.get( 2 ),
                p.get( 3 ),
                p.get( 4 ),
                p.get( 5 ) );
    }

    static float compute( float a, float b, float c, float d, float e, float f)
    {
        var result =
                a;
        result -=
                b;
        result *=
                10;
        result *=
                8;
        result /=
                c;
        result +=
                13;
        result -=
                d;
        result *=
                e;
        result +=
                f;

        return result;
    }

    public static void main(String[] args){

        var floats = Arrays.asList(1f,2f,4f,5f,7f,12f);

        int count = 0;
        do
        {
            var value =
                    compute( floats );
            System.out.printf( "%s: %s -> %s%n",
                    count++,
                    floats,
                    value );

            if ( value == 58f )
                return;
        }
        while ( Permute.next_permutation( floats ) );
    }
}
