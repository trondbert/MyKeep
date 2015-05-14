class MazeEscaper {

    char WALL = '#'
    char OPEN = '-'

    def solve(args) {
        BufferedReader br = args.length > 0 ?
                new BufferedReader(new FileReader(args[0])) :
                new BufferedReader(new InputStreamReader(System.in))

        def botNumber = br.readLine() as Integer
        def board = (0..2).collect { br.readLine().toCharArray() }

        def move = nextMove(board, botNumber)
        if (move != null) println move
    }

    def nextMove(board, botNumber) {
        def decisionMap = ["DOWN": "LEFT", "LEFT": "UP", "UP": "RIGHT", "RIGHT": "DOWN",
                           "DOWNLEFT": "LEFT", "UPLEFT": "UP", "UPRIGHT": "UP", "DOWNRIGHT": "DOWN"]

        def walls = []
        board.eachWithIndex { rowArray, row ->
            rowArray.eachWithIndex { cell, col ->
                //println "CELL $cell"
                if (cell == WALL) {
                    if (row != 1 && col != 1)
                        walls.push ((row == 0 ? "UP" : "DOWN") + (col == 2 ? "RIGHT" : "LEFT"))
                    else
                        walls.push (col == 1 ? (row == 0 ? "UP" : "DOWN") :
                                               (col == 2 ? "RIGHT" : "LEFT")
                        )
                }
            }
        }
        //println walls
        for (keyval in decisionMap) {
            if ( walls.find{ it.equals(keyval.key) } != null && walls.find{ it == keyval.value } == null )
                return keyval.value
        }
        return "DOWN"
    }

    def nextMoveRandom(board, botNumber) {
        def directions = ["UP", "DOWN", "LEFT", "RIGHT"]
        def boardDirections = [board[0][1], board[2][1], board[1][0], board[1][2]]
        def exit = boardDirections.indexOf('e')
        if (exit != -1)
            return directions[exit]

        def options = []
        directions.eachWithIndex { val, idx -> if (boardDirections[idx] != '#') options.push(val) }

        return options[ ((int)(Math.random() * options.size())) ]
    }
}

new MazeEscaper().solve(args)