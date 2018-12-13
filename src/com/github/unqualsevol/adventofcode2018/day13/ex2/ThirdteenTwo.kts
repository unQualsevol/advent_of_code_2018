import com.github.unqualsevol.adventofcode2018.day13.ex1.Coordinate
import com.github.unqualsevol.adventofcode2018.day13.ex1.Direction
import com.github.unqualsevol.adventofcode2018.day13.ex1.Vector
import java.io.File

val map = mutableMapOf<Coordinate, Track>()
var carts = mutableListOf<Cart>()
var y = 0
File("input").forEachLine {
    it.forEachIndexed { x, c ->
        val coordinate = Coordinate(x, y)
        if (isCart(c)) {
            carts.add(Cart(Vector(coordinate, getDirection(c))))
        }
        map[coordinate] = getTrack(c)
    }
    y++
}

mainloop@ while (true) {
    carts.sortBy { cart -> cart.vector.coordinate }
    for (cart in carts) {
        if(cart.isDestroyed()) {
            continue
        }
        cart.moveForward(map)
        val cartsInSameCoordinate = carts.filter { it.vector.coordinate.equals(cart.vector.coordinate) }

        if (cartsInSameCoordinate.size > 1) {
            cartsInSameCoordinate.forEach { c -> c.destroy()}
        }
    }
    val stillAliveCarts = carts.filter { !it.isDestroyed() }
    carts = mutableListOf()
    carts.addAll(stillAliveCarts)
    if(carts.size == 1){
        println(carts)
        break@mainloop
    }
}

fun isCart(c: Char): Boolean {
    return c == '^' || c == 'v' || c == '<' || c == '>'
}


fun getDirection(c: Char): Direction {
    when (c) {
        '^' -> {
            return Direction.N
        }
        'v' -> {
            return Direction.S
        }
        '<' -> {
            return Direction.W
        }
        else -> {
            return Direction.E
        }
    }
}

fun getTrack(c: Char): Track {
    when (c) {
        '|', '^', 'v' -> {
            return VerticalTrack()
        }
        '-', '<', '>' -> {
            return HorizontalTrack()
        }
        '/' -> {
            return SlashTrack()
        }
        '\\' -> {
            return BackSlashTrack()
        }
        else -> {
            return IntersectionTrack()
        }
    }
}


data class Cart(var vector: Vector) {
    private var option: IntersectionOption = IntersectionOption.LEFT
    private var destroyed = false
    fun moveForward(map: Map<Coordinate, Track>) {
        vector = vector.moveForward(1)
        map[vector.coordinate]!!.computeDirection(this)
    }

    fun turnRight() {
        vector = vector.turnRight()
    }

    fun turnLeft() {
        vector = vector.turnLeft()
    }

    fun intersection() {
        option.turn(this)
        option = option.nextOption()
    }

    fun isDestroyed(): Boolean {
        return destroyed
    }

    fun destroy(){
        this.destroyed = true
    }

}

interface Track {
    fun computeDirection(cart: Cart)

}

class VerticalTrack : Track {
    override fun computeDirection(cart: Cart) {
    }

}

class HorizontalTrack : Track {
    override fun computeDirection(cart: Cart) {
    }

}

class SlashTrack : Track {
    override fun computeDirection(cart: Cart) {
        when (cart.vector.direction) {
            Direction.N, Direction.S -> {
                return cart.turnRight()

            }
            else -> {
                return cart.turnLeft()
            }
        }

    }

}

class BackSlashTrack : Track {
    override fun computeDirection(cart: Cart) {
        when (cart.vector.direction) {
            Direction.N, Direction.S -> {
                return cart.turnLeft()

            }
            else -> {
                return cart.turnRight()
            }
        }
    }

}

class IntersectionTrack : Track {
    override fun computeDirection(cart: Cart) {
        cart.intersection()
    }

}

enum class IntersectionOption {
    LEFT {

        override fun nextOption(): IntersectionOption {
            return STRAIGHT
        }

        override fun turn(cart: Cart) {
            cart.turnLeft()
        }
    },
    STRAIGHT {

        override fun nextOption(): IntersectionOption {
            return RIGHT
        }

        override fun turn(cart: Cart) {
        }
    },
    RIGHT {

        override fun nextOption(): IntersectionOption {
            return LEFT
        }

        override fun turn(cart: Cart) {
            cart.turnRight()
        }

    };

    abstract fun nextOption(): IntersectionOption
    abstract fun turn(cart: Cart)

}
