package day9

import getInput

fun main() {
    day9part1(getInput(9, true, 1))
    day9part1(getInput(9, false, 1))
    day9part2(getInput(9, true, 2))
    day9part2(getInput(9, false, 2))
}

private fun day9part1(lines: List<String>) {
    val nums = parseInput(lines.single())
    var nextEmpty = nums.indexOfFirst { it < 0 }
    var nextFromEnd = nums.indexOfLast { it > 0 }
    while (nextEmpty < nextFromEnd) {
        nums[nextEmpty] = nums[nextFromEnd].also { nums[nextFromEnd] = -1 }
        while (nums[++nextEmpty] > 0) {
        }
        while (nums[--nextFromEnd] < 0) {
        }
    }
    println(nums.checksum())
}

private fun day9part2(lines: List<String>) {
    val nums = parseInput(lines.single())
    var nextFromEnd = nums.indexOfLast { it > 0 }
    for (block in filledBlocks(nums).asReversed()) {
        val id = nums[block.first]
        val size = block.last - block.first + 1

        var valid = false
        var nextEmpty = nums.indexOfFirst { it < 0 }
        while (nextEmpty < block.first) {
            if (nextEmpty + size > nums.size) {
                break
            }
            if (nums.subList(nextEmpty, nextEmpty + size).all { it < 0 }) {
                valid = true
                break
            }
            nextEmpty++
            while (nums[nextEmpty] > 0) {
                nextEmpty++
            }
        }
        if (valid) {
            for (i in nextEmpty..<nextEmpty + size) {
                nums[i] = id
            }
            for (i in block) {
                nums[i] = -1
            }
        }
        nextFromEnd -= size
        while (nextFromEnd > 0 && nums[nextFromEnd] < 0) {
            nextFromEnd--
        }
    }
    println(nums.checksum())
}

private fun parseInput(line: String): MutableList<Int> {
    val result = mutableListOf<Int>()
    var filled = true
    var id = 0
    for (char in line) {
        val toAdd = if (filled) id++ else -1
        repeat(char.digitToInt()) {
            result.add(toAdd)
        }
        filled = !filled
    }
    return result
}

private fun filledBlocks(nums: List<Int>) = buildList {
    var start = -1
    var last = -1
    for ((i, num) in nums.withIndex()) {
        if (num != last) {
            if (last != -1) {
                add(start..<i)
            }
            last = num
            start = i
        }
    }
    if (last != -1) {
        add(start..nums.lastIndex)
    }
}

private fun List<Int>.checksum() = withIndex().sumOf { (i, num) -> if (num < 0) 0L else (num * i).toLong() }