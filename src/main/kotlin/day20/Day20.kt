package day20

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import utils.getInput
import kotlin.math.abs

fun main() {
    day20part1(getInput(20, true, 1))
    day20part1(getInput(20, false, 1))
    day20part2(getInput(20, true, 2))
    day20part2(getInput(20, false, 2))
}

private fun day20part1(lines: List<String>) {
    val path = findPath(lines, lines.find('S'), lines.find('E'))
    val progress = path.withIndex().associate { (i, pos) -> pos to i }
    val cheats100 = hashSetOf<Pair<Point, Point>>()
    for ((time, pos) in path.withIndex()) {
        for (cheatStart in pos.adjacent()) {
            for (cheatEnd in cheatStart.adjacent()) {
                val endTime = progress[cheatEnd] ?: continue
                val saved = endTime - time - 2
                if (saved >= 100) {
                    cheats100.add(cheatStart to cheatEnd)
                }
            }
        }
    }
    println(cheats100.size)
}

private fun day20part2(lines: List<String>) {
    val path = findPath(lines, lines.find('S'), lines.find('E'))
    val progress = path.withIndex().associate { (i, pos) -> pos to i }
    val cheats100 = hashSetOf<Pair<Point, Point>>()
    for ((cheatStart, startTime) in progress) {
        for ((cheatEnd, endTime) in progress) {
            val dist = cheatStart.manhattanDist(cheatEnd)
            if (dist <= 20) {
                val saved = endTime - startTime - dist
                if (saved >= 100) {
                    cheats100.add(cheatStart to cheatEnd)
                }
            }
        }
    }
    println(cheats100.size)
}

private fun findPath(lines: List<String>, start: Point, end: Point): List<Point> {
    val seen = hashSetOf(start)
    val queue = ArrayDeque(listOf(ToVisit(start, persistentListOf(start))))
    while (queue.isNotEmpty()) {
        val (pos, history) = queue.removeFirst()
        if (pos == end) {
            return history
        }
        for (adj in pos.adjacent()) {
            if (lines[adj.y][adj.x] != '#' && seen.add(adj)) {
                queue.add(ToVisit(adj, history.add(adj)))
            }
        }
    }
    error("Unsolvable!")
}

private fun List<String>.find(char: Char): Point {
    for ((y, row) in this.withIndex()) {
        for ((x, c) in row.withIndex()) {
            if (char == c) {
                return Point(x, y)
            }
        }
    }
    error("Could not find $char")
}

private data class ToVisit(val pos: Point, val history: PersistentList<Point>)

private data class Point(val x: Int, val y: Int) {
    fun move(direction: Direction) = Point(x + direction.dx, y + direction.dy)
    fun adjacent() = Direction.entries.asSequence().map(this::move)
    fun manhattanDist(other: Point) = abs(x - other.x) + abs(y - other.y)
}

private enum class Direction(val dx: Int, val dy: Int) {
    LEFT(-1, 0),
    UP(0, -1),
    RIGHT(1, 0),
    DOWN(0, 1);
}