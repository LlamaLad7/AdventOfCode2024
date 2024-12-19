package day19

import utils.getInput

fun main() {
    day19part1(getInput(19, true, 1))
    day19part1(getInput(19, false, 1))
    day19part2(getInput(19, true, 2))
    day19part2(getInput(19, false, 2))
}

private fun day19part1(lines: List<String>) {
    val (patterns, designs) = parseInput(lines)
    println(designs.count { canMake(it, patterns) })
}

private fun day19part2(lines: List<String>) {
    val (patterns, designs) = parseInput(lines)
    println(designs.sumOf { numWays(it, patterns) })
}

private fun canMake(design: String, patterns: Set<String>, cache: MutableMap<String, Boolean> = hashMapOf()): Boolean =
    cache.getOrPut(design) {
        design.isEmpty() ||
                (1..design.length).any { design.take(it) in patterns && canMake(design.drop(it), patterns, cache) }
    }

private fun numWays(design: String, patterns: Set<String>, cache: MutableMap<String, Long> = hashMapOf()): Long =
    cache.getOrPut(design) {
        if (design.isEmpty()) {
            1
        } else {
            (1..design.length).sumOf {
                if (design.take(it) !in patterns) {
                    0
                } else {
                    numWays(design.drop(it), patterns, cache)
                }
            }
        }
    }

private fun parseInput(lines: List<String>) = lines.first().splitToSequence(", ").toSet() to lines.drop(2)