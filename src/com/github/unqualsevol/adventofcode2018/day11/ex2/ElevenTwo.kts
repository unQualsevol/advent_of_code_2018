val gridSerialNumber = 1788

val size = 300

//1,1 ... 300,1
//...       ...
//1,300 ... 300,300

val board = Array(size, { IntArray(size) })

for (x in 0 until size) {
    for (y in 0 until size) {
        board[x][y] = computePowerLevel(x + 1, y + 1)
    }
}

var indexX = 0
var indexY = 0
var maxPowerSquareSize = 0
var maxValue = Int.MIN_VALUE


//To avoid Index out of bounds, the explanation also says the 3x3 is not cut by the limits
for (squareSize in 1..300) {
    for (x in 0..size - squareSize) {
        for (y in 0..size - squareSize) {
            val value = computeSquare(x, y, squareSize)
            if (maxValue < value) {
                maxValue = value
                indexX = x
                indexY = y
                maxPowerSquareSize = squareSize
            }
        }
    }
}
//adding one to indexes as I've 0 index array
println("value: $maxValue pos[${indexX + 1},${indexY + 1}] size: $maxPowerSquareSize")

fun computeSquare(x: Int, y: Int, size: Int): Int {
    var result = 0
    for (i in x until x + size) {
        for (j in y until y + size) {
            result += board[i][j]
        }
    }
    return result
}

fun computePowerLevel(x: Int, y: Int): Int {
    val rackId = x + 10
    var tmpPower = ((rackId * y) + gridSerialNumber) * rackId
    val hundredDigit = (tmpPower / 100) % 10
    val powerLevel = hundredDigit - 5
    return powerLevel
}

fun printBoard() {
    for (y in 0 until size) {
        for (x in 0 until size) {
            if (board[x][y] < 0) {
                print("${board[x][y]}, ")
            } else {
                print(" ${board[x][y]}, ")
            }
        }
        println()
    }
}
