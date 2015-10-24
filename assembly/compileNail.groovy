argsRegisters = ["rdi", "rsi", "rdx", "rcx", "r8", "r9"]

target = null

compileLine = { line ->
    def matcher = line =~ /^(\s*)call\s+(\w+)([^;]*)(.*)/

    def params = matcher ?
            matcher.group(3).split(",")
                    .findAll { str -> !str.trim().isEmpty() } :
            []
    if (!params.isEmpty()) {
        params.eachWithIndex { it, i ->
            target << matcher.group(1)
            target << "mov \t" + argsRegisters[i] + ", " + it.trim() + "\n"
        }
        target << matcher.group(1)
        target << "call \t" + matcher.group(2) + " " + matcher.group(4) + "\n"
    } else
        target << line << "\n"
}

def fromFileToFile() {
    target = new File(args[1])
    target.setText("")

    new File(args[0]).eachLine compileLine

}

def foo() {
    target = new StringWriter()
    compileLine.call("  call foo, 2, 3")
    compileLine.call("  mov   rdi, rax")
    compileLine.call("  call    foobar  ; lets you function")
    compileLine.call("  call    f1, raz, dva  ; lets you function")
    compileLine.call("  call    f2, raz, dva  ; lets you function")
    println target.toString()
}

fromFileToFile()
//foo()
