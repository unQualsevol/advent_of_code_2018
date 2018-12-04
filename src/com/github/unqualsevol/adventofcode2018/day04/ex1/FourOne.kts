import java.io.File

val lines = File("input").readLines()
val sortedLines = lines.sorted()

val it = sortedLines.iterator()

val guardsMap = mutableMapOf<String, Guard>()

val guardRegex = """#(\d+)""".toRegex()
val minuteRegex = """:(\d+)""".toRegex()

var currentGuard:Guard? = null;
while (it.hasNext()) {
    val line = it.next();
    if(line.contains("#")){
        val guard = line
        val matchResult = guardRegex.find(guard)
        val (id) = matchResult!!.destructured
        if(guardsMap.containsKey(id)){
            currentGuard = guardsMap[id]
        } else {
            currentGuard = Guard(id)
            guardsMap.put(id, currentGuard!!)
        }
    } else {
        val asleep = line
        val matchStart = minuteRegex.find(asleep)
        val (start) = matchStart!!.destructured
        val wakeUp = it.next()
        val matchEnd = minuteRegex.find(wakeUp)
        val (end) = matchEnd!!.destructured
        currentGuard!!.addAsleepMinutes(start.toInt(), end.toInt())
    }
}

val maxGuard = guardsMap.values.maxBy { it -> it.totalMinutesSleep }
val id = maxGuard!!.id.toInt()
val mostMinutesAsleep = maxGuard!!.getMostMinutesAsleep()
println(id * mostMinutesAsleep)

data class Guard(val id:String){
    val asleepMinutes = mutableMapOf<Int, Int>()
    var totalMinutesSleep = 0;
    fun addAsleepMinutes(start:Int, end:Int) {
        for (i in start until end){
            if(asleepMinutes.containsKey(i)) {
                asleepMinutes[i] = asleepMinutes[i]!!.plus(1)
            } else {
                asleepMinutes[i] = 1;
            }
        }
        totalMinutesSleep += end-start
    }

    fun getMostMinutesAsleep():Int {
        return asleepMinutes.maxBy { it.value }!!.key;
    }

}