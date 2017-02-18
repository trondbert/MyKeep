def content = new URL("http://pastebin.com/raw/BZrAMcN2").getText()

def lines = content.split("\\r?\\n")

println lines
def normalized = lines.collect { line ->
    line.replaceAll("(\\d+) meters south", "-\$1 meters north")
        .replaceAll("(\\d+) meters east", "-\$1 meters west")

}

println "north " + normalized.findAll { it.matches(".*north.*") }.collect { line ->
    line.replaceAll(".*?(-?\\d+).*", "\$1")
}.collect{ Long.parseLong(it) }.sum()

println "west " + normalized.findAll { it.matches(".*west.*") }.collect { line ->
    line.replaceAll(".*?(-?\\d+).*", "\$1")
}.collect{ Long.parseLong(it) }.sum()

