import java.io.File

val lines = File("input").readLines()

val height = lines.size
val width = lines.first().length

var map = Array(height + 2, { CharArray(height + 2) })

lines.forEachIndexed { row, line ->
    line.forEachIndexed { column, c ->
        map[row + 1][column + 1] = c
    }
}

//I printed the 600 first results, then looked for repetitions
//that values appears every 28 since iteration 488
//then 1000000000%28 is 20. if 484%28 is 12
//then the result from 494 is the same as what we want
for (i in 1..600) {
    var newMap = Array(height + 2, { CharArray(height + 2) })
    var countTrees = 0
    var countLumberjacks = 0
    for (row in 1..height) {
        for (column in 1..width) {
            val current = map[row][column]
            val newValue = computeNewValue(current, row, column)
            if(newValue == '|') countTrees++
            if (newValue == '#') countLumberjacks++
            newMap[row][column] = newValue
        }
    }
    map = newMap
    val total = countLumberjacks * countTrees
    println("count: ${i} trees: $countTrees lumberjacks: $countLumberjacks total: $total")
}

fun computeNewValue(current: Char, row: Int, column: Int): Char {
    when (current) {
        '.' -> {
            if (count('|', row, column) >= 3) return '|' else return '.'
        }
        '|' -> {
            if (count('#', row, column) >= 3) return '#' else return '|'
        }
        '#' -> {
            if (count('#', row, column) >= 1 && count('|', row, column) >= 1) return '#' else return '.'
        }
    }
    return ' '
}

fun count(c: Char, row: Int, column: Int): Int {
    var count = 0
    for (y in row - 1..row + 1) {
        for (x in column - 1..column + 1) {
            if (y == row && x == column) continue
            if (map[y][x] == c) count++
        }
    }
    return count
}
