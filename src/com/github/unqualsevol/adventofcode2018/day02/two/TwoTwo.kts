import java.io.File

val lines = File("input").readLines()

val results = mutableListOf<String>()

for (i in 0..lines.size-1) {
    val mainLine = lines[i]
    nextLine@ for(j in (i+1)..lines.size-1) {
        val currentLine = lines[j]
        var diff = 0
        var index = -1
        for(k in 0.. currentLine.length-1) {
            if(!mainLine.get(k).equals(currentLine.get(k))) {
                diff++
                index=k
            }
            if (diff > 1) {
                continue@nextLine
            }
        }
        if (index>-1) {
            val element = currentLine.removeRange(index,index+1)
            results.add(element)
        }

    }
}
println(results)