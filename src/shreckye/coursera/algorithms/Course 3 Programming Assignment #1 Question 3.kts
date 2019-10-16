package shreckye.coursera.algorithms

import java.util.*

val filename = args[0]
val INDEX_LABEL_OFFSET = 1
val (numVertices, _, verticesEdges) = readUndirectedGraphVerticesArrayListSimplifiedEdgesFromEdges(
    filename,
    INDEX_LABEL_OFFSET
)

data class VertexCost(val vertex: Int, val cost: Int)

fun mstOverallCost(verticesEdges: VerticesArrayListSimplifiedEdges, numVertices: Int, initialVertex: Int): Int {
    var mstOverallCost = 0
    val vertexCostHeap = CustomPriorityQueue(numVertices, compareBy(VertexCost::cost))
    val vertexCostHeapHolders = arrayOfNulls<CustomPriorityQueue.Holder<VertexCost>>(numVertices)
    val spanned = BitSet(numVertices)
    val INITIAL_COST = Int.MAX_VALUE
    val minCosts = IntArray(numVertices) { INITIAL_COST }

    minCosts[initialVertex] = Int.MIN_VALUE
    vertexCostHeapHolders[initialVertex] = vertexCostHeap.offerAndGetHolder(VertexCost(initialVertex, 0))
    repeat(numVertices) {
        val (vertex, cost) = vertexCostHeap.poll()
        vertexCostHeapHolders[vertex] = null
        spanned[vertex] = true
        mstOverallCost += cost

        for ((head, headCost) in verticesEdges[vertex]) {
            /* Must check whether it's spanned already,
            because minCosts at a spanned vertex stores a min cost from previously spanned vertex sets to this vertex,
            thus might be greater than current cost.
            This is different from the implementation of Dijkstra's algorithm where we can ignore checking. */
            if (!spanned[head]) {
                //println("$vertex $head $headCost ${minCosts[head]}")
                if (minCosts[head] == INITIAL_COST) {
                    minCosts[head] = headCost
                    vertexCostHeapHolders[head] = vertexCostHeap.offerAndGetHolder(VertexCost(head, headCost))
                } else if (headCost < minCosts[head]) {
                    minCosts[head] = headCost
                    vertexCostHeapHolders[head] =
                        vertexCostHeap.replaceByHolder(vertexCostHeapHolders[head]!!, VertexCost(head, headCost))
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