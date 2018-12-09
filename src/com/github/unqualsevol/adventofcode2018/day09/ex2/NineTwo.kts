//I just added the input here but I don feel I need to get it from the file
val input = "429 players; last marble is worth 70901 points"
// I don't even going to use it...
val totalPlayers = 429
val lastMarble = 70901*100
val stoleTurn = 23

val players = LongArray(totalPlayers) { 0 }

val startMarble = Marble(0)
var currentMarble = startMarble;

var currentPlayer = 0

for (currentMarbleValue in 1..lastMarble) {
    if (currentMarbleValue % stoleTurn == 0) {
        currentMarble = currentMarble.getLeft(6)
        players.set(currentPlayer, players.get(currentPlayer) + currentMarbleValue + currentMarble.left.value)
        currentMarble.removeLeft()
    } else {
        currentMarble = currentMarble.add(currentMarbleValue)
    }
    currentPlayer = (currentPlayer + 1) % totalPlayers
}


val maxScore = players.max()
val index = players.indexOf(maxScore!!) +1
println("Player: $index Score: $maxScore")

data class Marble(val value: Int) {
    var right: Marble

    var left: Marble

    init {
        right = this
        left = this
    }

    fun add(marbleValue: Int): Marble {
        val newMarble = Marble(marbleValue)
        this.right.addRight(newMarble)

        return newMarble
    }

    private fun addRight(newRight: Marble) {
        val oldRight = this.right

        this.right = newRight
        newRight.left = this
        newRight.right = oldRight
        oldRight.left = newRight
    }

    fun getLeft(count: Int): Marble {
        var result = this
        for (i in 0 until count) {
            result = result.left
        }
        return result
    }

    fun removeLeft() {
        val oldLeft = this.left
        this.left = oldLeft.left
        oldLeft.left.right = this

        oldLeft.left = oldLeft
        oldLeft.right = oldLeft
    }
}

fun printMarbles(startMarble: Marble, currentMarble: Marble): String {
    var printMarble = startMarble;
    val buffer = StringBuffer()
    do {
        if(currentMarble === printMarble) {
            buffer.append("(")
            buffer.append(printMarble.value)
            buffer.append(")")
        } else {

            buffer.append(" ")
            buffer.append(printMarble.value)
            buffer.append(" ")
        }
        printMarble = printMarble.right
    } while (printMarble !== startMarble)
    return buffer.toString()
}
