import java.io.File
import kotlin.math.absoluteValue

val coorList = mutableListOf<Coordinate>()
File("input").forEachLine {
    val regex = """^(\d+),\s(\d+)$""".toRegex()

    val matchResult = regex.find(it)

    val (ys, xs) = matchResult!!.destructured

    val x = xs.toInt()
    val y = ys.toInt()
    val current = Coordinate(x, y)
    coorList.add(current)
}

println(coorList)

val xMax = coorList.maxBy { c -> c.x }!!.x
println(xMax)
val yMax = coorList.maxBy { c -> c.y }!!.y + 1
println(yMax)
val xMin = coorList.minBy { c -> c.x }!!.x
println(xMin)
val yMin = coorList.minBy { c -> c.y }!!.y
println(yMin)

var countUnder10000 = 0
for (x in xMin..xMax) {
    for(y in yMin..yMax) {
        val totalDistance = computeTotalDistanceFrom(x,y)
        if(totalDistance < 10000){
            countUnder10000++
        }
    }
}
println("spaces under 10000: $countUnder10000")

data class Coordinate(val x: Int, val y: Int) {

    fun distanceTo(coordinate: Coordinate): Int {
        val result = (this.x - coordinate.x).absoluteValue + (this.y - coordinate.y).absoluteValue
        return result
    }
}

fun computeTotalDistanceFrom(x: Int, y: Int): Int {
    var result = 0
    val current = Coordinate(x,y)
    coorList.forEach { result+= current.distanceTo(it)}
    return result
}
