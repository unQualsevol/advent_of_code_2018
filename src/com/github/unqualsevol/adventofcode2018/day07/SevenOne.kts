import java.io.File

val sleighInstructions = mutableMapOf<String, Task>()
File("input.txt").forEachLine {
    val regex = """^Step\s(\w).*(\w)\scan\sbegin\.$""".toRegex()

    val matchResult = regex.find(it)

    val (dependency, current) = matchResult!!.destructured
    println("$dependency $current")

    if (sleighInstructions.containsKey(current)) {
        sleighInstructions[current]!!.dependencies.add(dependency)
    } else {
        sleighInstructions[current] = Task(current,mutableListOf(dependency))
    }
    if (!sleighInstructions.containsKey(dependency)) {
        sleighInstructions[dependency] = Task(dependency)
    }
}
sleighInstructions.forEach { println("$it ${it.value.cost}") }
println()
val result = StringBuffer()
var roots = sleighInstructions.filter { e -> e.value.canBeStarted() }.keys.sorted()
while (!roots.isEmpty()) {
    result.append(roots[0])
    removeFromMap(roots[0])
    roots = sleighInstructions.filter { e -> e.value.canBeStarted() }.keys.sorted()
}
println(result)

fun removeFromMap(letter: String) {
    sleighInstructions.remove(letter)
    sleighInstructions.forEach { e -> e.value.dependencies.remove(letter) }
}

data class Task(val id:String, val dependencies:MutableList<String> = mutableListOf()){
    private val initialCost:Int
    var cost:Int
    init {
        initialCost = 60 + id[0].toInt() -64
        cost = initialCost
    }

    fun canBeStarted():Boolean = dependencies.isEmpty() && cost == initialCost
    fun isFinished():Boolean = cost == 0
}