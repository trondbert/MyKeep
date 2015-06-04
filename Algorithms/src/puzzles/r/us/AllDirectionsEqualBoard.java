package puzzles.r.us;

import static puzzles.r.us.AllDirectionsEqualSum.width;
import static puzzles.r.us.AllDirectionsEqualSum.boardArray;

class AllDirectionsEqualBoard {

    public void reset() {
        for (int x = 0; x < AllDirectionsEqualSum.width; x++) {
            if (AllDirectionsEqualSum.boardArray[x] == null)
                boardArray[x] = new int[width];
            for (int y = 0; y < width; y++) {
                boardArray[x][y] = 0;
            }
        }
    }

    public void set(int x, int y, int number) {
        boardArray[x][y] = number;
    }

    public int get(int x, int y) {
        try {
            return boardArray[x][y];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw e;
        }

    }

    public boolean sumsCorrect() {
        int sum = boardArray[0][0] + boardArray[0][1] + boardArray[0][2] + boardArray[0][3];
        if (boardArray[1][0] + boardArray[1][1] + boardArray[1][2] + boardArray[1][3] != sum) return false;
        if (boardArray[2][0] + boardArray[2][1] + boardArray[2][2] + boardArray[2][3] != sum) return false;
        if (boardArray[3][0] + boardArray[3][1] + boardArray[3][2] + boardArray[3][3] != sum) return false;

        if (boardArray[0][0] + boardArray[1][0] + boardArray[2][0] + boardArray[3][0] != sum) return false;
        if (boardArray[0][1] + boardArray[1][1] + boardArray[2][1] + boardArray[3][1] != sum) return false;
        if (boardArray[0][2] + boardArray[1][2] + boardArray[2][2] + boardArray[3][2] != sum) return false;
        if (boardArray[0][3] + boardArray[1][3] + boardArray[2][3] + boardArray[3][3] != sum) return false;

        if (boardArray[0][0] + boardArray[1][1] + boardArray[2][2] + boardArray[3][3] != sum) return false;
        if (boardArray[0][3] + boardArray[1][2] + boardArray[2][1] + boardArray[3][0] != sum) return false;

        return true;
    }

    @Override
    public String toString() {
        if (boardArray[width - 1] == null) return "(Tom)";

        StringBuilder sb = new StringBuilder();

        for (int     rowY = 0; rowY < width; rowY++) {
            for (int colX = 0; colX < width; colX++) {

                sb.append(boardArray[colX][rowY]);
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}