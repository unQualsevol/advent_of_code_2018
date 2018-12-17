import java.io.File


val springRow = 0
val springColumn = 500
val spring = GroundSquareMeter(springRow, springColumn, GroundType.Spring)
val ground = mutableSetOf(spring)

var i = 1
//File("input").forEachLine {
File("testInput").forEachLine {

    val regex = """^([xy])=(\d+),\s[xy]=(\d+)\.\.(\d+)$""".toRegex()

    val matchResult = regex.find(it)

    val (xOry, sFirstDimension, sSecondDimensionStart, sSecondDimensionEnd) = matchResult!!.destructured

    val firstDimension = sFirstDimension.toInt()
    val secondDimensionStart = sSecondDimensionStart.toInt()
    val secondDimensionEnd = sSecondDimensionEnd.toInt()
    for (secondDimension in secondDimensionStart..secondDimensionEnd) {
        when (xOry) {
            "x" -> ground.add(GroundSquareMeter(secondDimension, firstDimension, GroundType.Clay))
            "y" -> ground.add(GroundSquareMeter(firstDimension, secondDimension, GroundType.Clay))
        }
    }
    i++
}

val bottom = ground.maxBy { it.row }!!.row
val left = ground.minBy { it.column }!!.column
val right = ground.maxBy { it.column }!!.column

printBoard(ground)

println(dfs(GroundSquareMeter(springRow + 1, springColumn, GroundType.FlowWater)))
printBoard(ground)

fun dfs(node: GroundSquareMeter): Int {
    ground.add(node)
    printBoard(ground)
    if(isBottom(node.row)){
        return 1
    }
    val down = node.down(GroundType.FlowWater)
    var waterDown = 0
    if (isValid(down)) {
        waterDown = dfs(down)
    }
    val left = node.left(GroundType.FlowWater)
    var waterAtLeft = 0
    if (isValidLateral(left)) {
        waterAtLeft = dfs(left)
    }
    val right = node.right(GroundType.FlowWater)
    var waterAtRight = 0
    if (isValidLateral(right)) {
        waterAtRight = dfs(right)
    }
    return 1 + waterDown + waterAtLeft + waterAtRight
}

fun isValid(node: GroundSquareMeter): Boolean = !ground.contains(node)
fun isValidLateral(node: GroundSquareMeter): Boolean = !ground.contains(node)

data class GroundSquareMeter(val row: Int, val column: Int) {

    var type: GroundType = GroundType.Sand

    constructor(row: Int, column: Int, type: GroundType) : this(row, column) {
        this.type = type
    }

    override fun toString(): String {
        return type.toString()
    }

    fun down(type: GroundType = GroundType.Sand): GroundSquareMeter = GroundSquareMeter(row + 1, column, type)
    fun left(type: GroundType = GroundType.Sand): GroundSquareMeter = GroundSquareMeter(row, column - 1, type)
    fun right(type: GroundType = GroundType.Sand): GroundSquareMeter = GroundSquareMeter(row, column + 1, type)

}

enum class GroundType(private val code: String) {
    Sand("."),
    Clay("#"),
    Spring("+"),
    FlowWater("|"),

    RestWater("~");


    override fun toString(): String {
        return this.code
    }

}

fun printBoard(board: Set<GroundSquareMeter>) {

    val bottom = board.maxBy { it.row }!!.row
    val left = board.minBy { it.column }!!.column
    val right = board.maxBy { it.column }!!.column

    for (row in 0..bottom) {
        for (column in left..right) {
            val firstOrNull = board.firstOrNull { it == SeventeenOne.GroundSquareMeter(row, column) }
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

fun SeventeenOne.isBottom(row: Int) = row == bottom