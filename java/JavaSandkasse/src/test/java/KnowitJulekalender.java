    import javax.jnlp.IntegrationService;
    import java.io.File;
    import java.io.FileNotFoundException;
    import java.util.*;

    public class KnowitJulekalender {

        public static void main(String[] args) {

            new KnowitJulekalender().solve2();
            /*
            new KnowitJulekalender().solve2Basic();

             */
        }

        private void solve1() {
            for (int i = 0; i < Integer.MAX_VALUE; i += 2) {
                if (String.valueOf(i).endsWith("6")) {
                    String string = String.valueOf(i);
                    String newString = string.substring(string.length()-1) + string.substring(0, string.length() - 1);
                    int sixFirst = Integer.valueOf(newString);
                    System.out.println(i + " " + sixFirst);
                    if (sixFirst == i * 4)
                        break;
                }
            }
        }

        private void solve2() {
            long start = System.nanoTime();
            long lastNumber = 2;
            long previousNumber = 1;
            long tempLast = 0;
            long sum = 0;
            do {
                sum += lastNumber;
                tempLast = lastNumber;
                lastNumber = 2*previousNumber + 3*lastNumber;
                previousNumber = previousNumber + 2*tempLast;
            } while (lastNumber < 1_000_000_000_000L);

            System.out.println(sum);
            System.out.println((System.nanoTime() - start) + " nanos");
            // 1 2 3 5 8 13 21 34 55 89 144

            // 3*1 + 5*1  = (p + c) + (p+c)+c = 2p+3c, p <- p+2c, c <- 2p+3c
            // 5+8 + 8+5+8

            // 1 3 13 55
        }

        private void solve2Basic() {
            long start = System.nanoTime();
            long lastNumber = 2;
            long previousNumber = 1;
            long temp = 0;
            long sum = 0;
            do {
                if (lastNumber % 2 == 0)
                    sum += lastNumber;

                temp = lastNumber;
                lastNumber = previousNumber + lastNumber;
                previousNumber = temp;
            } while (lastNumber < 1_000_000_000_000L);

            System.out.println(sum);
            System.out.println((System.nanoTime() - start) + " nanos");
            // 1 2 3 5 8 13 21 34 55 89 144

            // 3*1 + 5*1  = (p + c) + (p+c)+c = 2p+3c, p <- p+2c, c <- 2+p+3c
            // 5+8 + 8+5+8

            // 1 3 13 55
        }

    }
