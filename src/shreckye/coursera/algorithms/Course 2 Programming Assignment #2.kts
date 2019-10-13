package shreckye.coursera.algorithms

import java.io.File

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
val vertexShortestDistanceHeap = CustomPriorityQueue(NUM_VERTICES, compareBy(VertexDistance::distance))
val vertexDistanceHeapHolders = arrayOfNulls<CustomPriorityQueue.Holder<VertexDistance>>(NUM_VERTICES)

shortestDistances[fromVertex] = 0
vertexDistanceHeapHolders[fromVertex] = vertexShortestDistanceHeap.offerAndGetHolder(VertexDistance(fromVertex, 0))
repeat(NUM_VERTICES) {
    val (vertex, distance) = vertexShortestDistanceHeap.poll()

    for (edge in verticesEdges[vertex]) {
        val head = edge.otherVertex
        val headDistance = distance + edge.weight
        if (shortestDistances[head] == INITIAL_DISTANCE) {
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

println(ANSWER_VERTICES.map { shortestDistances[it - INDEX_LABEL_OFFSET] }.joinToString(","))