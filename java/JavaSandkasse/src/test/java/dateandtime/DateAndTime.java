package dateandtime;

import org.junit.Test;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DateAndTime {

    @Test
    public void addMonth() throws Exception {

        LocalDate jan31_2016 = LocalDate.of(2016, 1, 31);
        LocalDate mar31_2016 = LocalDate.of(2016, 3, 31);
        LocalDate jan31_2017 = LocalDate.of(2017, 1, 31);

        assertThat(jan31_2016.plusMonths(1)).isEqualTo(LocalDate.of(2016, 2, 29));
        assertThat(mar31_2016.plusMonths(1)).isEqualTo(LocalDate.of(2016, 2, 29));
        assertThat(jan31_2017.plusMonths(1)).isEqualTo(LocalDate.of(2017, 2, 28));
    }

    @Test
    public void testTriplets() {
        ArrayList<List<Integer>> lists = new ArrayList<>();
        lists.add(new ArrayList<Integer>() {{ add(1); }});
        lists.add(new ArrayList<Integer>() {{ add(2); }});
        lists.add(new ArrayList<Integer>() {{ add(4); }});
        lists.add(new ArrayList<Integer>() {{ add(5); }});
        lists.add(new ArrayList<Integer>() {{ add(7); }});
        lists.add(new ArrayList<Integer>() {{ add(8); }});
        lists.add(new ArrayList<Integer>() {{ add(10); }});

        List<List<Integer>> triplets = triplets(lists, 3);
        System.out.println(triplets);
    }

    private List<List<Integer>> triplets(List<List<Integer>> numbers, int difference) {
        List<List<Integer>> triplets = new ArrayList<List<Integer>>();
        for (int i = 0; i < numbers.size(); i++) {
            int j = i-1;
            Integer current = numbers.get(i).get(0);
            while (j >= 0) {
                if (numbers.get(j).get(0) == current - difference) {
                    numbers.get(i).addAll(numbers.get(j));
                    if (numbers.get(i).size() == 3) {
                        triplets.add(numbers.get(i));

                        ArrayList<Integer> newList = new ArrayList<>();
                        newList.add(numbers.get(i).get(0));
                        newList.add(numbers.get(i).get(1));
                        numbers.set(i, newList);
                        break;
                    }
                }
                if (numbers.get(j).get(0) < current - difference)
                    break;
                j--;
            }
        }
        return triplets;
    }

    @Test
    public void kaprekarTest() {
        //assertThat(kaprekar(1, 99999)).containsExactly(1, 9, 45);

        assertTrue(kaprekar(99999L));
    }

    Function<String, String> zeroPadIfOneChar = (String s) -> s.length() == 1 ? "0" + s : s;
    private List<Integer> kaprekar(int lowerBound, int upperBound) {
        List<Integer> result = new ArrayList<>();

        List<Integer> ds = IntStream.rangeClosed(lowerBound, upperBound)
                .filter(this::kaprekar)
                .boxed().collect(Collectors.toList());

        for (int num = lowerBound; num <= upperBound; num++) {
            if (kaprekar(num))
                result.add(num);
        }

        return result;
    }

    private boolean kaprekar(long num) {
        long square = num * num;
        String squareString = zeroPadIfOneChar.apply(String.valueOf(square));
        int squareDigits = squareString.length();
        int rightHand = Integer.parseInt(squareString.substring(squareDigits / 2));
        int leftHand =  Integer.parseInt(squareString.substring(0, squareDigits / 2));
        boolean yes = false;
        if (leftHand + rightHand == num)
            yes = true;
        return yes;
    }

    @Test
    public void convertTest() {
        assertEquals("00:00:00", convertToMilitaryTime("12:00:00AM"));
        assertEquals("12:00:00", convertToMilitaryTime("12:00:00PM"));

        assertEquals("04:12:45", convertToMilitaryTime("04:12:45AM"));
        assertEquals("11:02:43", convertToMilitaryTime("11:02:43AM"));
        assertEquals("00:02:43", convertToMilitaryTime("12:02:43AM"));

        assertEquals("12:02:13", convertToMilitaryTime("12:02:13PM"));
        assertEquals("13:02:13", convertToMilitaryTime("01:02:13PM"));
    }

    public String convertToMilitaryTime(final String twelveHourTime) {
        String hoursPart = twelveHourTime.substring(0, 2);
        boolean am = twelveHourTime.substring(8,10).equals("AM");
        String mmss = twelveHourTime.substring(2,8);
        int hours = hoursPart.equals("12") ? 0 : Integer.parseInt(hoursPart);
        if (am) {
            return (hours < 10 ? "0" + hours : hours) + mmss;
        } else {
            return (hours + 12) + mmss;
        }
    }

    @Test
    public void testEncrypt() {
        String input = "haveaniceday";
        // have
        // anic
        // eday rows <= cols
        // hae and via ecy


        Map<String, Integer> dimensions = calcDimensions(12);
        assertThat(dimensions.get("rows")).isEqualTo(3);
        assertThat(dimensions.get("columns")).isEqualTo(4);

        dimensions = calcDimensions(13);
        assertThat(dimensions.get("rows")).isEqualTo(4);
        assertThat(dimensions.get("columns")).isEqualTo(4);

        assertThat(encrypt(input)).isEqualTo(Arrays.asList("hae", "and", "via", "ecy"));
    }

    private List<String> encrypt(String input) {
        Map<String, Integer> dimensions = calcDimensions(input.length());
        final Integer rowCount = dimensions.get("rows");
        final Integer colCount = dimensions.get("columns");


        List<String> rows = new ArrayList<>();
        for (int row = 0; row < rowCount; row++) {
            rows.add(input.substring(row * colCount, Math.min((row+1)*colCount, input.length())));
        }

        List<String> cols = new ArrayList<>();
        for (int colIndex = 0; colIndex < colCount; colIndex++) {
            StringBuffer buffer = new StringBuffer("");
            for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
                if (rows.get(rowIndex).length() > colIndex)
                    buffer.append(rows.get(rowIndex).charAt(colIndex));
            }
            cols.add(buffer.toString());
        }

        return cols;
    }

    private Map<String, Integer> calcDimensions(int inputLength) {
        int columns = (int) Math.ceil(Math.sqrt(inputLength));
        int rows;
        for (rows = 1; rows <= columns; rows++) {
            if (rows * columns >= inputLength)
                break;
        }
        HashMap<String, Integer> dimensions = new HashMap<>();
        dimensions.put("rows", rows);
        dimensions.put("columns", columns);
        return dimensions;
    }
}

