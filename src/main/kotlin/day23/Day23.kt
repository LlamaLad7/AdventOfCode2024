package day23

import org.jgrapht.alg.clique.BronKerboschCliqueFinder
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.graph.DefaultUndirectedGraph
import utils.getInput

fun main() {
    day23part1(getInput(23, true, 1))
    day23part1(getInput(23, false, 1))
    day23part2(getInput(23, true, 2))
    day23part2(getInput(23, false, 2))
}

private fun day23part1(lines: List<String>) {
    val graph = Graph(lines)
    var total = 0
    graph.forEachTriangle { a, b, c ->
        if (a[0] == 't' || b[0] == 't' || c[0] == 't') {
            total++
        }
    }
    println(total)
}

private fun day23part2(lines: List<String>) {
    val graph = DefaultUndirectedGraph<String, _>(DefaultEdge::class.java)
    for (line in lines) {
        val (a, b) = line.split('-')
        graph.addVertex(a)
        graph.addVertex(b)
        graph.addEdge(a, b)
    }
    println(BronKerboschCliqueFinder(graph).maximumIterator().next().sorted().joinToString(","))
}

private class Graph(lines: List<String>) {
    private val connections = hashMapOf<String, MutableSet<String>>()

    init {
        for (line in lines) {
            val (a, b) = line.split('-')
            connections.getOrPut(a, ::hashSetOf).add(b)
            connections.getOrPut(b, ::hashSetOf).add(a)
        }
    }

    fun forEachTriangle(consumer: (String, String, String) -> Unit) {
        for ((first, connected) in connections) {
            for (second in connected.asSequence().filter { it > first }) {
                for (third in connections[second].orEmpty().asSequence().filter { it > second }) {
                    if (third in connections[first].orEmpty()) {
                        consumer(first, second, third)
                    }
                }
            }
        }
    }
}