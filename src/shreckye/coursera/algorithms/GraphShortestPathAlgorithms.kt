package shreckye.coursera.algorithms

import kotlin.math.min

/* A number representing positive infinity in all these algorithms
When running algorithms, assuming all path distances in the graph are strictly smaller than this value */
const val POSITIVE_INFINITY_INT = Int.MAX_VALUE / 2

data class VertexDistance(val vertex: Int, val distance: Int)

fun dijkstraShortestPathDistances(
    verticesEdges: VerticesSimplifiedEdges,
    numVertices: Int,
    fromVertex: Int,
    initialDistance: Int = POSITIVE_INFINITY_INT
): IntArray {
    val shortestDistances = IntArray(numVertices) { initialDistance }

    val vertexShortestDistanceHeap = CustomPriorityQueue(numVertices, compareBy(VertexDistance::distance))
    val vertexDistanceHeapHolders = arrayOfNulls<CustomPriorityQueue.Holder<VertexDistance>>(numVertices)

    shortestDistances[fromVertex] = 0
    vertexDistanceHeapHolders[fromVertex] = vertexShortestDistanceHeap.offerAndGetHolder(VertexDistance(fromVertex, 0))
    repeat(numVertices) {
        val (vertex, distance) = vertexShortestDistanceHeap.poll()
        vertexDistanceHeapHolders[vertex] = null

        for (edge in verticesEdges[vertex]) {
            val head = edge.otherVertex
            val headDistance = distance + edge.weight
            if (shortestDistances[head] == initialDistance) {
                shortestDistances[head] = headDistance
                vertexDistanceHeapHolders[head] =
                    vertexShortestDistanceHeap.offerAndGetHolder(VertexDistance(head, headDistance))
            } else if (headDistance < shortestDistances[head]) {
                shortestDistances[head] = headDistance
                vertexDistanceHeapHolders[head] = vertexShortestDistanceHeap.replaceByHolder(
                    vertexDistanceHeapHolders[head]!!,
                    VertexDistance(head, headDistance)
                )
            }
        }
    }
    assert(vertexShortestDistanceHeap.isEmpty()) { "size = ${vertexShortestDistanceHeap.size}" }
    return shortestDistances
}

fun bellmanFordShortestPathDistances(
    inverseGraphVerticesEdges: VerticesSimplifiedEdges, numVertices: Int, fromVertex: Int
): IntArray? {
    var lastShortestDistances = IntArray(numVertices) { POSITIVE_INFINITY_INT }
    lastShortestDistances[fromVertex] = 0

    var currentShortestDistances = IntArray(numVertices)
    for (i in 1 until numVertices) {
        var isUpdated = false
        for (v in 0 until numVertices) {
            val lastShortestDistance = lastShortestDistances[v]
            val newShortestDistance = inverseGraphVerticesEdges[v].map { (otherVertex, weight) ->
                lastShortestDistances[otherVertex] + weight
            }.min()

            if (newShortestDistance !== null && newShortestDistance < lastShortestDistance) {
                currentShortestDistances[v] = newShortestDistance
                isUpdated = true
            } else
                currentShortestDistances[v] = lastShortestDistance
        }
        if (!isUpdated)
            return lastShortestDistances

        val temp = lastShortestDistances
        lastShortestDistances = currentShortestDistances
        currentShortestDistances = temp
    }

    // There is a negative cycle
    return null
}

fun floydWarshallAllPairsShortestPathDistances(
    inverseGraphVerticesEdges: VerticesSimplifiedEdges, numVertices: Int
): Array<IntArray>? {
    val shortestDistances = Array(numVertices) { IntArray(numVertices) { POSITIVE_INFINITY_INT } }
    for (i in 0 until numVertices)
        shortestDistances[i][i] = 0
    for ((vertex, vertexEdges) in inverseGraphVerticesEdges.withIndex())
        for ((otherVertex, weight) in vertexEdges)
            shortestDistances[otherVertex][vertex] = weight

    for (k in 0 until numVertices) {
        for (i in 0 until numVertices)
            for (j in 0 until numVertices)
                shortestDistances[i][j] =
                    min(shortestDistances[i][j], shortestDistances[i][k] + shortestDistances[k][j])
        if ((0 until numVertices).any { shortestDistances[it][it] < 0 })
            return null
    }

    return shortestDistances
}

fun johnsonAllPairsShortestPathDistances(
    verticesEdges: VerticesSimplifiedEdges, inverseGraphVerticesEdges: VerticesSimplifiedEdges, numVertices: Int
): Array<IntArray>? {
    // Use the Bellman-Ford algorithm to reweight edges
    val reweightingVertex = numVertices
    val reweightingVertexInverseEdge = WeightedSimplifiedEdge(reweightingVertex, 0)
    val reweightingInverseGraphVerticesEdges =
        (inverseGraphVerticesEdges.map { it + reweightingVertexInverseEdge } + listOf(emptyList())).toTypedArray()

    val reweightingShortestDistances =
        bellmanFordShortestPathDistances(reweightingInverseGraphVerticesEdges, numVertices + 1, reweightingVertex)
    if (reweightingShortestDistances === null) return null

    val reweightedVerticesEdges = verticesEdges.mapIndexed { vertex, edges ->
        edges.map { (otherVertex, weight) ->
            WeightedSimplifiedEdge(
                otherVertex,
                (weight + reweightingShortestDistances[vertex] - reweightingShortestDistances[otherVertex])
                    .also { assert(it >= 0) }
            )
        }
    }.toTypedArray()


    // Run Dijkstra's algorithm from all vertices
    return (0 until numVertices)
        .map { tail ->
            dijkstraShortestPathDistances(reweightedVerticesEdges, numVertices, tail).mapIndexed { head, distance ->
                distance - reweightingShortestDistances[tail] + reweightingShortestDistances[head]
            }.toIntArray()
        }.toTypedArray()
}