package de.michab.lab;

import java.util.ArrayList;
import java.util.List;

public class Permute{
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

    static List<List<Float>> permutations =
            new ArrayList<>();

    static void permute(java.util.List<Float> arr, int k){
        for(int i = k; i < arr.size(); i++){
            java.util.Collections.swap(arr, i, k);
            permute(arr, k+1);
            java.util.Collections.swap(arr, k, i);
        }
        if (k == arr.size() -1){
            System.out.println(java.util.Arrays.toString(arr.toArray()));
            permutations.add( new ArrayList<Float>( arr ) );
        }
    }

    public static void main(String[] args){

        permute(java.util.Arrays.asList(1f,2f,4f,5f,7f,12f), 0);

        int count = 0;
        for ( var c : permutations )
        {
            var value =
                    compute( c );
            System.out.printf( "%s: %s -> %s%n",
                    count++,
                    c,
                    value );

            if ( value == 58f )
                return;
        }
    }
}
