package day10

import utils.getInput

fun main() {
    day10part1(getInput(10, true, 1))
    day10part1(getInput(10, false, 1))
    day10part2(getInput(10, true, 2))
    day10part2(getInput(10, false, 2))
}

private fun day10part1(lines: List<String>) {
    var total = 0
    for ((y, row) in lines.withIndex()) {
        for (x in row.indices) {
            total += numEndpoints(lines, Point(x, y), 0)
        }
    }
    println(total)
}

private fun numEndpoints(lines: List<String>, pos: Point, num: Int, seen: MutableSet<Point> = hashSetOf()): Int {
    if (lines[pos].digitToInt() != num || !seen.add(pos)) {
        return 0
    }
    if (num == 9) {
        return 1
    }
    return pos.adjacent(lines).sumOf { numEndpoints(lines, it, num + 1, seen) }
}

private fun day10part2(lines: List<String>) {
    var total = 0
    for ((y, row) in lines.withIndex()) {
        for (x in row.indices) {
            total += numRoutes(lines, Point(x, y), 0)
        }
    }
    println(total)
}

private fun numRoutes(lines: List<String>, pos: Point, num: Int): Int {
    if (lines[pos].digitToInt() != num) {
        return 0
    }
    if (num == 9) {
        return 1
    }
    return pos.adjacent(lines).sumOf { numRoutes(lines, it, num + 1) }
}

private data class Point(val x: Int, val y: Int) {
    fun adjacent(lines: List<String>): Sequence<Point> {
        val height = lines.size
        val width = lines[0].length
        return sequenceOf(
            copy(x = x - 1),
            copy(x = x + 1),
            copy(y = y - 1),
            copy(y = y + 1),
        ).filter { (x, y) -> x in 0..<width && y in 0..<height }
    }
}

private operator fun List<String>.get(point: Point) = this[point.y][point.x]