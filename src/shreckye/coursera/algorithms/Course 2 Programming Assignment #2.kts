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

val INITIAL_DISTANCE = 1000000

val shortestDistances =
    dijkstraShortestPathDistances(verticesEdges, NUM_VERTICES, 1 - INDEX_LABEL_OFFSET, INITIAL_DISTANCE)
println(ANSWER_VERTICES.map { shortestDistances[it - INDEX_LABEL_OFFSET] }.joinToString(","))