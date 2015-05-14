import static java.lang.Math.*
import static java.lang.Integer.*

class BotCleaner {

    static enum Cell {
        CLEAN("-"), DIRTY("+"), BOTDIRTY("B"), BOTCLEAN("b")
        def stringValue
        Cell(stringValue) { this.stringValue = stringValue }
        String toString() {stringValue}
        static boolean isDirty(cell) {
            cell == DIRTY || cell == BOTDIRTY
        }
    }
    static class Point {
        public int row, col
    }


    int boardSize
    def board
    int botRow, botCol

    BotCleaner(boardSize) {
        this.boardSize = boardSize
    }

    def safeSlice(array, start, end) {
        def adjust = { num ->
            num < 0 ? 0 : num >= array.size() ? array.size() - 1 : num
        }
        return array[adjust(start)..adjust(end)]
    }
    def closestCorner = {
        def row = [0, boardSize - 1].min { int cornerRow -> abs(botRow - cornerRow) }
        def col = [0, boardSize - 1].min { int cornerCol -> abs(botCol - cornerCol) }
        return new Point(row: row, col: col)
    }
    def dirtyTowardsRow = {
        int row ->
        def rowsToGo = botRow < row ? board[botRow+1..row] : board[row..botRow-1]
        def dirtyRow = rowsToGo.find { rowIt ->
            rowIt.find { isDirty(it) } != null }
        return dirtyRow != null
    }
    def moveTowardsRow = {
        int row ->
        return (botRow < row) ? "DOWN" : "UP"
    }
    def whereToCleanOnSameRow() {
        def toTheLeft = safeSlice(board[botRow], 0, botCol-1)
        def toTheRight = safeSlice(board[botRow], botCol+1, boardSize-1)
        def bestChoice = null, bestDistance = 1000000
        toTheLeft.eachWithIndex { cell, index ->
            if (isDirty(cell) && bestDistance > index) {
                bestDistance = index; bestChoice = "LEFT"
            }
        }
        toTheRight.eachWithIndex { cell, index ->
            if (isDirty(cell) && bestDistance > (boardSize-1-index)) { bestChoice = "RIGHT"}
        }
        return bestChoice
    }

    def solve(args) {
        BufferedReader br = args.length > 0 ?
                new BufferedReader(new FileReader(args[0])) :
                new BufferedReader(new InputStreamReader(System.in))

        def botPos = br.readLine().split(" ").collect { parseInt(it) }
        botRow = botPos[0]; botCol = botPos[1]

        board = (0..boardSize - 1).collect { rownum ->
            def line = br.readLine()
            (0..boardSize - 1).collect { colnum ->
                line[colnum].equals("d") ? DIRTY : CLEAN
            }
        }

        def move = nextMove()
        if (move != null) println move
    }

    def nextMove() {
        if ( isDirty(board[botRow][botCol]) ) {
            return "CLEAN"
        }
        def corner = closestCorner()
        if (botRow != corner.row && dirtyTowardsRow(corner.row)) {
            return moveTowardsRow(corner.row)
        }
        def cleanOnSameRow = whereToCleanOnSameRow()
        if ( cleanOnSameRow != null) {
            return cleanOnSameRow
        }
        if ( botRow > 0 && dirtyTowardsRow(0) ) {
            return "UP"
        }
        if ( botRow < boardSize-1 && dirtyTowardsRow(boardSize-1) ) {
            return "DOWN"
        }
        return null;
    }
}

new BotCleaner(5).solve(args)