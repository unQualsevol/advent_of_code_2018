import java.io.File

val lines = File("input").readLines()

var count = 0
for (i in 0 until lines.size step 4) {
    val before: Register = readRegister(lines[i])
    val operators: Operators = readInstructionOperators(lines[i + 1])
    val after: Register = readRegister(lines[i + 2])
    val sample: Step = Step(before, operators, after)
    val matches: Int = sample.getNumberOfMatches()
    if (matches >= 3) {
        count++
    }
}
println(count)

fun readRegister(line: String): Register {
    val regex = """\[(\d+).{2}(\d+).{2}(\d+).{2}(\d+)\]$""".toRegex()

    val matchResult = regex.find(line)

    val (r0, r1, r2, r3) = matchResult!!.destructured

    return Register(r0.toInt(), r1.toInt(), r2.toInt(), r3.toInt())

}

fun readInstructionOperators(line: String): Operators {
    val regex = """^\d+\s(\d+)\s(\d+)\s(\d+)$""".toRegex()

    val matchResult = regex.find(line)

    val (a, b, c) = matchResult!!.destructured

    return Operators(a.toInt(), b.toInt(), c.toInt())
}

class Operators(val a: Int, val b: Int, val rc: Int)

data class Register(var r0: Int, var r1: Int, var r2: Int, var r3: Int) {
    fun setValue(value: Int, register: Int) {
        when (register) {
            0 -> r0 = value
            1 -> r1 = value
            2 -> r2 = value
            3 -> r3 = value
        }
    }

    fun getValue(register: Int): Int {
        when (register) {
            0 -> return r0
            1 -> return r1
            2 -> return r2
            3 -> return r3
        }
        return -1
    }
}

interface Instruction {
    val a: Int
    val b: Int
    val rc: Int
    fun execute(register: Register): Register
}

class addr(override val a: Int, override val b: Int, override val rc: Int) : Instruction {
    //I just need id to keep the values to test
    override fun execute(register: Register): Register {
        register.setValue(register.getValue(a) + register.getValue(b), rc)
        return register
    }
}

class addi(override val a: Int, override val b: Int, override val rc: Int) : Instruction {
    //I just need id to keep the values to test
    override fun execute(register: Register): Register {
        register.setValue(register.getValue(a) + b, rc)
        return register
    }
}

class mulr(override val a: Int, override val b: Int, override val rc: Int) : Instruction {
    //I just need id to keep the values to test
    override fun execute(register: Register): Register {
        register.setValue(register.getValue(a) * register.getValue(b), rc)
        return register
    }
}

class muli(override val a: Int, override val b: Int, override val rc: Int) : Instruction {
    //I just need id to keep the values to test
    override fun execute(register: Register): Register {
        register.setValue(register.getValue(a) * b, rc)
        return register
    }
}

class banr(override val a: Int, override val b: Int, override val rc: Int) : Instruction {
    //I just need id to keep the values to test
    override fun execute(register: Register): Register {
        register.setValue(register.getValue(a).and(register.getValue(b)), rc)
        return register
    }
}

class bani(override val a: Int, override val b: Int, override val rc: Int) : Instruction {
    //I just need id to keep the values to test
    override fun execute(register: Register): Register {
        register.setValue(register.getValue(a).and(b), rc)
        return register
    }
}

class borr(override val a: Int, override val b: Int, override val rc: Int) : Instruction {
    //I just need id to keep the values to test
    override fun execute(register: Register): Register {
        register.setValue(register.getValue(a).or(register.getValue(b)), rc)
        return register
    }
}

class bori(override val a: Int, override val b: Int, override val rc: Int) : Instruction {
    //I just need id to keep the values to test
    override fun execute(register: Register): Register {
        register.setValue(register.getValue(a).or(b), rc)
        return register
    }
}

class setr(override val a: Int, override val b: Int, override val rc: Int) : Instruction {
    //I just need id to keep the values to test
    override fun execute(register: Register): Register {
        register.setValue(register.getValue(a), rc)
        return register
    }
}

class seti(override val a: Int, override val b: Int, override val rc: Int) : Instruction {
    //I just need id to keep the values to test
    override fun execute(register: Register): Register {
        register.setValue(a, rc)
        return register
    }
}

class gtir(override val a: Int, override val b: Int, override val rc: Int) : Instruction {
    //I just need id to keep the values to test
    override fun execute(register: Register): Register {
        register.setValue(if (a > register.getValue(b)) 1 else 0, rc)
        return register
    }
}

class gtri(override val a: Int, override val b: Int, override val rc: Int) : Instruction {
    //I just need id to keep the values to test
    override fun execute(register: Register): Register {
        register.setValue(if (register.getValue(a) > b) 1 else 0, rc)
        return register
    }
}

class gtrr(override val a: Int, override val b: Int, override val rc: Int) : Instruction {
    //I just need id to keep the values to test
    override fun execute(register: Register): Register {
        register.setValue(if (register.getValue(a) > register.getValue(b)) 1 else 0, rc)
        return register
    }
}

class eqir(override val a: Int, override val b: Int, override val rc: Int) : Instruction {
    //I just need id to keep the values to test
    override fun execute(register: Register): Register {
        register.setValue(if (a == register.getValue(b)) 1 else 0, rc)
        return register
    }
}

class eqri(override val a: Int, override val b: Int, override val rc: Int) : Instruction {
    //I just need id to keep the values to test
    override fun execute(register: Register): Register {
        register.setValue(if (register.getValue(a) == b) 1 else 0, rc)
        return register
    }
}

class eqrr(override val a: Int, override val b: Int, override val rc: Int) : Instruction {
    //I just need id to keep the values to test
    override fun execute(register: Register): Register {
        register.setValue(if (register.getValue(a) == register.getValue(b)) 1 else 0, rc)
        return register
    }
}


class Step(val before: Register, val operators: Operators, val after: Register) {
    fun getNumberOfMatches(): Int {
        //build each instruction with instruction registers
        val instructionsSet:List<Instruction> = buildInstructionsSetWithOperators(operators)
        //run each instruction
        //compare result with expected result
        //if match then increase counter
        return instructionsSet.count { instruction -> instruction.execute(before.copy()) == (after) }
    }

    private fun buildInstructionsSetWithOperators(operators: Operators): List<Instruction>
    = listOf(addr(operators.a, operators.b, operators.rc),
            addi(operators.a, operators.b, operators.rc),
            mulr(operators.a, operators.b, operators.rc),
            muli(operators.a, operators.b, operators.rc),
            banr(operators.a, operators.b, operators.rc),
            bani(operators.a, operators.b, operators.rc),
            borr(operators.a, operators.b, operators.rc),
            bori(operators.a, operators.b, operators.rc),
            setr(operators.a, operators.b, operators.rc),
            seti(operators.a, operators.b, operators.rc),
            gtir(operators.a, operators.b, operators.rc),
            gtri(operators.a, operators.b, operators.rc),
            gtrr(operators.a, operators.b, operators.rc),
            eqir(operators.a, operators.b, operators.rc),
            eqri(operators.a, operators.b, operators.rc),
            eqrr(operators.a, operators.b, operators.rc)
            )
}
