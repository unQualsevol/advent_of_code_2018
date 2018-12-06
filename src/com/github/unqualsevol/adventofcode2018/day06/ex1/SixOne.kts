import java.io.File
import kotlin.math.absoluteValue

val centerList = mutableListOf<Center>()
var map = mutableMapOf<Coordinate, Cell>()
val nullCenter = Center(46, Coordinate(-1, -1))
var id = 65
File("input").forEachLine {
    val regex = """^(\d+),\s(\d+)$""".toRegex()

    val matchResult = regex.find(it)

    val (ys, xs) = matchResult!!.destructured

    val x = xs.toInt()
    val y = ys.toInt()
    val current = Coordinate(x, y)
    val center = Center(id++, current)
    centerList.add(center)
    map[current] = Cell(current, center, 0)
}

println(centerList)
val xMax = centerList.maxBy { c -> c.coordinate.x }!!.coordinate.x
println(xMax)
val yMax = centerList.maxBy { c -> c.coordinate.y }!!.coordinate.y + 1
println(yMax)


fillMap()

printMap()


println("size before: ${centerList.size}")
removeInfiniteLands()
println("size after: ${centerList.size}")

println("Longest Area: " + getLongestArea())

fun getLongestArea(): Int {

    val longestCenter = centerList.maxBy { center ->
        (map.values.count { cell ->
            center.equals(cell.center)
        })
    }
    return map.values.count { cell ->
        longestCenter!!.equals(cell.center)
    }
}


fun fillLevel(center: Center): Int {
    var countModified = 0
    for (dx in 0..xMax) {
        for (dy in 0..yMax) {
            val current = Coordinate(dx, dy)
            val distanceToCenter = current.distanceTo(center.coordinate)
            if (isNotValid(current, distanceToCenter)) continue
            val cell = map[current]
            if (cell == null) {
                map[current] = Cell(current, center, distanceToCenter)
                countModified++
            } else if (cell.weight > distanceToCenter) {
                cell.weight = distanceToCenter
                cell.center = center
                countModified++
            } else if (cell.weight == distanceToCenter) {
                cell.center = nullCenter
            }

        }
    }
    return countModified
}

fun fillMap() {
    for (center in centerList) {
        fillLevel(center)
    }
}

fun isNotValid(coordinate: Coordinate, distance:Int): Boolean {
    return coordinate.x < 0 || coordinate.x > xMax || coordinate.y < 0 || coordinate.y > yMax || distance == 0
}

data class Coordinate(val x: Int, val y: Int) {

    fun distanceTo(coordinate: Coordinate): Int {
        val result = (this.x - coordinate.x).absoluteValue + (this.y - coordinate.y).absoluteValue
        return result
    }
}

data class Center(val id: Int, val coordinate: Coordinate)
data class Cell(val coordinate: Coordinate, var center: Center, var weight: Int = 500)

fun removeInfiniteLands() {
    map.filter { (k) -> k.y == 0 || k.x == 0 || k.x == xMax || k.y == yMax }.values.forEach { centerList.remove(it.center) }
}

fun printMap() {
    for (i in 0..xMax) {
        for (j in 0..yMax) {
            val current = SixOne.Coordinate(i, j)
            if (map.containsKey(current)) {
                print(map[current]!!.center.id.toChar())
                //print(map[current]!!.weight)
            } else {
                print("$")
            }
        }
        println()
    }
}

