package shreckye.coursera.algorithms

import java.io.File

typealias VertexLabels = Pair<Int, IntArray>

fun readAdjacencyList(filename: String, initialCapacity: Int): List<VertexLabels> =
    File(filename).useLines {
        it.map {
            val vertexLabelInts = it.splitToInts()
            vertexLabelInts.first() to vertexLabelInts.drop(1).toIntArray()
        }.toCollection(ArrayList(initialCapacity))
    }

data class EdgeData(val vertex1Label: Int, val vertex2Label: Int)
data class WeightedEdgeData(val vertex1Label: Int, val vertex2Label: Int, val weight: Int)

fun String.splitToEdgeData(): EdgeData {
    val lineInts = splitToInts()
    return EdgeData(lineInts[0], lineInts[1])
}

fun String.splitToWeightedEdgeData(): WeightedEdgeData {
    val lineInts = splitToInts()
    return WeightedEdgeData(lineInts[0], lineInts[1], lineInts[2])
}