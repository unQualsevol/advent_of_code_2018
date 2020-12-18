import java.io.File

var twiceCount = 0
var threeTimeCount = 0

val lines = File("input").readLines()


twiceCount =lines.count( {it -> hasAnyCharTwice(occurrences(it))})
threeTimeCount =lines.count( {it -> hasAnyCharThreeTimes(occurrences(it))})

println(twiceCount * threeTimeCount)

fun hasAnyCharTwice(occurrences: Map<Int, Char>) : Boolean {
    return occurrences.containsKey(2)
}

fun hasAnyCharThreeTimes(occurrences: Map<Int, Char>) : Boolean {
    return occurrences.containsKey(3)
}

fun occurrences(sequence: String): Map<Int, Char> {
    val associateBy = sequence.associateBy {
        sequence.count({ ch -> ch == it })
    }
    return associateBy
}