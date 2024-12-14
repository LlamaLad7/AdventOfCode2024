package day12

import getInput

fun main() {
    day12part1(getInput(12, true, 1))
    day12part1(getInput(12, false, 1))
    day12part2(getInput(12, true, 2))
    day12part2(getInput(12, false, 2))
}

private fun day12part1(lines: List<String>) {
    val regions = findRegions(lines)
    println(regions.sumOf { it.area * it.perimeter })
}

private fun day12part2(lines: List<String>) {
    val regions = findRegions(lines)
    var total = 0
    for (region in regions) {
        val visitedLeftStarts = hashSetOf<Point>()
        val externalStart = region.points.minBy { it.x }
        val externalPerimeter = region.countExternalPerimeter(externalStart, visitedLeftStarts::add)
        total += externalPerimeter * region.area
        for (internalStart in region.findLeftStarts().filter { it !in visitedLeftStarts }) {
            val internalPerimeter = region.countInternalPerimeter(internalStart, visitedLeftStarts::add)
            total += internalPerimeter * region.area
        }
    }
    println(total)
}

private fun findRegions(lines: List<String>): List<Region> {
    val regions = mutableListOf<Region>()
    val seen = hashSetOf<Point>()

    fun dfs(pos: Point, region: Region) {
        if (!seen.add(pos)) {
            return
        }
        val char = lines[pos]
        region.points.add(pos)
        for (adj in pos.adjacent()) {
            if (lines.getOrNull(adj) == char) {
                dfs(adj, region)
            } else {
                region.perimeter++
            }
        }
    }

    for ((y, row) in lines.withIndex()) {
        for (x in row.indices) {
            val pos = Point(x, y)
            if (pos !in seen) {
                val region = Region()
                dfs(pos, region)
                regions.add(region)
            }
        }
    }

    return regions
}

private class Region {
    val points: MutableSet<Point> = hashSetOf()
    var perimeter = 0
    val area get() = points.size

    fun findLeftStarts() = points.asSequence().filter { it.move(Direction.LEFT) !in points }

    fun countExternalPerimeter(start: Point, leftStartSink: (Point) -> Unit): Int {
        var facing = Direction.UP
        var pos = start
        var sides = 0

        do {
            check(pos in points)
            check(pos.move(facing.ccw()) !in points)
            if (facing == Direction.UP) {
                leftStartSink(pos)
            }
            if (pos.move(facing) !in points) {
                facing = facing.cw()
                sides++
                continue
            }
            pos = pos.move(facing)
            if (pos.move(facing.ccw()) in points) {
                facing = facing.ccw()
                sides++
                pos = pos.move(facing)
            }
        } while (pos != start || facing != Direction.UP)

        return sides
    }

    fun countInternalPerimeter(start: Point, leftStartSink: (Point) -> Unit): Int {
        var facing = Direction.DOWN
        var pos = start
        var sides = 0

        do {
            check(pos in points)
            check(pos.move(facing.cw()) !in points)
            if (facing == Direction.DOWN) {
                leftStartSink(pos)
            }
            if (pos.move(facing) !in points) {
                facing = facing.ccw()
                sides++
                continue
            }
            pos = pos.move(facing)
            if (pos.move(facing.cw()) in points) {
                facing = facing.cw()
                sides++
                pos = pos.move(facing)
            }
        } while (pos != start || facing != Direction.DOWN)

        return sides
    }
}

private data class Point(val x: Int, val y: Int) {
    fun adjacent() = Direction.entries.asSequence().map(this::move)

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
private fun List<String>.getOrNull(point: Point) = this.getOrNull(point.y)?.getOrNull(point.x)