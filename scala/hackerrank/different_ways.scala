object Solution {

    var solutions = Array.ofDim[Int](1000, 1000);
    
    def main(args: Array[String]) = {
        var counter = 0;
        for( line <- io.Source.stdin.getLines) {
            if (counter > 0) {
                var N_K = line.split(" ").map(_.toInt);
                var N = N_K(0); var K = N_K(1);
                println(count(N, K));
            }
            counter = counter + 1;
        }
    }

    def count(N: Int, K: Int): Int = {
        if (N < K)
            throw new IllegalArgumentException("To select K out of N, N cannot be less than K")
        //println(s"$N $K");
        if(K==0 || K==N) {
            //println("= 1");
            return 1;
        }
        if (solutions(N)(math.min(K, N-K)) != 0) {
            def stored = solutions(N)(math.min(K, N-K));
            //println(s"$N $K - Found $stored");
            return stored;
        }
        var result = count(N-1, K-1) + count(N-1, K);
        solutions(N)(math.min(K, N-K)) = result;
        //println(s"$N $K - Calculated $result");
        return result;
    }
}