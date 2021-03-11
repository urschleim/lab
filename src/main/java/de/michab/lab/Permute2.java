package org.jdesktop.beans;

public class Permute2{
//    template<typename Iter>
//    bool next_permutation(Iter first, Iter last)
//    {
//        if (first == last)
//            return false;
//        Iter i = first;
//        ++i;
//        if (i == last)
//            return false;
//        i = last;
//        --i;
//
//        for (;;)
//        {
//            Iter ii = i;
//            --i;
//            if (*i < *ii)
//            {
//                Iter j = last;
//                while (!(*i < *--j))
//                {
//                }
//                std::iter_swap(i, j);
//                std::reverse(ii, last);
//                return true;
//            }
//            if (i == first)
//            {
//                std::reverse(first, last);
//                return false;
//            }
//        }
//    }

//    int main()
//    {
//        char xs[] = "mcbinz";
//        do
//        {
//            std::puts(xs);
//        } while (Mbient::TokenMaster::next_permutation(xs, xs + sizeof(xs) - 1));
//        return 0;
//    }

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
