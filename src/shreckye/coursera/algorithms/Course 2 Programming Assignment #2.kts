package shreckye.coursera.algorithms

import java.io.File
import java.util.*

val filename = args[0]
val ANSWER_VERTICES = intArrayOf(7, 37, 59, 82, 99, 115, 133, 165, 188, 197)

val NUM_VERTICES = 200
val INDEX_LABEL_OFFSET = 1

val verticesEdges = VerticesSimplifiedEdges(NUM_VERTICES) { emptyList() }
File(filename).forEachLine {
    val splitted = it.splitWithWhiteSpaceAndFilterEmpty()
    val vertexLabel = splitted.first().toInt()
    verticesEdges[vertexLabel - INDEX_LABEL_OFFSET] = splitted.drop(1).map {
        val splittedEdgeData = it.split(',').filter(String::isNotEmpty)
        WeightedSimplifiedEdge(splittedEdgeData[0].toInt() - INDEX_LABEL_OFFSET, splittedEdgeData[1].toInt())
    }
}


data class VertexDistance(val vertex: Int, val distance: Int)

val INITIAL_DISTANCE = 1000000
val shortestDistances = IntArray(NUM_VERTICES) { INITIAL_DISTANCE }

val fromVertex = 1 - INDEX_LABEL_OFFSET
val vertexShortestDistanceHeap = PriorityQueue(NUM_VERTICES, compareBy(VertexDistance::distance))

shortestDistances[fromVertex] = 0
vertexShortestDistanceHeap.offer(VertexDistance(fromVertex, 0))
repeat(NUM_VERTICES) {
    val (vertex, distance) = vertexShortestDistanceHeap.poll()

    for (edge in verticesEdges[vertex]) {
        val head = edge.otherVertex
        val headDistance = distance + edge.weight
        if (shortestDistances[head] == INITIAL_DISTANCE) {
            shortestDistances[head] = headDistance
            vertexShortestDistanceHeap.offer(VertexDistance(head, headDistance))
        } else if (headDistance < shortestDistances[head]) {
            // TODO: this step takes O(n) time, thus should be optimized.
            assert(vertexShortestDistanceHeap.removeIf { it.vertex == head })
            shortestDistances[head] = headDistance
            vertexShortestDistanceHeap.offer(VertexDistance(head, headDistance))
        }
    }
}
assert(vertexShortestDistanceHeap.isEmpty()) { "size = ${vertexShortestDistanceHeap.size}" }

println(ANSWER_VERTICES.map { shortestDistances[it - INDEX_LABEL_OFFSET] }.joinToString(","))