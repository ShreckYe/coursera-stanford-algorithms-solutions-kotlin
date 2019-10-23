package shreckye.coursera.algorithms

import java.io.File
import java.util.*
import kotlin.test.assertEquals

val question1 = args[0]
val K = 4

val INDEX_LABEL_OFFSET = 1
val (numVertices, distanceMatrix) = File(question1).bufferedReader().use {
    val numNodes = it.readLine().toInt()
    val distanceMatrix = SymmetricDistanceMatrix(numNodes)
    it.forEachLine {
        val weightedEdgeData = it.splitToWeightedEdgeData()
        val vertex1 = weightedEdgeData.vertex1Label - INDEX_LABEL_OFFSET
        val vertex2 = weightedEdgeData.vertex2Label - INDEX_LABEL_OFFSET
        distanceMatrix[vertex1, vertex2] = weightedEdgeData.weight
    }
    numNodes to distanceMatrix
}


fun maximumSpacing(numVertices: Int, distanceMatrix: SymmetricDistanceMatrix, k: Int): Int {
    val clusterUnionFind = UnionFind(numVertices)
    val minDistanceEdgeHeap = PriorityQueue(compareBy(WeightedEdge::weight))
    minDistanceEdgeHeap.addAll(distanceMatrix.asEdgeSequence().toList())

    var numClusters = numVertices
    while (numClusters > k) {
        val minDistanceEdge = minDistanceEdgeHeap.poll()

        if (clusterUnionFind.findAndUnionIfNecessary(minDistanceEdge.vertex1, minDistanceEdge.vertex2))
            numClusters--
    }
    assertEquals(k, clusterUnionFind.getSets().size)

    return distanceMatrix.asEdgeSequence().filter {
        clusterUnionFind.areInDifferentSets(it.vertex1, it.vertex2)
    }.map(WeightedEdge::weight).min()!!
}

println(maximumSpacing(numVertices, distanceMatrix, K))