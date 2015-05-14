import static java.lang.Integer.*

class PacmanBFSBot {

    static enum Cell {
        OPEN("-"), WALL("%"), PACMAN("P"), FOOD(".")
        def stringValue
        Cell(stringValue) { this.stringValue = stringValue }
        String toString() {stringValue}

        static Cell byStringValue(stringValue) {
            values().find { it.stringValue == stringValue }
        }
    }
    static class Point {
        public int row, col
        Point() {}
        Point(row, col) { this.row = row; this.col = col }

        @Override
        int hashCode() {
            return row * 23 + col *17
        }
        @Override
        boolean equals(Object obj) {
            def other = { (Point) obj }
            return (obj instanceof Point &&
                    other().row == row &&
                    other().col == col)
        }
        @Override
        String toString() {
            return "$row $col"
        }
    }

    static class Visit {
        public Point point
        public Visit origin
    }

    def solve(String[] args) {
        BufferedReader br = args.length > 0 ?
                new BufferedReader(new FileReader(args[0])) :
                new BufferedReader(new InputStreamReader(System.in))

        def botPosArr = br.readLine().split(" ").collect { parseInt(it) }
        def botPos =    new Point(row: botPosArr[0], col: botPosArr[1])
        br.readLine() // Food position

        def boardHeight = br.readLine().split(" ")[0] as Integer

        Cell[][] board = (1..boardHeight).collect {
            br.readLine().collect { Cell.byStringValue(it) }
        }

        def result = breadthFirstSearch(board, botPos)
        println result.pointsVisited.size()
        result.pointsVisited.each { println "${it.row} ${it.col}" }

        def trail = []
        def visit = result.visitList[0]
        while (visit != null) {
            trail.push(visit.point)
            visit = visit.origin
        }
        println trail.size() - 1
        trail.reverse().each { println it }
    }

    def breadthFirstSearch(board, botPos) {
        def visitList = []
        visitList.push( new Visit(point: botPos, origin: null))
        def pointsVisited = []
        visit(board, visitList, pointsVisited)
        [visitList: visitList, pointsVisited: pointsVisited]
    }

    def visit(Cell[][] board, visitList, pointsVisited) {
        if (visitList.isEmpty()) return
        def visiting = visitList[0]

        pointsVisited.push(visiting.point)
        if (board[visiting.point.row][visiting.point.col] == Cell.FOOD)
            return

        visitList.remove(0)

        for (neighbor in neighbors(visiting.point, board)) {
            if (    visitList.find { it.point.equals(neighbor) } == null &&
                    pointsVisited.contains(neighbor) == false ) {
                visitList.push(new Visit(point: neighbor, origin: visiting))
            }
        }

        visit(board, visitList, pointsVisited)
    }

    def neighbors(point, board) {
        def candidates = [ getPoint(point.row-1, point.col,   board),
                           getPoint(point.row,   point.col-1, board),
                           getPoint(point.row,   point.col+1, board),
                           getPoint(point.row+1, point.col,   board)
        ]
        candidates.findAll { it != null && board[it.row][it.col] != Cell.WALL }
    }

    def getPoint(row, col, board) {
        def within = { value, range -> value >= range[0] && value <= range[1] }
        if ( !within(row, [0, board.size() - 1]) || !within(col, [0, board[0].size() - 1]) )
            return null
        return new Point(row, col)
    }
}

new PacmanBFSBot().solve(args)
