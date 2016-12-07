package hackerrank.moderate.battleships1p;

import static java.lang.Integer.parseInt;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

/**
 * @author trond
 */
public class BattleshipsGame {

    private static String[] hitShipSigns = { " ", "!", "\"", "#", "$", "%", "&", "/" };

    public static void main(String[] args) {
        final String[][] cleanField = setupField();

        Solution solution = new Solution();
        List<Supplier<String>> tricks = Arrays.asList(solution::randomCandidate, solution::bestCandidate);

        boolean interactive = false;
        for (Supplier<String> trick : tricks) {
            String[][] field = copy(cleanField);
            solve(field, solution, trick, interactive);
        }
    }

    private static String[][] copy(final String[][] field) {
        String[][] strings = new String[10][10];
        for (int i = 0; i < 10; i++) {
            System.arraycopy(field[i], 0, strings[i], 0, 10);
        }
        return strings;
    }

    private static void solve(final String[][] field, final Solution solution, final Supplier<String> findCandidateWithoutHit,
                              final boolean interactive) {
        String solutionStr;
        int tries = 0;

        while (true) {
            if (interactive)
                printFieldAll(field);

            StringBuilder sb = new StringBuilder();
            for (String[] row : field) {
                for (String cell : row) {
                    sb.append(cell.replaceAll("^\\d$", "-").replaceAll("\\d", ""));
                }
                sb.append("\n");
            }
            final String input = sb.toString();

            if (interactive) {
                System.out.println();
                System.out.println(input);
            }

            solution.reset(input);
            solutionStr = solution.nextMove(findCandidateWithoutHit);
            tries++;
            if (interactive)
                System.out.println(solutionStr);

            updateField(field, solutionStr);
            if (!matches(field, "^\\d$")) {
                System.out.println("You did it in " + tries + " moves!");
                if (!interactive) {
                    break;
                }
            }

            if (interactive) {
                pause();
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        }
    }

    private static void printFieldAll(final String[][] field) {
        for (String[] row : field) {
            for (String cell : row) {
                if (cell.matches("^\\d.$"))
                    System.out.print(hitShipSigns[parseInt(cell.substring(0, 1))]);
                else
                    System.out.print(cell);
            }
            System.out.println();
        }
    }

    private static void updateField(final String[][] field, final String solution) {
        if (solution == null)
            return;

        final String[] split = solution.split(" ");
        int row = parseInt(split[0]);
        int col = parseInt(split[1]);

        String hitCell = field[row][col];
        if (hitCell.matches("^\\d$")) {
            field[row][col] = hitCell + "h";
            if (!exists(field, hitCell)) {
                replaceAll(field, hitCell + ".*", hitCell + "d");
            }
        }
        else if (field[row][col].equals("-")) {
            field[row][col] = "m";
        }
    }

    private static void replaceAll(final String[][] field, final String s, final String s1) {
        int rowNo = 0;
        for (String[] row : field) {
            int colNo = 0;
            for (String cell : row) {
                field[rowNo][colNo] = cell.replaceAll(s, s1);
                colNo++;
            }
            rowNo++;
        }
    }

    private static boolean exists(final String[][] field, final String s) {
        for (String[] row : field) {
            for (String cell : row) {
                if (cell.equals(s))
                    return true;
            }
        }
        return false;
    }

    private static boolean matches(final String[][] field, final String s) {
        for (String[] row : field) {
            for (String cell : row) {
                if (cell.matches(s))
                    return true;
            }
        }
        return false;
    }

    private static void pause() {
        final InputStreamReader inputStreamReader = new InputStreamReader(System.in);
        try {
            inputStreamReader.read();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String[][] setupField() {
        String[][] field = new String[10][10];
        for (String[] strings : field) {
            Arrays.fill(strings, "-");
        }

        List<Integer> sizes = Arrays.asList(5, 4, 3, 2, 2, 1, 1);

        int shipNumber = 1;
        for (Integer size : sizes) {
            if (Math.random() > 0.85) {
                continue;
            }
            while (true) {
                final Random random = new Random();

                int row = random.nextInt(10);
                int col = random.nextInt(10);

                int dx = random.nextInt(10) > 5? 0 : 1;
                int dy = 1 - dx;
                int i;
                for (i = 0; i < size; i++) {
                    int rowI = row + dy * i;
                    int colI = col + dx * i;
                    if (rowI < 0 || rowI > 9 || colI < 0 || colI > 9)
                        break;
                    if (!field[rowI][colI].equals("-"))
                        break;
                }
                if (i == size) {
                    for (int j = 0; j < size; j++) {
                        int rowJ = row + dy * j;
                        int colJ = col + dx * j;
                        field[rowJ][colJ] = "" + shipNumber;
                    }
                    break;
                }
            }
            shipNumber++;
        }
        return field;
    }

}
