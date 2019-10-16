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

fun readUndirectedGraphVerticesArrayListSimplifiedEdgesFromEdges(
    filename: String, indexLabelOffset: Int
): Triple<Int, Int, VerticesArrayListSimplifiedEdges> {
    File(filename).bufferedReader().use {
        val (numNodes, numEdges) = it.readLine().splitToInts()

        val verticesEdges = VerticesArrayListSimplifiedEdges(numNodes) { ArrayList() }
        it.forEachLine {
            val weighetdEdgeData = it.splitToWeightedEdgeData()
            val vertex1 = weighetdEdgeData.vertex1Label - indexLabelOffset
            val vertex2 = weighetdEdgeData.vertex2Label - indexLabelOffset
            val cost = weighetdEdgeData.weight
            verticesEdges[vertex1].add(WeightedSimplifiedEdge(vertex2, cost))
            verticesEdges[vertex2].add(WeightedSimplifiedEdge(vertex1, cost))
        }

        return Triple(numNodes, numEdges, verticesEdges)
    }
}

fun readDirectedGraphVerticesArrayListSimplifiedEdgesAndInverseFromEdges(
    filename: String, indexLabelOffset: Int
): Triple<Int, Int, Pair<VerticesArrayListSimplifiedEdges, VerticesArrayListSimplifiedEdges>> {
    File(filename).bufferedReader().use {
        val (numNodes, numEdges) = it.readLine().splitToInts()

        val verticesEdges = VerticesArrayListSimplifiedEdges(numNodes) { ArrayList() }
        val inverseGraphVerticesEdges = VerticesArrayListSimplifiedEdges(numNodes) { ArrayList() }
        it.forEachLine {
            val weighetdEdgeData = it.splitToWeightedEdgeData()
            val vertex1 = weighetdEdgeData.vertex1Label - indexLabelOffset
            val vertex2 = weighetdEdgeData.vertex2Label - indexLabelOffset
            val cost = weighetdEdgeData.weight
            verticesEdges[vertex1].add(WeightedSimplifiedEdge(vertex2, cost))
            inverseGraphVerticesEdges[vertex2].add(WeightedSimplifiedEdge(vertex1, cost))
        }

        return Triple(numNodes, numEdges, verticesEdges to inverseGraphVerticesEdges)
    }
}