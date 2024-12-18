package day18

import utils.getInput
import utils.ints

fun main() {
    day18part1(getInput(18, true, 1), true)
    day18part1(getInput(18, false, 1), false)
    day18part2(getInput(18, true, 2), true)
    day18part2(getInput(18, false, 2), false)
}

private fun day18part1(lines: List<String>, test: Boolean) {
    val blocked = lines.points().take(if (test) 12 else 1024).toHashSet()
    println(bfs(blocked, test))
}

private fun day18part2(lines: List<String>, test: Boolean) {
    val blocked = hashSetOf<Point>()
    for (point in lines.points()) {
        blocked.add(point)
        if (bfs(blocked, test) == null) {
            println("${point.x},${point.y}")
            break
        }
    }
}

private fun bfs(blocked: Set<Point>, test: Boolean): Int? {
    val end = if (test) 6 else 70

    val seen = hashSetOf(Point(0, 0))
    val queue = ArrayDeque(listOf(ToVisit(0, Point(0, 0))))
    while (queue.isNotEmpty()) {
        val (dist, pos) = queue.removeFirst()
        if (pos.x == end && pos.y == end) {
            return dist
        }
        for (adj in pos.adjacent().filter { (x, y) -> x in 0..end && y in 0..end }.filter { it !in blocked }) {
            if (seen.add(adj)) {
                queue.add(ToVisit(dist + 1, adj))
            }
        }
    }
    return null
}

private data class Point(val x: Int, val y: Int) {
    fun adjacent() = sequenceOf(
        copy(x = x - 1),
        copy(x = x + 1),
        copy(y = y - 1),
        copy(y = y + 1),
    )
}

private data class ToVisit(val dist: Int, val point: Point)

private fun List<String>.points() = asSequence().map { it.ints().let { (x, y) -> Point(x, y) } }