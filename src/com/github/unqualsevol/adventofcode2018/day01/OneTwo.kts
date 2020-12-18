import java.io.File


var count = 0
val mutableSetOf = mutableSetOf<Int>(0)
val listOfValues = File("input").readLines().map { it.toInt() }
var found = false
mainloop@ while (true) {
    for (current in listOfValues) {
        count += current
        found = !mutableSetOf.add(count)
        if (found) {
            break@mainloop
        }
    }
}
println(count)