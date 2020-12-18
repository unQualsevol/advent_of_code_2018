import java.io.File

var count = File("input").readLines().map { it.toInt() }.sum()
println("frequency is: $count")