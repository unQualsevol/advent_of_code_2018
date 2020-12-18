import java.io.File

val sleighInstructions = mutableMapOf<String, Task>()
File("input.txt").forEachLine {
    val regex = """^Step\s(\w).*(\w)\scan\sbegin\.$""".toRegex()

    val matchResult = regex.find(it)

    val (dependency, current) = matchResult!!.destructured

    if (sleighInstructions.containsKey(current)) {
        sleighInstructions[current]!!.dependencies.add(dependency)
    } else {
        sleighInstructions[current] = Task(current,mutableListOf(dependency))
    }
    if (!sleighInstructions.containsKey(dependency)) {
        sleighInstructions[dependency] = Task(dependency)
    }
}

val workers = listOf(Worker(1), Worker(2), Worker(3), Worker(4), Worker(5))
var readyToStart = sleighInstructions.filter { e -> e.value.canBeStarted() }.keys.sorted()

var seconds = 0

while (notAllFinished()) {
    seconds++
    for(worker in workers){
        if(worker.canDoaNewTask() && readyToStart.isNotEmpty()){
            val taskReady = readyToStart.find { sleighInstructions[it]!!.canBeStarted() }
            worker.task = sleighInstructions[taskReady]
            worker.progressTask()
        } else if(worker.canProgress()){
            worker.progressTask()
        } else {
            worker.task = null
        }
        if(worker.taskDone()){
            removeFromMap(worker.task!!.id)
        }
    }
    readyToStart = sleighInstructions.filter { e -> e.value.canBeStarted() }.keys.sorted()
}
println(seconds)

fun removeFromMap(letter: String?) {
    sleighInstructions.remove(letter)
    sleighInstructions.forEach { e -> e.value.dependencies.remove(letter) }
}

data class Task(val id:String, val dependencies:MutableList<String> = mutableListOf()){
    private val initialCost:Int
    private var cost:Int
    init {
        initialCost = 60 + id[0].toInt() -64
        cost = initialCost
    }

    fun canBeStarted():Boolean = dependencies.isEmpty() && cost == initialCost
    fun isFinished():Boolean = cost == 0
    fun advanceWork() = cost--
    override fun toString(): String {
        return "Task(id='$id', dependencies=$dependencies, initialCost=$initialCost, cost=$cost)"
    }


}

data class Worker(val id:Int, var task:Task? = null){
    fun canDoaNewTask():Boolean = task == null || task!!.isFinished()
    fun canProgress():Boolean = task != null && !task!!.isFinished()
    fun taskDone():Boolean = task != null && task!!.isFinished()
    fun progressTask() = task?.advanceWork()
}

fun SevenTwo.notAllFinished() = sleighInstructions.count { e -> !e.value.isFinished() } > 0