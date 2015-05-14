import static java.lang.Integer.parseInt

BufferedReader br = args.length > 0 ?
        new BufferedReader(new FileReader(args[0])) :
        new BufferedReader(new InputStreamReader(System.in))

def boardSize = 5
def botPos = br.readLine().split(" ").collect { parseInt(it) }
botRow = botPos[0]; botCol = botPos[1]

def board = (0..boardSize - 1).collect { rownum ->
    def line = br.readLine()
    (0..boardSize - 1).collect { colnum ->
        line[colnum].equals("d") ? DIRTY : CLEAN
    }
}
board[botRow][botCol] = board[botRow][botCol] == DIRTY ? BOTDIRTY : BOTCLEAN

def cleaner = new BotCleaner(boardSize)
cleaner.botRow = botRow
cleaner.botCol = botCol
cleaner.board = board


def move
def moveIndex = 1
while ((move  = cleaner.nextMove()) != null) {
    if (moveIndex < 60) {
        def filenumber = "${moveIndex++}".padLeft(3, "0");
        BufferedWriter bw = new BufferedWriter(new FileWriter("./botStage${filenumber}.txt"))
        board.each { row -> bw.writeLine( row.join(" ")) }
        bw.flush(); bw.close()
    } else {
        break;
    }

    if (move.equals("CLEAN")) board[botRow][botCol] = BOTCLEAN
    else {
        board[botRow][botCol] = isDirty(board[botRow][botCol]) ? DIRTY : CLEAN
    }

    if (move.equals("LEFT")) botCol--; if (move.equals("RIGHT")) botCol++
    if (move.equals("UP")) botRow--;   if (move.equals("DOWN")) botRow++

    if (!move.equals("CLEAN"))
        board[botRow][botCol] = isDirty(board[botRow][botCol]) ? BOTDIRTY : BOTCLEAN

    cleaner.botRow = botRow
    cleaner.botCol = botCol
}
