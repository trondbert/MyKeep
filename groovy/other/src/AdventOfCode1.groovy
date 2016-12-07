
public static <E> List elementWise(List<E> a, List<E> b, Closure<? extends E> transform) {
    a.withIndex().collect { it, index -> transform.call(it, b.get(index)) }
}


def input = "L5, R1, R3, L4, R3, R1, L3, L2, R3, L5, L1, L2, R5, L1, R5, R1, L4, R1, R3, L4, L1, R2, R5, R3, R1, R1, L1, R1, L1, L2, L1, R2, L5, L188, L4, R1, R4, L3, R47, R1, L1, R77, R5, L2, R1, L2, R4, L5, L1, R3, R187, L4, L3, L3, R2, L3, L5, L4, L4, R1, R5, L4, L3, L3, L3, L2, L5, R1, L2, R5, L3, L4, R4, L5, R3, R4, L2, L1, L4, R1, L3, R1, R3, L2, R1, R4, R5, L3, R5, R3, L3, R4, L2, L5, L1, L1, R3, R1, L4, R3, R3, L2, R5, R4, R1, R3, L4, R3, R3, L2, L4, L5, R1, L4, L5, R4, L2, L1, L3, L3, L5, R3, L4, L3, R5, R4, R2, L4, R2, R3, L3, R4, L1, L3, R2, R1, R5, L4, L5, L5, R4, L5, L2, L4, R4, R4, R1, L3, L2, L4, R3"


def north = [0,1], south = [0,-1], east = [1,0], west = [-1,0]

def rightTurns = [ (north): east, (east): south, (south): west, (west): north]
def leftTurns =  [ (north): west, (west): south, (south): east, (east): north]

def pos = [0,0]
def dir = north
def steps = input.replaceAll(" ", "").split(",") as List

steps.each { step ->
    if      (step.startsWith("R")) dir = rightTurns[dir]
    else if (step.startsWith("L")) dir =  leftTurns[dir]

    def distance = Integer.valueOf(step.substring(1))

    pos = elementWise( pos, dir.collect{it*distance}, {a, b -> a+b})
}
println pos

//println( elementWise(a, b, { x1, y1 -> x1 * y1}) )
