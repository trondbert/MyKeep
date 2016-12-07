
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

import static java.lang.Math.ceil

def input = Files.readAllLines(Paths.get("./luke5.input"), StandardCharsets.UTF_8).get(0)

def numbers = input.replaceAll("[\\[\\] ]","").split(",")
        .collect { it.replaceAll("IX", "9").replaceAll("IV", "4")
                     .replaceAll("I", "1").replaceAll("V","5").replaceAll("X","a") }
        .collect {
            it.split("").collect { Integer.parseInt(it, 16)}
        }
def length = numbers.size()
def result = (0..(length /2)).collect {
    def numeric = (numbers.get(it) + numbers.get(length - 1 - it)).sum()
    (char)(64 + numeric)
}

println result.subList(0, (int) ceil(length / 2.0)).join("").toLowerCase()

