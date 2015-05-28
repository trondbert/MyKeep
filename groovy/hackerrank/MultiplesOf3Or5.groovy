// The trick is to decide when to divide, so that the partial results are always whole numbers
BufferedReader br = new BufferedReader(new InputStreamReader(System.in))
long T = br.readLine() as Long

long threes, fives, fifteens, maxNumber
for (int caseNo = 0; caseNo < T; caseNo++) {
    maxNumber = (br.readLine() as Long) - 1
    fifteens = maxNumber.intdiv(15)
    threes = maxNumber.intdiv(3)
    fives = maxNumber.intdiv(5)

    println (
            ( threes % 2 != 0     ? threes * ((3 + threes*3).intdiv(2))
                    : (threes.intdiv(2)) * (3 + threes*3))
                    - ( fifteens % 2 != 0   ? fifteens * ((15 + fifteens*15).intdiv(2))
                    : (fifteens.intdiv(2)) * (15 + fifteens*15) )
                    + ( fives % 2 != 0      ? fives * ((5 + fives*5).intdiv(2))
                    : (fives.intdiv(2)) * (5 + fives*5) )
    )
}
