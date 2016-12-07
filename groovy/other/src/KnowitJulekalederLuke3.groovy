def content = new URL("http://pastebin.com/raw/e0bE4naA").getText()

def lines = content.split("\\r\\n")

Map hatedOnesByHater = lines
        .findAll { it.contains(" hates ") }
        .inject([:]) { map, line ->
            def (hater, _, hatedOne) = line.split()
            map.get(hater, []).add(hatedOne)
            map
}

def hatedFriendsByHater = [:]
lines.findAll { it.startsWith("friends") }
        .each { line ->
            def (_, friendA, friendB) = line.split()
            def friends = [friendA, friendB]
            (0..1).each { i ->
                if (hatedOnesByHater.get(friends[i], []).contains(friends[1-i]) &&
                    ! hatedOnesByHater.get(friends[1-i], []).contains(friends[i]) ) {

                    hatedFriendsByHater.get(friends[i], [] as Set).add(friends[1-i])
}}}

println (hatedFriendsByHater.max {it.getValue().size()}.key)
