import java.io.File

val lines = File("input").readLines()

var potSequence: IntArray = buildPotSequence(lines[0].removePrefix("initial state: "))

val rules = mutableListOf<String>()

val patternLenght = 5
lines.filter { it.endsWith("=> #") }.forEach { line -> rules.add(line.take(patternLenght)) }

var generationsDone = 0
val mapDiffFrequences = mutableMapOf<Int, Int>()
//go until the diff between generations is stable
while (true) {
    var start = getStartingPot(potSequence)
    var end = getEndingPot(potSequence)

    val newPotSequence = mutableListOf<Int>()
    for (pot in start..end) {
        if (rules.contains(sequenceFrom(pot, potSequence))) {
            newPotSequence.add(pot)
        }
    }
    val oldSum = potSequence.sum()
    potSequence = newPotSequence.toIntArray()
    val newSum = potSequence.sum()
    val diff = newSum-oldSum
    generationsDone++
    if (mapDiffFrequences.containsKey(diff)){
        val value = mapDiffFrequences[diff]!!
        if(value >= 10) break;
        mapDiffFrequences[diff] = value.plus(1)
    } else {
        mapDiffFrequences[diff] = 1
    }

}

val baseValue = potSequence.sum()
println(baseValue+(50000000000-generationsDone)*32)


fun buildPotSequence(input: String): IntArray {
    val list = mutableListOf<Int>()
    input.forEachIndexed { index, c -> if (c == '#') list.add(index) }
    return list.toIntArray()
}

fun getStartingPot(potSequence: IntArray): Int {
    return potSequence.first() - patternLenght
}

fun getEndingPot(potSequence: IntArray): Int {
    return potSequence.last() + patternLenght
}

fun sequenceFrom(pot: Int, potSequence: IntArray): String {
    val buffer = StringBuffer()
    for(i in pot-2..pot+2) {
        if (potSequence.contains(i)){
            buffer.append('#')
        } else {
            buffer.append('.')
        }
    }
    return buffer.toString()
}
