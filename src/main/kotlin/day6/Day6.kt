package day6

import utils.getInput

fun main() {
    day6part1(getInput(6, true, 1))
    day6part1(getInput(6, false, 1))
    day6part2(getInput(6, true, 2))
    day6part2(getInput(6, false, 2))
}

private fun day6part1(lines: List<String>) {
    val board = Board(lines.map { it.toCharArray() })
    println(board.runSimulation().seenPositions)
}

private fun day6part2(lines: List<String>) {
    val newLines = lines.map { it.toCharArray() }
    var found = 0
    for ((y, row) in newLines.withIndex()) {
        for ((x, char) in row.withIndex()) {
            if (char == '.') {
                newLines[y][x] = '#'
                if (Board(newLines).runSimulation().inLoop) {
                    found++
                }
                newLines[y][x] = '.'
            }
        }
    }
    println(found)
}

private class Board(private val lines: List<CharArray>) {
    private val height = lines.size
    private val width = lines[0].size

    fun findStart(): State {
        for ((y, row) in lines.withIndex()) {
            for ((x, char) in row.withIndex()) {
                if (char == '^') {
                    return State(Point(x, y), Direction.UP)
                }
            }
        }
        error("Couldn't find start")
    }

    fun isInBounds(state: State) = state.point.x in 0..<width && state.point.y in 0..<height

    fun isBlocked(state: State): Boolean {
        val newState = state.move()
        return isInBounds(newState) && lines[newState.point.y][newState.point.x] == '#'
    }

    fun runSimulation(): SimResult {
        var state = findStart()
        val seenStates = hashSetOf<State>()
        val seenPositions = hashSetOf<Point>()
        var inLoop = false
        while (isInBounds(state)) {
            if (!seenStates.add(state)) {
                inLoop = true
                break
            }
            seenPositions.add(state.point)
            state = if (isBlocked(state)) {
                state.turn()
            } else {
                state.move()
            }
        }
        return SimResult(seenPositions.size, inLoop)
    }
}

private data class SimResult(val seenPositions: Int, val inLoop: Boolean)

private enum class Direction(val dx: Int, val dy: Int) {
    UP(0, -1),
    RIGHT(1, 0),
    DOWN(0, 1),
    LEFT(-1, 0);

    fun next() = entries[(ordinal + 1) % 4]
}

private data class Point(val x: Int, val y: Int) {
    fun move(direction: Direction) = Point(x + direction.dx, y + direction.dy)
}

private data class State(val point: Point, val direction: Direction) {
    fun move() = State(point.move(direction), direction)
    fun turn() = State(point, direction.next())
}