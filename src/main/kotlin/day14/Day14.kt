package day14

import utils.getInput
import utils.ints

fun main() {
    day14part1(getInput(14, true, 1), true)
    day14part1(getInput(14, false, 1), false)
    day14part2(getInput(14, false, 2))
}

private fun day14part1(lines: List<String>, test: Boolean) {
    val width = if (test) 11 else 101
    val height = if (test) 7 else 103
    val middleX = width / 2
    val middleY = height / 2
    val robots = lines.map(::parseRobot)

    var topLeft = 0
    var bottomLeft = 0
    var topRight = 0
    var bottomRight = 0

    for (robot in robots) {
        val endX = (robot.start.x + 100 * robot.velocity.x).mod(width)
        val endY = (robot.start.y + 100 * robot.velocity.y).mod(height)
        when {
            endX < middleX && endY < middleY -> topLeft++
            endX < middleX && endY > middleY -> bottomLeft++
            endX > middleX && endY < middleY -> topRight++
            endX > middleX && endY > middleY -> bottomRight++
        }
    }
    println(topLeft * bottomLeft * topRight * bottomRight)
}

private fun day14part2(lines: List<String>) {
    val width = 101
    val height = 103
    val robots = lines.map(::parseRobot)

    for (turn in 1..10000) {
        val grid = Array(height) { CharArray(width) { ' ' } }
        for (robot in robots) {
            val endX = (robot.start.x + turn * robot.velocity.x).mod(width)
            val endY = (robot.start.y + turn * robot.velocity.y).mod(height)
            grid[endY][endX] = '*'
        }
        if (grid.any { "****************" in it.concatToString() }) {
            println("Turn $turn")
            println(grid.joinToString("\n") { it.concatToString() })
        }
    }
}

private fun parseRobot(line: String): Robot {
    val (px, py, vx, vy) = line.ints()
    return Robot(Vec2(px, py), Vec2(vx, vy))
}

private data class Robot(val start: Vec2, val velocity: Vec2)
private data class Vec2(val x: Int, val y: Int)