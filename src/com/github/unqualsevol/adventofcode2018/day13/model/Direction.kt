package com.github.unqualsevol.adventofcode2018.day13.model

enum class Direction {
    N {
        override fun moveBackward(position: Coordinate, delta: Int): Coordinate {
            return Coordinate(position.x, position.y + delta)
        }

        override fun moveForward(position: Coordinate, delta: Int): Coordinate {
            return Coordinate(position.x, position.y - delta)
        }

        override fun turnLeft(): Direction {
            return W
        }

        override fun turnRight(): Direction {
            return E
        }
    },
    E {
        override fun moveBackward(position: Coordinate, delta: Int): Coordinate {
            return Coordinate(position.x - delta, position.y)
        }

        override fun moveForward(position: Coordinate, delta: Int): Coordinate {
            return Coordinate(position.x + delta, position.y)
        }

        override fun turnLeft(): Direction {
            return N
        }

        override fun turnRight(): Direction {
            return S
        }
    },
    S {
        override fun moveBackward(position: Coordinate, delta: Int): Coordinate {
            return Coordinate(position.x, position.y - delta)
        }

        override fun moveForward(position: Coordinate, delta: Int): Coordinate {
            return Coordinate(position.x, position.y + delta)
        }

        override fun turnLeft(): Direction {
            return E
        }

        override fun turnRight(): Direction {
            return W
        }
    },
    W {
        override fun moveBackward(position: Coordinate, delta: Int): Coordinate {
            return Coordinate(position.x + delta, position.y)
        }

        override fun moveForward(position: Coordinate, delta: Int): Coordinate {
            return Coordinate(position.x - delta, position.y)
        }

        override fun turnLeft(): Direction {
            return S
        }

        override fun turnRight(): Direction {
            return N
        }
    };

    abstract fun turnRight(): Direction

    abstract fun turnLeft(): Direction

    abstract fun moveForward(position: Coordinate, delta: Int): Coordinate

    abstract fun moveBackward(position: Coordinate, delta: Int): Coordinate
}