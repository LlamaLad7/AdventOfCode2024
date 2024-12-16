package day16

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import utils.getInput
import java.util.*

fun main() {
    day16part1(getInput(16, true, 1))
    day16part1(getInput(16, false, 1))
    day16part2(getInput(16, true, 2))
    day16part2(getInput(16, false, 2))
}

private fun day16part1(lines: List<String>) {
    val start = lines.find('S')
    val end = lines.find('E')

    val queue = PriorityQueue<ToVisit>()
    val bestScores = hashMapOf<State, Int>()
    queue.add(ToVisit(0, State(start, Direction.RIGHT)))

    while (queue.isNotEmpty()) {
        val (score, state) = queue.remove()
        val (pos, facing) = state
        if (pos == end) {
            println(score)
            return
        }

        if (bestScores[state].let { it != null && it <= score }) {
            continue
        }
        bestScores[state] = score

        queue.add(ToVisit(score + 1000, State(pos, facing.ccw())))
        queue.add(ToVisit(score + 1000, State(pos, facing.cw())))
        if (lines[pos.move(facing)] != '#') {
            queue.add(ToVisit(score + 1, State(pos.move(facing), facing)))
        }
    }
    error("End is unreachable!")
}

private fun day16part2(lines: List<String>) {
    val start = lines.find('S')
    val end = lines.find('E')

    val queue = PriorityQueue<ToVisit2>()
    val bestScores = hashMapOf<State, Int>()
    queue.add(ToVisit2(0, persistentListOf(start), State(start, Direction.RIGHT)))

    var bestScore = Int.MAX_VALUE
    val interestingSquares = hashSetOf<Point>()

    while (queue.isNotEmpty()) {
        val (score, history, state) = queue.remove()
        val (pos, facing) = state
        if (pos == end) {
            if (score <= bestScore) {
                bestScore = score
                interestingSquares.addAll(history)
            } else {
                break
            }
        }

        if (bestScores[state].let { it != null && it < score }) {
            continue
        }
        bestScores[state] = score

        queue.add(ToVisit2(score + 1000, history, State(pos, facing.ccw())))
        queue.add(ToVisit2(score + 1000, history, State(pos, facing.cw())))
        val forward = pos.move(facing)
        if (lines[forward] != '#') {
            queue.add(ToVisit2(score + 1, history.add(forward), State(forward, facing)))
        }
    }

    println(interestingSquares.size)
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

private data class ToVisit(val score: Int, val state: State) : Comparable<ToVisit> {
    override fun compareTo(other: ToVisit) = score.compareTo(other.score)
}

private data class ToVisit2(val score: Int, val history: PersistentList<Point>, val state: State) :
    Comparable<ToVisit2> {
    override fun compareTo(other: ToVisit2) = score.compareTo(other.score)
}

private data class State(val pos: Point, val facing: Direction)

private data class Point(val x: Int, val y: Int) {
    fun move(direction: Direction) = Point(x + direction.dx, y + direction.dy)
}

private enum class Direction(val dx: Int, val dy: Int) {
    LEFT(-1, 0),
    UP(0, -1),
    RIGHT(1, 0),
    DOWN(0, 1);

    fun ccw() = entries.getOrNull(ordinal - 1) ?: DOWN
    fun cw() = entries.getOrNull(ordinal + 1) ?: LEFT
}

private operator fun List<String>.get(point: Point) = this[point.y][point.x]