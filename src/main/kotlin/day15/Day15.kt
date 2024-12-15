package day15

import utils.getInput
import utils.split

fun main() {
    day15part1(getInput(15, true, 1))
    day15part1(getInput(15, false, 1))
    day15part2(getInput(15, true, 2))
    day15part2(getInput(15, false, 2))
}

private fun day15part1(lines: List<String>) {
    val (gridLines, moveLines) = lines.split("")
    val grid = gridLines.map { it.toCharArray() }
    val moves = moveLines.asSequence().flatMap { it.asSequence() }.map(Direction::ofChar)
    var pos = findRobot(grid)

    for (move in moves) {
        pos = tryMove(pos, move, grid)
    }

    println(sumGps(grid, 'O'))
}

private fun day15part2(lines: List<String>) {
    val (gridLines, moveLines) = lines.split("")
    val grid = gridLines.map { doubleWidthLine(it) }
    val moves = moveLines.asSequence().flatMap { it.asSequence() }.map(Direction::ofChar)
    var pos = findRobot(grid)

    for (move in moves) {
        if (canMoveBig(pos, move, grid)) {
            moveBig(pos, move, grid)
            pos = pos.move(move)
        }
    }

    println(sumGps(grid, '['))
}

private fun findRobot(grid: List<CharArray>): Point {
    for ((y, row) in grid.withIndex()) {
        for ((x, char) in row.withIndex()) {
            if (char == '@') {
                return Point(x, y)
            }
        }
    }
    error("Couldn't find robot")
}

private fun tryMove(pos: Point, direction: Direction, grid: List<CharArray>): Point {
    var current = pos
    while (true) {
        current = current.move(direction)
        when (grid[current]) {
            '#' -> return pos
            '.' -> break
        }
    }
    grid[current] = 'O'
    grid[pos] = '.'
    grid[pos.move(direction)] = '@'
    return pos.move(direction)
}

private fun canMoveBig(pos: Point, direction: Direction, grid: List<CharArray>): Boolean {
    val char = grid[pos]
    when (char) {
        '.' -> return true
        '#' -> return false
        '@' -> return canMoveBig(pos.move(direction), direction, grid)
    }
    check(char == '[' || char == ']')

    if (direction.dy == 0) {
        return canMoveBig(pos.move(direction), direction, grid)
    }

    return when (char) {
        '[' -> canMoveBig(pos.move(direction), direction, grid) &&
                canMoveBig(pos.move(Direction.RIGHT).move(direction), direction, grid)
        ']' -> canMoveBig(pos.move(direction), direction, grid) &&
                canMoveBig(pos.move(Direction.LEFT).move(direction), direction, grid)
        else -> error("Invalid char $char")
    }
}

private fun moveBig(pos: Point, direction: Direction, grid: List<CharArray>) {
    val char = grid[pos]
    when (char) {
        '.' -> return
        '#' -> error("Tried to move wall!")
        '[' -> {
            if (direction.dx == 0) {
                moveBig(pos.move(direction), direction, grid)
                moveBig(pos.move(Direction.RIGHT).move(direction), direction, grid)
                grid[pos.move(direction)] = '['
                grid[pos.move(Direction.RIGHT).move(direction)] = ']'
                grid[pos] = '.'
                grid[pos.move(Direction.RIGHT)] = '.'
                return
            }
        }
        ']' -> {
            if (direction.dx == 0) {
                moveBig(pos.move(direction), direction, grid)
                moveBig(pos.move(Direction.LEFT).move(direction), direction, grid)
                grid[pos.move(direction)] = ']'
                grid[pos.move(Direction.LEFT).move(direction)] = '['
                grid[pos] = '.'
                grid[pos.move(Direction.LEFT)] = '.'
                return
            }
        }
    }
    moveBig(pos.move(direction), direction, grid)
    grid[pos.move(direction)] = char
    grid[pos] = '.'
}

private fun doubleWidthLine(line: String): CharArray {
    val result = CharArray(line.length * 2)
    for ((x, char) in line.withIndex()) {
        result[x * 2] = when (char) {
            'O' -> '['
            else -> char
        }
        result[x * 2 + 1] = when (char) {
            'O' -> ']'
            '@' -> '.'
            else -> char
        }
    }
    return result
}

private fun sumGps(grid: List<CharArray>, boxChar: Char): Int {
    var total = 0
    for ((y, row) in grid.withIndex()) {
        for ((x, char) in row.withIndex()) {
            if (char == boxChar) {
                total += 100 * y + x
            }
        }
    }
    return total
}

private data class Point(val x: Int, val y: Int) {
    fun move(direction: Direction) = Point(x + direction.dx, y + direction.dy)
}

private operator fun List<CharArray>.get(point: Point) = this[point.y][point.x]
private operator fun List<CharArray>.set(point: Point, value: Char) {
    this[point.y][point.x] = value
}

private enum class Direction(val dx: Int, val dy: Int) {
    LEFT(-1, 0),
    UP(0, -1),
    RIGHT(1, 0),
    DOWN(0, 1);

    companion object {
        fun ofChar(char: Char) = when (char) {
            '<' -> LEFT
            '^' -> UP
            '>' -> RIGHT
            'v' -> DOWN
            else -> error("Invalid direction $char")
        }
    }
}