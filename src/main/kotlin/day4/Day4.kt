package day4

import utils.getInput

fun main() {
    day4part1(getInput(4, true, 1))
    day4part1(getInput(4, false, 1))
    day4part2(getInput(4, true, 2))
    day4part2(getInput(4, false, 2))
}

private fun day4part1(lines: List<String>) {
    var total = 0
    for (dy in -1..1) {
        for (dx in -1..1) {
            total += lines.countAppearances("XMAS", dx, dy).count()
        }
    }
    println(total)
}

private fun day4part2(lines: List<String>) {
    val topToBottomCentres = hashSetOf<Point>()
    var count = 0

    for ((dx, dy) in listOf(-1 to -1, 1 to 1)) {
        for ((startX, startY) in lines.countAppearances("MAS", dx, dy)) {
            topToBottomCentres.add(Point(startX + dx, startY + dy))
        }
    }
    for ((dx, dy) in listOf(1 to -1, -1 to 1)) {
        for ((startX, startY) in lines.countAppearances("MAS", dx, dy)) {
            val centre = Point(startX + dx, startY + dy)
            if (centre in topToBottomCentres) {
                count++
            }
        }
    }

    println(count)
}

private fun List<String>.countAppearances(word: String, dx: Int, dy: Int): Sequence<Point> = sequence {
    val grid = this@countAppearances
    val height = grid.size
    val width = grid[0].length

    for (startY in 0..<height) {
        outer@ for (startX in 0..<width) {
            var y = startY
            var x = startX
            for (c in word) {
                if (y !in 0..<height || x !in 0..<width || grid[y][x] != c) {
                    continue@outer
                }
                x += dx
                y += dy
            }
            yield(Point(startX, startY))
        }
    }
}

private data class Point(val x: Int, val y: Int)