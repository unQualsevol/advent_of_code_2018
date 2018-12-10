import java.io.File
import kotlin.math.absoluteValue

val stars = mutableListOf<Star>()
var maxX = Int.MIN_VALUE
var minX = Int.MAX_VALUE
var maxY = Int.MIN_VALUE
var minY = Int.MAX_VALUE

File("input").forEachLine {
    val regex = """(-?\d+),\s+(-?\d+).+<\s?(-?\d+),\s+(-?\d+)>$""".toRegex()

    val matchResult = regex.find(it)

    val (x, y, dx, dy) = matchResult!!.destructured

    val star = Star(x.toInt(), y.toInt(), dx.toInt(), dy.toInt())
    stars.add(star)
}

//10000 seems to be a good approach to advance faster
var seconds = 10000
stars.forEach { s ->
        s.advance(seconds)
    }
updateMaxMin(stars)

var oldDY = (maxY-minY).absoluteValue

while (true) {
    stars.forEach { s ->
        s.advance(1)
    }
    seconds++
    updateMaxMin(stars)
    val newDY = (maxY-minY).absoluteValue
    if (newDY < 12) {
        break
    }
}
println(seconds)
println(printStarMap())



data class Star(var x: Int, var y: Int, val dx: Int, val dy: Int) {
    fun advance(times:Int) {
        x += dx*times
        y += dy*times
    }

    override fun toString(): String {
        return "{ \"x\": $x, \"y\":$y, \"velocity\": { \"x\": $dx, \"y\": $dy } },"
    }
}

fun updateMaxMin(stars: List<Star>) {
    minX = stars.minBy { s -> s.x }!!.x
    minY = stars.minBy { s -> s.y }!!.y
    maxX = stars.maxBy { s -> s.x }!!.x
    maxY = stars.maxBy { s -> s.y }!!.y
    println("$minX $minY $maxX $maxY")
}

fun printStarMap(): String {
    val buffer = StringBuffer()
    for (y in minY..maxY) {
        for (x in minX..maxX) {
            if (stars.find { s -> s.x == x && s.y == y } != null) {
                buffer.append("#")
            } else {
                buffer.append(".")
            }
        }
        buffer.append("\n")
    }
    val result = buffer.toString()
    return result
}