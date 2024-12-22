package day22

import utils.getInput

fun main() {
    day22part1(getInput(22, true, 1))
    day22part1(getInput(22, false, 1))
    day22part2(getInput(22, true, 2))
    day22part2(getInput(22, false, 2))
}

private fun day22part1(lines: List<String>) {
    println(lines.sumOf { generateSequence(it.toLong(), ::nextSecret).drop(2000).first() })
}

private fun day22part2(lines: List<String>) {
    val pricesByChange = lines.map { priceByChanges(it) }
    val possibleChanges = pricesByChange.asSequence().flatMap { it.keys }.distinct()
    println(possibleChanges.maxOf { change -> pricesByChange.sumOf { it[change] ?: 0 } })
}

private fun nextSecret(num: Long): Long {
    var secret = num
    secret = secret xor (secret * 64)
    secret %= MOD
    secret = secret xor (secret / 32)
    secret %= MOD
    secret = secret xor (secret * 2048)
    secret %= MOD
    return secret
}

private const val MOD = 16777216

private data class ChangeSequence(val first: Int, val second: Int, val third: Int, val fourth: Int)

private fun priceByChanges(line: String): Map<ChangeSequence, Int> {
    val result = mutableMapOf<ChangeSequence, Int>()
    val seed = line.toLong()
    val digits = generateSequence(seed, ::nextSecret).take(2001).map { (it % 10).toInt() }.toList()
    var (first, second, third, fourth) = digits.asSequence().zipWithNext { a, b -> b - a }.take(4).toList()
    result[ChangeSequence(first, second, third, fourth)] = digits[4]
    for ((a, b) in digits.asSequence().drop(4).zipWithNext()) {
        first = second
        second = third
        third = fourth
        fourth = b - a
        result.putIfAbsent(ChangeSequence(first, second, third, fourth), b)
    }
    return result
}