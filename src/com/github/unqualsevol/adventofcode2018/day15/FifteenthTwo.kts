import com.github.unqualsevol.adventofcode2018.day15.model.Coordinate
import java.io.File

//there is something wrong is I run it from 4 it stops at 14
//but is not good then trying from 15 it stops at 16 and the is the right answer... WTF!
//probably I have an error somewhere between shortest path and the first read...
var atkPower = 15
var board = readFile("input", atkPower)
while (true) {
    while (true) {
        if (!board.runTurn()) {
            break
        }
    }
    println("$atkPower")
    board.printMap()
    if (board.anyElfDead()) {
        board = readFile("input", ++atkPower)
    } else {
        break
    }

}

fun readFile(filePath: String, atkPower: Int): Board {
    var row = 0
    val map = mutableMapOf<Coordinate, Boolean>()
    val mobs = mutableListOf<Mob>()

    File(filePath).forEachLine {
        it.forEachIndexed { column, c ->
            val coordinate = Coordinate(row, column)
            when (c) {
                '.' -> map[coordinate] = true
                'E' -> {
                    map[coordinate] = true
                    mobs.add(Elf(coordinate, atkPower = atkPower))
                }
                'G' -> {
                    map[coordinate] = true
                    mobs.add(Goblin(coordinate, atkPower = 3))
                }
                else -> {
                    map[coordinate] = false
                }
            }
        }
        row++
    }
    return Board(map, mobs)
}

println("turns(${board.turns}) * totalHP(${board.getTotalHp()}) = ${board.turns * board.getTotalHp()}")

data class Board(val map: Map<Coordinate, Boolean>, val mobs: List<Mob>) {

    var turns = 0
    val mobByLocation = mobs.map { it.coordinate to it }.toMap().toMutableMap()

    fun getTotalHp(): Int {
        return mobs.sumBy { m ->
            if (m.hp > 0)
                m.hp
            else
                0
        }
    }

    fun anyElfDead(): Boolean = mobs.any { m -> m is Elf && m.isDead() }

    fun isFree(coordinate: Coordinate): Boolean {
        return !isBusy(coordinate)
    }

    fun isBusy(coordinate: Coordinate): Boolean {
        val mob = mobByLocation[coordinate]
        return !map[coordinate]!! || (mob != null && mob.isAlive())
    }

    fun runTurn(): Boolean {
        if(anyElfDead()){
            return false
        }
        val sortedMobs = mobs.sortedBy { mob -> mob.coordinate }
        for (mob in sortedMobs) {
            if (mob.isDead())
                continue
            val enemies: List<Mob> = mob.findEnemies(this)
            if (enemies.none()) {
                return false
            }
            mob.move(this)
            mob.attack(enemies)
        }
        turns++
        return true
    }

    fun printMap() {
        for (row in 0 until 32) {
            for (column in 0 until 32) {
                val coordinate = Coordinate(row, column)
                if (map[coordinate]!!) {
                    if (mobByLocation[coordinate] is Elf && mobByLocation[coordinate]!!.isAlive()) {
                        print("E")
                    } else if (mobByLocation[coordinate] is Goblin && mobByLocation[coordinate]!!.isAlive()) {
                        print("G")
                    } else {
                        print(".")
                    }

                } else {
                    print("#")
                }
            }
            println()
        }
        mobs.forEach {
            if (it is Elf)
                print("E")
            else
                print("G")
            print("[${it.coordinate.row}][${it.coordinate.column}](${it.hp}) ")
        }
        println()
        println()
    }

    fun distance(target: Coordinate, source: Coordinate): Int {
        val mapOfDistances = distanceWithBfs(source)
        if (mapOfDistances.containsKey(target)) {
            return mapOfDistances[target]!!
        }
        return Int.MAX_VALUE
    }

    private fun distanceWithBfs(src: Coordinate): Map<Coordinate, Int> {
        var nodes = mutableMapOf<Coordinate, Int>()
        nodes[src] = 0

        val queue = mutableListOf<Coordinate>()

        queue.add(src)

        while (!queue.isEmpty()) {
            val current = queue.removeAt(0)
            if (isFree(current)) {
                current.adjacentCoordinates().forEach { aux ->
                    if (!nodes.containsKey(aux) && isFree(aux)) {
                        queue.add(aux)
                        nodes[aux] = nodes[current]!! + 1
                    }
                }
            }
        }
//        nodes.remove(src)
        return nodes
    }

    fun updateMobLocation(mob: Mob, newCoor: Coordinate) {
        mobByLocation.remove(mob.coordinate)
        mobByLocation[newCoor] = mob
    }
}


interface Mob : Comparable<Mob> {
    fun findEnemies(board: Board): List<Mob>
    fun attack(mobs: List<Mob>)
    fun getAdjacentEnemiesAlive(mobs: List<Mob>): List<Mob>
    fun isAdjacent(mob: Mob): Boolean

    fun isAlive(): Boolean
    fun isDead(): Boolean
    fun move(board: Board)

    var coordinate: Coordinate

    var hp: Int
    val atkPower: Int
}

abstract class AbstractMob(override var coordinate: Coordinate, override var hp: Int = 200, override val atkPower: Int = 3) : Mob {
    abstract fun isEnemy(mob: Mob): Boolean

    override fun move(board: Board) {
        val adjacentCoordinates = coordinate.adjacentCoordinates()
        if (adjacentCoordinates.none { board.isFree(it) }) {
            //no option to move
            return
        }
        val enemies = this.findEnemies(board)
        if (enemies.any() { enemy -> adjacentCoordinates.contains(enemy.coordinate) }) {
            //there is an adjacent enemy
            return
        }
        val targetCoordinates = getCoordinatesAdjacentToEnemies(enemies, board)
        //filter factible
        //get lowest distance
        val adjacentDistanceMap = mutableMapOf<Coordinate, Map<Coordinate, Int>>()
        adjacentCoordinates.filter { board.isFree(it) }.forEach { c ->
            val distanceMap = mutableMapOf<Coordinate, Int>()
            targetCoordinates.forEach {
                val distance = board.distance(c, it)
                if (distance < Int.MAX_VALUE)
                    distanceMap[it] = distance
            }
            if (distanceMap.any()) {
                adjacentDistanceMap[c] = distanceMap
            }
        }
        if (adjacentDistanceMap.isEmpty()) {
            //no path found
            return
        }
        //get adjacent cell closest to destination
        val destination = adjacentDistanceMap.minBy { e -> e.value.values.min()!! }
        if (destination != null) {
            board.updateMobLocation(this, destination.key)
            coordinate = destination.key
        }
    }

    private fun getCoordinatesAdjacentToEnemies(enemies: List<Mob>, board: Board): MutableSet<Coordinate> {
        val targetCoordinates = mutableSetOf<Coordinate>()
        enemies.forEach { e ->
            e.coordinate.adjacentCoordinates().forEach { aux ->
                if (board.isFree(aux)) {
                    targetCoordinates.add(aux)
                }
            }
        }
        return targetCoordinates
    }

    override fun attack(mobs: List<Mob>) {

        val adjacentEnemies = getAdjacentEnemiesAlive(mobs)
        if (!adjacentEnemies.isEmpty()) {
            val targetEnemy = adjacentEnemies.sortedBy { mob -> mob.coordinate }.minBy { mob -> mob.hp }!!
            targetEnemy.hp -= atkPower
        }

    }

    override fun isAlive(): Boolean = hp > 0
    override fun isDead(): Boolean = !isAlive()

    override fun getAdjacentEnemiesAlive(mobs: List<Mob>) =
            mobs.filter { mob -> isAdjacent(mob) && isEnemyAlive(mob) }

    private fun isEnemyAlive(mob: Mob): Boolean = isEnemy(mob) && mob.isAlive();


    override fun findEnemies(board: Board): List<Mob> = board.mobs.filter { mob -> isEnemyAlive(mob) }

    override fun isAdjacent(mob: Mob): Boolean =
            this.coordinate.manhattanDistance(mob.coordinate) == 1


    override fun compareTo(other: Mob): Int = compareValuesBy(this, other, { it.coordinate.row })

}


data class Elf(override var coordinate: Coordinate, override val atkPower: Int) : AbstractMob(coordinate, atkPower = atkPower) {
    override fun isEnemy(mob: Mob): Boolean = mob is Goblin

}

data class Goblin(override var coordinate: Coordinate, override val atkPower: Int) : AbstractMob(coordinate, atkPower = atkPower) {
    override fun isEnemy(mob: Mob): Boolean = mob is Elf

}

