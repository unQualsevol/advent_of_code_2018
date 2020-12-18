import java.io.File


val springRow = 0
val springColumn = 500
val spring = Coordinate(springRow, springColumn)
val ground = mutableMapOf<Coordinate, GroundType>(Pair(Coordinate(springRow, springColumn), GroundType.Spring))

var i = 1
File("input").forEachLine {
    val regex = """^([xy])=(\d+),\s[xy]=(\d+)\.\.(\d+)$""".toRegex()

    val matchResult = regex.find(it)

    val (xOry, sFirstDimension, sSecondDimensionStart, sSecondDimensionEnd) = matchResult!!.destructured

    val firstDimension = sFirstDimension.toInt()
    val secondDimensionStart = sSecondDimensionStart.toInt()
    val secondDimensionEnd = sSecondDimensionEnd.toInt()
    for (secondDimension in secondDimensionStart..secondDimensionEnd) {
        when (xOry) {
            "x" -> ground[Coordinate(secondDimension, firstDimension)] = GroundType.Clay

            "y" -> ground[Coordinate(firstDimension, secondDimension)] = GroundType.Clay
        }
    }
    i++
}

val top = ground.filter { it.value == GroundType.Clay }.keys.minBy { it.row }!!.row
val bottom = ground.keys.maxBy { it.row }!!.row
val left = ground.keys.minBy { it.column }!!.column
val right = ground.keys.maxBy { it.column }!!.column

downDfs(Coordinate(springRow + 1, springColumn))
val waterCount = ground.filterNot { it.key.row < top }.values.count { it == GroundType.FlowWater || it == GroundType.RestWater }
println("Part1: tiles the water can reach: $waterCount")
val leftWaterCount = ground.filterNot { it.key.row < top }.values.count { it == GroundType.RestWater }
println("Part2: water tiles left are: $leftWaterCount")

fun downDfs(node: Coordinate): Int {
    ground[node] = GroundType.FlowWater
    if (isBottomFloor(node.row)) {
        return 1
    }
    val down = node.down()
    var streamValue = 1
    if (isValid(down)) {
        streamValue += downDfs(down)
        if (ground[down] != GroundType.RestWater) {
            return streamValue
        }
    }
    //find leftest (wall or fall)
    val left = node.left()
    val leftFlow = leftDfs(left)
    //find rightest (wall or fall)
    val right = node.right()
    val rightFlow = rightDfs(right)
    //looks if the water is resting
    val waterType = if (leftFlow.fall || rightFlow.fall) GroundType.FlowWater else GroundType.RestWater
    //fill the water
    for (n in leftFlow.left.column..rightFlow.right.column) {
        ground[Coordinate(leftFlow.left.row, n)] = waterType
    }

    if(leftFlow.fall){
        streamValue+= downDfs(leftFlow.left)
    }
    if(rightFlow.fall){
        streamValue+= downDfs(rightFlow.right)
    }

    return streamValue + (rightFlow.right.column - leftFlow.left.column)
}

fun leftDfs(node: Coordinate): Flow {
    val right = node.right()
    if (isWall(node)) {
        return Flow(right, right, false)
    } else if (isValid(node.down())) {
        return Flow(node, node, true)
    } else {
        val leftFlow = leftDfs(node.left())
        return leftFlow.copy(right = node)
    }
}

fun rightDfs(node: Coordinate): Flow {
    if (isWall(node)) {
        return Flow(node.left(), node.left(), false)
    } else if (isValid(node.down())) {
        return Flow(node, node, true)
    } else {
        val rightFlow = rightDfs(node.right())
        return rightFlow.copy(left = node)
    }
}

fun isValid(node: Coordinate): Boolean {
    val groundType = ground[node]
    return groundType == null || !groundType.isFloor()
}

data class Coordinate(val row: Int, val column: Int) {

    fun down(): Coordinate = Coordinate(row + 1, column)
    fun left(): Coordinate = Coordinate(row, column - 1)
    fun right(): Coordinate = Coordinate(row, column + 1)

}

enum class GroundType(private val code: String, private val floor: Boolean) {
    Sand(".", false),
    Clay("#", true),
    Spring("+", false),
    FlowWater("|", false),
    RestWater("~", true);


    override fun toString(): String {
        return this.code
    }

    fun isFloor(): Boolean = this.floor

}

fun printBoard(board: Map<Coordinate, GroundType>) {

    val bottom = board.keys.maxBy { it.row }!!.row
    val left = board.keys.minBy { it.column }!!.column
    val right = board.keys.maxBy { it.column }!!.column

    for (row in 0..bottom) {
        for (column in left..right) {
            val firstOrNull = board[Coordinate(row, column)]
            if (firstOrNull == null) {
                print(".")
            } else {
                print(firstOrNull)
            }
        }
        println()
    }
    println()
}

fun SeventeenOne.isBottomFloor(row: Int) = row == bottom

data class Flow(val left: Coordinate, val right: Coordinate, val fall: Boolean)

fun isWall(left: Coordinate): Boolean = ground[left] != null && ground[left]!!.isFloor()
