import java.io.File


var count = 0
val mutableSetOf = mutableSetOf<Int>(0)
val listOfValues = mutableListOf<Int>()
File("input").forEachLine { listOfValues.add(it.toInt())}
var found = false
mainloop@ while (true) {
    for (current in listOfValues){
        count += current
        found = !mutableSetOf.add(count)
        //println("count: "+ count + " found: " + found)
        if (found) {
            break@mainloop
        }
    }
}
println(count)