import static java.lang.Integer.parseInt

class PacmanAStarBot {

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

    static class Visit implements Comparable {
        public Point point
        public Visit origin
        public int cost

        Visit(Point point, Visit origin, Point startPos, Point food) {
            this.point = point;
            this.origin = origin;
            this.cost = (origin ? origin.cost : 0) +
                    (point.equals(food) ? 0 : 1)
            manhattanDistance(point, food)
        }

        def manhattanDistance(Point point1, Point point2) {
            Math.abs(point1.row - point2.row) + Math.abs(point1.col - point2.col)
        }

        @Override
        int compareTo(Object o) {
            return cost - ((Visit)o).cost
        }
    }

    def solve(String[] args) {
        BufferedReader br = args.length > 0 ?
                new BufferedReader(new FileReader(args[0])) :
                new BufferedReader(new InputStreamReader(System.in))

        def botPosArr = br.readLine().split(" ").collect { parseInt(it) }
        def botPos =    new Point(row: botPosArr[0], col: botPosArr[1])

        def foodPosArr = br.readLine().split(" ").collect { parseInt(it) }
        def foodPos =   new Point(row: foodPosArr[0], col: foodPosArr[1])

        def boardHeight = br.readLine().split(" ")[0] as Integer

        Cell[][] board = (1..boardHeight).collect {
            br.readLine().collect { Cell.byStringValue(it) }
        }

        def result = aStarSearch(board, botPos, foodPos)

        def trail = []
        def visit = result.visitList[0]
        while (visit != null) {
            trail.push(visit.point)
            visit = visit.origin
        }
        println trail.size()
        trail.reverse().each { println it }
    }

    def aStarSearch(board, botPos, foodPos) {
        def visitList = []
        pushAndSort(visitList, new Visit(botPos, null, botPos, foodPos))
        def pointsVisited = []
        visit(board, visitList, pointsVisited, botPos, foodPos)
        [visitList: visitList, pointsVisited: pointsVisited]
    }

    def visit(Cell[][] board, visitList, pointsVisited, startPos, endPos) {
        if (visitList.isEmpty()) return
        def visiting = visitList[0]

        pointsVisited.push(visiting.point)
        if (board[visiting.point.row][visiting.point.col] == Cell.FOOD)
            return

        visitList.remove(0)

        for (neighbor in neighbors(visiting.point, board)) {
            if (    visitList.find { it.point.equals(neighbor) } == null &&
                    pointsVisited.contains(neighbor) == false ) {
                def visit = new Visit(neighbor, visiting, startPos, endPos)
                pushAndSort(visitList, visit)
            }
        }

        visit(board, visitList, pointsVisited, startPos, endPos)
    }

    void pushAndSort(visitList, Visit visit) {
        visitList.push(visit)
        Collections.sort(visitList)
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

new PacmanAStarBot().solve(args)