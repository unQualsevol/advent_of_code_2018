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

printMap(map)

for (i in 1..10) {
    var newMap = Array(height + 2, { CharArray(height + 2) })
    for (row in 1..height) {
        for (column in 1..width) {
            val current = map[row][column]
            newMap[row][column] = computeNewValue(current, row, column)
        }
    }
    map = newMap

    printMap(map)
}

var count = 0
fun printMap(map: Array<CharArray>) {
    var countTrees = 0
    var countLumberjacks = 0
    map.forEach {
        it.forEach {
            print(it)
            if(it == '|') countTrees++
            if (it == '#') countLumberjacks++
        }
        println()
    }
    println("count: ${count++} trees: $countTrees lumberjacks: $countLumberjacks total: ${countLumberjacks*countTrees}")
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
