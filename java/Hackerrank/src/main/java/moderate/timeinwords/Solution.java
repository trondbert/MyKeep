package moderate.timeinwords;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.asList;

public class Solution {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    Map<Integer, String> tensMap = new HashMap<>();

    enum Numerals {
        zero, one, two, three, four, five, six, seven, eight, nine, ten,
        eleven, twelve, thirteen, fourteen, fifteen, sixteen, eighteen, nineteen;

        static Numerals valueOf(int ordinal) {
            return asList(Numerals.values()).stream()
                .filter(num -> ordinal == num.ordinal()).findFirst().get();
        }
    }

    Solution() {
        tensMap.put(20, "twenty");
        tensMap.put(30, "thirty");
        tensMap.put(40, "forty");
        tensMap.put(50, "fifty");
    }

    String solve() throws IOException {

        String hours    = br.readLine();
        String minutes  = br.readLine();
        return timeInWords(hours, minutes);
    }

    String timeInWords(final String hoursStr, final String minutesStr) {
        int hours = Integer.parseInt(hoursStr);
        int minutes = Integer.parseInt(minutesStr);
        if (minutes == 0) {
            return Numerals.valueOf(hours).name() + " o' clock";
        }

        boolean afterHour = minutes <= 30;
        String hoursInLetters = Numerals.valueOf(afterHour ? hours : hours + 1).name();
        String minutesToOrPast = minutesInWords(afterHour ? minutes : 60 - minutes);

        return String.format("%s %s %s",
                minutesToOrPast,
                afterHour ? "past" : "to",
                hoursInLetters);
    }

    private String minutesInWords(int minutes) {

        if (minutes == 30)
            return "half";
        if (minutes == 15)
            return "quarter";

        String countNoun = "minute" + (minutes > 1 ? "s" : "");
        return minutesToString(minutes) + " " + countNoun;
    }

    private String minutesToString(int minutes) {
        int tens = minutes / 10;
        if (tens >= 2) {
            int leastSignificantDigit = minutes % 10;
            Numerals minutesOnes = Numerals.valueOf(leastSignificantDigit);
            return (leastSignificantDigit != 0 ?
                    tensMap.get(tens * 10) + " " + minutesOnes.name() :
                    tensMap.get(tens * 10));
        }
        else {
            return Numerals.valueOf(minutes).name();
        }
    }

    public static void main(String[] args) {
        try {
            String solved = new Solution().solve();
            System.out.println(solved);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
