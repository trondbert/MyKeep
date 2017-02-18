def ladders = [3: 17, 8: 10, 15:44, 22:5, 39:56, 49:75, 62:45, 64:19, 65:73, 80:12, 87:79]

def br = new BufferedReader(new InputStreamReader(new URL("http://pastebin.com/raw/dJ7cT4AF").openStream()))

def pos = new Integer[1337]
(0..1336).each { pos[it] = 1 }

String line
def player = 0
def ladderUse = 0
while ((line = br.readLine()) != null) {
    def newPos = pos[player] + Integer.valueOf(line)
    if (newPos > 90)
        newPos = pos[player]
    else if (ladders[newPos] != null) {
        newPos = ladders[newPos]
        ladderUse++
    }
    pos[player] = newPos

    if (pos[player] == 90) {
        def result = (player+1) * ladderUse
        println result
        break
    }
    player = (player + 1) % 1337
}

