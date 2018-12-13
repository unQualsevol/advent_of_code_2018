package com.github.unqualsevol.adventofcode2018.day13.ex1

data class Vector(val coordinate: Coordinate, val direction: Direction) {

    constructor(x:Int, y: Int, direction: String) : this(Coordinate(x, y), Direction.valueOf(direction))
    fun turnRight() = Vector(coordinate, direction.turnRight())
    fun turnLeft() = Vector(coordinate, direction.turnLeft())
    fun moveForward(delta: Int): Vector = Vector(direction.moveForward(coordinate, delta), direction)
    fun moveBackward(delta: Int): Vector = Vector(direction.moveBackward(coordinate, delta), direction)

}