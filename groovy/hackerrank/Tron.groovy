import static TronBot.CellType.OPEN
import static TronBot.CellType.PLAYER1
import static TronBot.CellType.PLAYER2
import static TronBot.Direction.DOWN
import static TronBot.Direction.LEFT
import static TronBot.Direction.RIGHT
import static TronBot.Direction.UP
import static java.lang.Integer.parseInt

class TronBot {
    static enum CellType {
        OPEN("-"), WALL("#"), PLAYER1("r"), PLAYER2("g")
        def stringValue

        CellType(stringValue) { this.stringValue = stringValue }
        String toString() {stringValue}

        static CellType byStringValue(stringValue) {
            values().find { it.stringValue == stringValue }
        }
    }
    static enum Direction {
        RIGHT(0, 1), LEFT(0, -1), UP(-1, 0), DOWN(1, 0)

        public final int dRow; public final int dCol
        Direction(dRow, dCol) { this.dRow = dRow; this.dCol = dCol}
    }
    static class Cell {
        public int row, col
        public CellType content

        @Override
        int hashCode() {
            return row * 23 + col *17
        }
        @Override
        boolean equals(Object obj) {
            def other = { (Cell) obj }
            return (obj instanceof Cell &&
                    other().row == row &&
                    other().col == col)
        }
        @Override
        String toString() {
            return "$row $col"
        }
        boolean is(CellType type) {
            content.equals(type)
        }

        def isToTheLeftOf(Cell point) { this.col < point.col }
        def isToTheRightOf(Cell point) { this.col > point.col }
        def isBelow(Cell point) { this.row > point.row }
        def isAbove(Cell point) { this.row < point.row }
    }

    def solve(String[] args) {
        BufferedReader br = args.length > 0 ?
                new BufferedReader(new FileReader(args[0])) :
                new BufferedReader(new InputStreamReader(System.in))

        def boardHeight = 15
        def player = CellType.byStringValue(br.readLine())
        def botPosArr  = br.readLine().split(" ").collect { parseInt(it) }
        def player1Pos = new Cell(row: botPosArr[0], col: botPosArr[1], content: PLAYER1)
        def player2Pos = new Cell(row: botPosArr[2], col: botPosArr[3], content: PLAYER2)
        def opponentPos = player.equals(PLAYER1) ? player2Pos : player1Pos
        def playerPos   = player.equals(PLAYER1) ? player1Pos : player2Pos

        int row = -1, col = -1;
        Cell[][] board = (1..boardHeight).collect {
            row++; col = -1
            br.readLine().collect { cellAsString ->
                col++
                new Cell(row: row, col: col, content: CellType.byStringValue(cellAsString)) }
        }

        println nextMove(board, playerPos, opponentPos)
    }

    def nextMove(board, Cell playerPos, Cell opponentPos) {
        if (playerPos.isToTheLeftOf(opponentPos)    && getCell(board, playerPos, RIGHT).is(OPEN)) return RIGHT
        if (playerPos.isToTheRightOf(opponentPos)   && getCell(board, playerPos, LEFT).is(OPEN)) return LEFT
        if (playerPos.isAbove(opponentPos)          && getCell(board, playerPos, DOWN).is(OPEN)) return DOWN
        if (playerPos.isBelow(opponentPos)          && getCell(board, playerPos, UP).is(OPEN)) return UP

        def options = openDirections(playerPos, board)
        options[ (int)(Math.rand() * options.size()) ]
    }

    def getCell(Cell[][] board, playerPos, Direction direction) {
        board[playerPos.row + direction.dRow][playerPos.col + direction.dCol]
    }

    def openDirections(Cell point, board) {
        Direction.values().findAll { getCell(board, point, it).is(OPEN) }
    }

    def getCell(row, col, board) {
        def within = { value, range -> value >= range[0] && value <= range[1] }
        if ( !within(row, [0, board.size() - 1]) || !within(col, [0, board[0].size() - 1]) )
            return null
        return board[row][col]
    }
}

new TronBot().solve(args)