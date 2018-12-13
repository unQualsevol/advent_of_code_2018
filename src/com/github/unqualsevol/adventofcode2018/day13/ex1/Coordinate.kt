package com.github.unqualsevol.adventofcode2018.day13.ex1

data class Coordinate(val x: Int, val y: Int): Comparable<Coordinate> {
    override fun compareTo(other: Coordinate): Int {
        val dX = this.x - other.x
        if(dX == 0){
            return this.y - other.y
        }
        return dX
    }
}