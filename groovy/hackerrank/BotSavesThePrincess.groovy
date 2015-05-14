import static java.lang.Integer.*
BufferedReader br

if (args.length > 0 ) { br = new BufferedReader(new FileReader(args[0])) }
else { br = new BufferedReader(new InputStreamReader(System.in)) }

def N = br.readLine() as Integer
def (botRow, botCol) = br.readLine().split(" ").collect { parseInt(it) }
def princessRow, princessCol

(0..N-1).each { rownum ->
    def line = br.readLine()
    (0..N-1).each { colnum ->
        if ( line[colnum] == "p") { princessRow = rownum; princessCol = colnum }
    }
}

println "$botRow $botCol $princessRow $princessCol"

if      (botRow > princessRow) println "UP"
else if (botRow < princessRow) println "DOWN"
else if (botCol < princessCol) println "RIGHT"
else if (botCol > princessCol) println "LEFT"