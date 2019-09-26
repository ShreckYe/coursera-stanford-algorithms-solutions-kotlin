package shreckye.coursera.algorithms

import java.io.File
import java.util.*
import kotlin.collections.ArrayList

val filename = args[0]
val INDEX_LABEL_OFFSET = 1
val (numVertices, verticesEdges) = File(filename).bufferedReader().use {
    val (numNodes, numEdges) = it.readLine().splitToInts()

    val verticesEdges = VerticesArrayListSimplifiedEdges(numNodes) { ArrayList() }
    it.forEachLine {
        val weighetdEdgeData = it.splitToWeightedEdgeData()
        val vertex1 = weighetdEdgeData.vertex1Label - INDEX_LABEL_OFFSET
        val vertex2 = weighetdEdgeData.vertex2Label - INDEX_LABEL_OFFSET
        val cost = weighetdEdgeData.weight
        verticesEdges[vertex1].add(WeightedSimplifiedEdge(vertex2, cost))
        verticesEdges[vertex2].add(WeightedSimplifiedEdge(vertex1, cost))
    }
    numNodes to verticesEdges
}


data class VertexCost(val vertex: Int, val cost: Int)

fun mstOverallCost(verticesEdges: VerticesArrayListSimplifiedEdges, numVertices: Int, initialVertex: Int): Int {
    var mstOverallCost = 0
    val vertexCostHeap = PriorityQueue(numVertices, compareBy(VertexCost::cost))
    val spanned = BitSet(numVertices)
    val INITIAL_COST = Int.MAX_VALUE
    val minCosts = IntArray(numVertices) { INITIAL_COST }

    minCosts[initialVertex] = Int.MIN_VALUE
    vertexCostHeap.offer(VertexCost(initialVertex, 0))
    repeat(numVertices) {
        val (vertex, cost) = vertexCostHeap.poll()
        spanned[vertex] = true
        mstOverallCost += cost

        for ((head, headCost) in verticesEdges[vertex]) {
            /* Must check whether it's spanned already,
        because minCosts of a spanned vertex stores a min cost from previously spanned vertex sets to this vertex,
        thus might be greater than current cost.
        This is different from the implementation of Dijkstra's algorithm where we can ignore checking. */
            if (!spanned[head]) {
                //println("$vertex $head $headCost ${minCosts[head]}")
                if (minCosts[head] == INITIAL_COST) {
                    minCosts[head] = headCost
                    vertexCostHeap.offer(VertexCost(head, headCost))
                } else if (headCost < minCosts[head]) {
                    // TODO: this step takes O(n) time, thus should be optimized.
                    assert(vertexCostHeap.removeIf { it.vertex == head })
                    minCosts[head] = headCost
                    vertexCostHeap.offer(VertexCost(head, headCost))
                }
            }
        }
    }
    assert(vertexCostHeap.isEmpty()) { "size = ${vertexCostHeap.size}" }

    return mstOverallCost
}

println(mstOverallCost(verticesEdges, numVertices, 1 - INDEX_LABEL_OFFSET))


// Test cases
(0 until numVertices).map { mstOverallCost(verticesEdges, numVertices, it) }.assertAllEqualsAndGet()