package shreckye.coursera.algorithms

import java.util.*
import kotlin.collections.ArrayList

val filename = args[0]

class Vertex(val label: Int, val edges: MutableList<Edge>)
class Edge(var vertex1: Vertex, var vertex2: Vertex)

/*@Suppress("NOTHING_TO_INLINE")
inline fun <T> Array<T>.getVertex(vertexLabel: Int) = get(vertexLabel - 1)

@Suppress("NOTHING_TO_INLINE")
inline fun <T> Array<T>.setVertex(vertexLabel: Int, vertex: T) = get(vertexLabel - 1)

@Suppress("NOTHING_TO_INLINE")
inline fun <T> Array<T>.withVertexLabel() =
    withIndex().map { (it.index + 1) to it.value }*/

val verticesLabels = readAdjacencyList(filename, 200)
val INDEX_LABEL_OFFSET = 1

fun createGraph(
    verticesLabelsSortedByLabel: List<VertexLabels>,
    indexLabelOffset: Int
): Pair<Array<Vertex>, ArrayList<Edge>> {
    val vertices = Array(verticesLabelsSortedByLabel.size) {
        Vertex(it + indexLabelOffset, LinkedList())
    }

    fun Array<Vertex>.getVertex(label: Int) = this[label - indexLabelOffset]
    val edges = ArrayList<Edge>(verticesLabelsSortedByLabel.sumBy { it.second.size } / 2)

    for ((label, adjacentLabels) in verticesLabelsSortedByLabel) {
        val vertex = vertices.getVertex(label)
        vertex.edges.addAll(adjacentLabels.filter { it > label }.map {
            val adjacentVertex = vertices.getVertex(it)
            val edge = Edge(vertex, adjacentVertex)
            adjacentVertex.edges.add(edge)
            edges.add(edge)
            edge
        })
    }

    return vertices to edges
}

val n = verticesLabels.size

var minNumCutEdges = Int.MAX_VALUE
for (i in 0 until n * n) {
    println("${i}st iteration")
    val (vertices, edges) = createGraph(verticesLabels, INDEX_LABEL_OFFSET)
    val vertexMap = vertices.associateBy(Vertex::label).toMutableMap()

    while (vertexMap.size > 2) {
        val edgeToContract = edges.random()
        val vertex1 = edgeToContract.vertex1
        val vertex2 = edgeToContract.vertex2

        fun removeFilter(edge: Edge) =
            (edge.vertex1 === vertex1 && edge.vertex2 === vertex2) || (edge.vertex1 === vertex2 && edge.vertex2 === vertex1)

        vertex1.edges.removeIf(::removeFilter)
        vertex2.edges.removeIf(::removeFilter)
        edges.removeIf(::removeFilter)

        for (edge in vertex2.edges) {
            if (edge.vertex1 === vertex2)
                edge.vertex1 = vertex1
            if (edge.vertex2 === vertex2)
                edge.vertex2 = vertex1
        }
        vertex1.edges.addAll(vertex2.edges)

        vertexMap.remove(vertex2.label)
    }
    //assert(vertexMap.size == 2 && vertexMap.values.iterator().run { next().edges.toSet() == next().edges.toSet() })
    val numCutEdges = vertexMap.values.first().edges.size
    if (numCutEdges < minNumCutEdges) minNumCutEdges = numCutEdges
    println("numCutEdges = $numCutEdges, minNumCutEdges = $minNumCutEdges")
}

println("With more than 1 - 1/e confidence: $minNumCutEdges")
