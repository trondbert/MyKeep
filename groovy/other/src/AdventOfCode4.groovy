
def frequency = [:]
('a'..'z').each { frequency[it] = 0 }

long sum = 0

new FileInputStream("./inputAoC4.txt").eachLine { line ->
    ('a'..'z').each { frequency[it] = 0 }
    checksum = line.substring(line.length()-6, line.length()-1)
    encrypted = line.substring(0, line.length()-11).replaceAll("-", "")

    encrypted.each {
        frequency[it] = frequency[it] + 1;
    }
    def sorted = frequency.sort {a,b -> b.value <=> a.value}
    def iter = sorted.iterator()
    def matches =
        (0..4).count { i -> iter.next().key == checksum[i] }
    if (matches == 5) {
        println line
        def m = line =~ /(\d+).{7}$/
        sum += Integer.parseInt(m[0][1])
    }
}

println sum
