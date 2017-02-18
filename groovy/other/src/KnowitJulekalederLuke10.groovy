import static Debug.debug

class Debug {
    static boolean DEBUG = true

    static void debug(String msg) {
        if (DEBUG)
            println msg
    }
}

class Thief extends Player {
    Thief() { super(true); }
    Object clone() { return new Thief(); }
    @Override
    Room move(Room room) {
        if (isAlive())
            return room.killGoblins(1)
        else
            return room
    }
    @Override
    String toString() {
        return "THIEF" + (alive ? "" : " DEAD")
    }
}
class Wizard extends Player {
    Wizard() { super(true); }
    Object clone() { return new Wizard(); }
    @Override
    Room move(Room room) {
        if (isAlive()) {
            return room.killGoblins(10)
        }
        else
            return room
    }
    @Override
    String toString() {
        return "WIZARD" + (alive ? "" : " DEAD")
    }
}
class Warrior extends Player {
    Warrior() { super(true); }
    Object clone() { return new Warrior(); }
    @Override
    Room move(Room room) {
        if (isAlive())
            return room.killGoblins(1)
        else
            return room
    }
    @Override
    String toString() {
        return "WARRIOR" + (alive ? "" : " DEAD")
    }
}
class Priest extends Player {
    Priest() { super(true); }
    Object clone() { return new Priest(); }
    @Override
    Room move(Room room) {
        if (isAlive()) {
            def deadBrother = ["warrior", "wizard"].find {
                room.getPlayers().containsKey(it) &&
                !room.getPlayer(it).isAlive() }

            if (deadBrother != null)
                return room.resurrectPlayer(deadBrother)
        }
        return room
    }
    @Override
    String toString() {
        return "PRIEST" + (alive ? "" : " DEAD");
    }
}
class SpentPriest extends Priest {
    @Override
    Room move(Room room) {
        // Cannot do a thing
        return room
    }
    @Override
    String toString() {
        return "PRIEST SPENT" + (alive ? "" : " DEAD");
    }
}

class Room {
    public final int goblinCount
    public final int roomNumber
    private Map<String, Player> players = [:]

    Room(Map players, int goblinCount, int roomNumber) {
        this.players = players;
        this.goblinCount = goblinCount
        this.roomNumber = roomNumber
    }
    Room nextRoom() {
        def nextRoomNumber = roomNumber + 1
        def newPlayers = players.findAll {k,v -> v.isAlive()}.clone() as Map
        if (players["priest"]?.isAlive())
            newPlayers["priest"] = new Priest()
        return new Room(newPlayers, nextRoomNumber, nextRoomNumber)
    }
    Room killGoblins(int count) {
        debug("KILL GOBLINS: " + count)
        return new Room(players.clone() as Map, Math.max(0, goblinCount-count) as int, roomNumber)
    }
    Room goblinAttack() {
        def heroes = players.findAll {k,v -> v.isAlive()}.size()
        if (goblinCount >= 10 * heroes) {
            def killable = ["warrior", "wizard", "priest"].find {
                players.containsKey(it) && getPlayer(it).isAlive() }
            if (killable != null)
                return killPlayer(killable)
        }
        return this
    }
    Player getPlayer(String key) {
        return players[key]
    }
    Room movePlayer(String key) {
        if (players[key] != null) {
            debug(players[key].toString() + ": ")
            return players[key].move(this)
        }
        return this
    }
    Room killPlayer(String key) {
        debug("KILL PLAYER: " + key)
        Map newPlayers = players.clone() as Map
        newPlayers[key] = players[key].die()
        return new Room(newPlayers, goblinCount, roomNumber)
    }
    Room resurrectPlayer(String playerKey) {
        debug("RESURRECT " + playerKey)
        Map newPlayers = players.clone() as Map
        newPlayers[playerKey] = players[playerKey].riseFromTheDead()
        newPlayers["priest"] = new SpentPriest();
        return new Room(newPlayers, goblinCount, roomNumber)
    }
    Map<String, Player> getPlayers() {
        return players.clone() as Map<String, Player>
    }
    int alivePlayersCount() {
        return players.values().findAll { it.isAlive() }.size()
    }
}

abstract class Player {
    protected boolean alive;

    Player(boolean alive) {
        this.alive = alive
    }

    abstract Room move(Room room);

    Player die() {
        Player p = clone() as Player;
        p.alive = false;
        return p
    }
    Player riseFromTheDead() {
        Player p = clone() as Player;
        p.alive = true;
        return p
    }
    boolean isAlive() {
        return alive;
    }
}

static Room visit(Room room) {
    debug("====================")
    debug("VISIT " + room.roomNumber)
    Room visited = room
    while (visited.goblinCount > 0 && visited.alivePlayersCount() > 0) {
        debug(visited.goblinCount + " goblins to go")
        visited     = visited.movePlayer("thief")
        visited     = visited.movePlayer("wizard")
        visited     = visited.movePlayer("warrior")
        visited     = visited.movePlayer("priest")
        if (visited.getPlayer("thief").isAlive() && visited.alivePlayersCount() == 1) {
            debug ("THIEF sneaks!")
            return visited // sneaks to next room
        }
        debug("GOBLINS: ")
        visited     = visited.goblinAttack()
    }
    return visited
}

public static void main(String[] args) {
    def players = [thief: new Thief(), wizard: new Wizard(), warrior: new Warrior(), priest: new Priest()]
    def room = new Room(players, 1, 1)
    def survivorCount = 0

    while (room.roomNumber <= 100) {
        def visited = visit(room)
        survivorCount += visited.goblinCount
        if (visited.alivePlayersCount() == 0)
            break;

        debug("Survivor count: $survivorCount")
        room = visited.nextRoom()
    }
    survivorCount += room.alivePlayersCount()
    if (room.roomNumber == 101)
        survivorCount += 17

    println survivorCount
    println room.getPlayer("thief")?.isAlive()
    println room.getPlayer("wizard")?.isAlive()
    println room.getPlayer("warrior")?.isAlive()
    println room.getPlayer("priest")?.isAlive()
}