package com.github.unqualsevol.adventofcode2018.day15.model

import kotlin.math.absoluteValue

data class Coordinate(val row: Int, val column: Int) : Comparable<Coordinate> {
    override fun compareTo(other: Coordinate): Int {
        return compareValuesBy(this, other, { it.row }, { it.column })
    }

    fun manhattanDistance(coordinate: Coordinate): Int {
        return (this.row - coordinate.row).absoluteValue + (this.column - coordinate.column).absoluteValue
    }

    fun adjacentCoordinates(): List<Coordinate> = listOf(Coordinate(row - 1, column),
            Coordinate(row, column - 1),
            Coordinate(row, column + 1),
            Coordinate(row + 1, column))
}