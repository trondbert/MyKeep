import static PacmanDFSBot.*

def testIt() {
    testNeighbors()
    testNeighborsAll()
    testNeighborsNone()
}

def testNeighbors() {
    def board = [
            [ Cell.WALL, Cell.OPEN,   Cell.OPEN ],
            [ Cell.WALL, Cell.PACMAN, Cell.OPEN ]
    ]
    def botPos = new Point(1, 1)
    def neighbors = new PacmanDFSBot().neighbors(botPos, board)

    assert neighbors == [new Point(0, 1), new Point(1, 2)]
}

def testNeighborsAll() {
    def board = [
            [ Cell.WALL, Cell.OPEN,   Cell.OPEN ],
            [ Cell.OPEN, Cell.PACMAN, Cell.OPEN ],
            [ Cell.WALL, Cell.OPEN, Cell.OPEN ]
    ]
    def botPos = new Point(1, 1)
    def neighbors = new PacmanDFSBot().neighbors(botPos, board)

    assert neighbors == [new Point(0, 1), new Point(1, 0), new Point(1, 2), new Point(2, 1)]
}

def testNeighborsNone() {
    def board = [
            [ Cell.WALL, Cell.WALL,   Cell.OPEN ],
            [ Cell.WALL, Cell.PACMAN, Cell.WALL ],
            [ Cell.WALL, Cell.WALL, Cell.OPEN ]
    ]
    def botPos = new Point(1, 1)
    def neighbors = new PacmanDFSBot().neighbors(botPos, board)

    assert neighbors == []
}

testIt()

new PacmanDFSBot().solve(["./PacmanDFS.input"] as String[])

