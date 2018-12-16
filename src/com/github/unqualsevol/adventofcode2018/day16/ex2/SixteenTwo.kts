import java.io.File

val lines = File("input").readLines()

val instructionsToModelMapping = mutableMapOf<Int, Int>()
val map = mutableMapOf<Int, MutableSet<Int>>()
for (i in 0 until lines.size step 4) {
    val before: Registers = readRegister(lines[i])
    val operators: Operators = readInstructionOperators(lines[i + 1])
    val after: Registers = readRegister(lines[i + 2])
    val sample: Step = Step(before, operators, after)
    val modelInstructionsIds = sample.getMatches()
    modelInstructionsIds.removeAll(instructionsToModelMapping.values)
    if (map.containsKey(operators.op)) {
        map[operators.op]!!.retainAll(modelInstructionsIds)
    } else {
        map[operators.op] = modelInstructionsIds
    }
    val modelInstructions = map[operators.op]!!
    if (modelInstructions.size == 1) {
        instructionsToModelMapping[operators.op] = modelInstructions.first()
    }
}
println(instructionsToModelMapping)

val registers = Registers(0, 0, 0, 0)
File("input2").forEachLine {
    val operators = readInstructionOperators(it)
    val instruction: Instruction = buildInstruction(operators)
    instruction.execute(registers)
}
println(registers)

fun readRegister(line: String): Registers {
    val regex = """\[(\d+).{2}(\d+).{2}(\d+).{2}(\d+)\]$""".toRegex()

    val matchResult = regex.find(line)

    val (r0, r1, r2, r3) = matchResult!!.destructured

    return Registers(r0.toInt(), r1.toInt(), r2.toInt(), r3.toInt())

}

fun readInstructionOperators(line: String): Operators {
    val regex = """^(\d+)\s(\d+)\s(\d+)\s(\d+)$""".toRegex()

    val matchResult = regex.find(line)

    val (op, a, b, c) = matchResult!!.destructured

    return Operators(op.toInt(), a.toInt(), b.toInt(), c.toInt())
}

class Operators(val op: Int, val a: Int, val b: Int, val rc: Int)

data class Registers(var r0: Int, var r1: Int, var r2: Int, var r3: Int) {
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
    val op: Int
    val a: Int
    val b: Int
    val rc: Int
    fun execute(registers: Registers): Registers
}

class addr(override val a: Int, override val b: Int, override val rc: Int) : Instruction {
    override val op: Int = 0
    //I just need id to keep the values to test
    override fun execute(registers: Registers): Registers {
        registers.setValue(registers.getValue(a) + registers.getValue(b), rc)
        return registers
    }
}

class addi(override val a: Int, override val b: Int, override val rc: Int) : Instruction {
    override val op: Int = 1
    //I just need id to keep the values to test
    override fun execute(registers: Registers): Registers {
        registers.setValue(registers.getValue(a) + b, rc)
        return registers
    }
}

class mulr(override val a: Int, override val b: Int, override val rc: Int) : Instruction {
    override val op: Int = 2
    //I just need id to keep the values to test
    override fun execute(registers: Registers): Registers {
        registers.setValue(registers.getValue(a) * registers.getValue(b), rc)
        return registers
    }
}

class muli(override val a: Int, override val b: Int, override val rc: Int) : Instruction {
    override val op: Int = 3
    //I just need id to keep the values to test
    override fun execute(registers: Registers): Registers {
        registers.setValue(registers.getValue(a) * b, rc)
        return registers
    }
}

class banr(override val a: Int, override val b: Int, override val rc: Int) : Instruction {
    override val op: Int = 4
    //I just need id to keep the values to test
    override fun execute(registers: Registers): Registers {
        registers.setValue(registers.getValue(a).and(registers.getValue(b)), rc)
        return registers
    }
}

class bani(override val a: Int, override val b: Int, override val rc: Int) : Instruction {
    override val op: Int = 5
    //I just need id to keep the values to test
    override fun execute(registers: Registers): Registers {
        registers.setValue(registers.getValue(a).and(b), rc)
        return registers
    }
}

class borr(override val a: Int, override val b: Int, override val rc: Int) : Instruction {
    override val op: Int = 6
    //I just need id to keep the values to test
    override fun execute(registers: Registers): Registers {
        registers.setValue(registers.getValue(a).or(registers.getValue(b)), rc)
        return registers
    }
}

class bori(override val a: Int, override val b: Int, override val rc: Int) : Instruction {
    override val op: Int = 7
    //I just need id to keep the values to test
    override fun execute(registers: Registers): Registers {
        registers.setValue(registers.getValue(a).or(b), rc)
        return registers
    }
}

class setr(override val a: Int, override val b: Int, override val rc: Int) : Instruction {
    override val op: Int = 8
    //I just need id to keep the values to test
    override fun execute(registers: Registers): Registers {
        registers.setValue(registers.getValue(a), rc)
        return registers
    }
}

class seti(override val a: Int, override val b: Int, override val rc: Int) : Instruction {
    override val op: Int = 9
    //I just need id to keep the values to test
    override fun execute(registers: Registers): Registers {
        registers.setValue(a, rc)
        return registers
    }
}

class gtir(override val a: Int, override val b: Int, override val rc: Int) : Instruction {
    override val op: Int = 10
    //I just need id to keep the values to test
    override fun execute(registers: Registers): Registers {
        registers.setValue(if (a > registers.getValue(b)) 1 else 0, rc)
        return registers
    }
}

class gtri(override val a: Int, override val b: Int, override val rc: Int) : Instruction {
    override val op: Int = 11
    //I just need id to keep the values to test
    override fun execute(registers: Registers): Registers {
        registers.setValue(if (registers.getValue(a) > b) 1 else 0, rc)
        return registers
    }
}

class gtrr(override val a: Int, override val b: Int, override val rc: Int) : Instruction {
    override val op: Int = 12
    //I just need id to keep the values to test
    override fun execute(registers: Registers): Registers {
        registers.setValue(if (registers.getValue(a) > registers.getValue(b)) 1 else 0, rc)
        return registers
    }
}

class eqir(override val a: Int, override val b: Int, override val rc: Int) : Instruction {
    override val op: Int = 13
    //I just need id to keep the values to test
    override fun execute(registers: Registers): Registers {
        registers.setValue(if (a == registers.getValue(b)) 1 else 0, rc)
        return registers
    }
}

class eqri(override val a: Int, override val b: Int, override val rc: Int) : Instruction {
    override val op: Int = 14
    //I just need id to keep the values to test
    override fun execute(registers: Registers): Registers {
        registers.setValue(if (registers.getValue(a) == b) 1 else 0, rc)
        return registers
    }
}

class eqrr(override val a: Int, override val b: Int, override val rc: Int) : Instruction {
    override val op: Int = 15
    //I just need id to keep the values to test
    override fun execute(registers: Registers): Registers {
        registers.setValue(if (registers.getValue(a) == registers.getValue(b)) 1 else 0, rc)
        return registers
    }
}


class Step(val before: Registers, val operators: Operators, val after: Registers) {
    private val instructionsSet = buildInstructionsSetWithOperators(operators)

    fun getMatches(): MutableSet<Int> = instructionsSet
            .filter { instruction -> instruction.execute(before.copy()) == (after) }
            .map { instruction -> instruction.op }.toMutableSet()

    private fun buildInstructionsSetWithOperators(operators: Operators): List<Instruction> = listOf(addr(operators.a, operators.b, operators.rc),
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

fun buildInstruction(operators: Operators): Instruction {
    when (instructionsToModelMapping[operators.op]) {
        0 -> return addr(operators.a, operators.b, operators.rc)
        1 -> return addi(operators.a, operators.b, operators.rc)
        2 -> return mulr(operators.a, operators.b, operators.rc)
        3 -> return muli(operators.a, operators.b, operators.rc)
        4 -> return banr(operators.a, operators.b, operators.rc)
        5 -> return bani(operators.a, operators.b, operators.rc)
        6 -> return borr(operators.a, operators.b, operators.rc)
        7 -> return bori(operators.a, operators.b, operators.rc)
        8 -> return setr(operators.a, operators.b, operators.rc)
        9 -> return seti(operators.a, operators.b, operators.rc)
        10 -> return gtir(operators.a, operators.b, operators.rc)
        11 -> return gtri(operators.a, operators.b, operators.rc)
        12 -> return gtrr(operators.a, operators.b, operators.rc)
        13 -> return eqir(operators.a, operators.b, operators.rc)
        14 -> return eqri(operators.a, operators.b, operators.rc)
        15 -> return eqrr(operators.a, operators.b, operators.rc)
    }
    throw Exception()
}
