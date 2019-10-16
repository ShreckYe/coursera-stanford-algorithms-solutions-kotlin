package shreckye.coursera.algorithms

import java.util.*

class IndexGraph(val numVertices: Int) {
    val verticesEdges = Array(numVertices) { SimpleIntArrayList() }

    /*fun setEdges(tail: Int, edges: SimpleIntArrayList) {
        verticesEdges[tail] = edges
    }

    fun setEdgesByLabel(tailLabel: Int, edges: SimpleIntArrayList) =
        setEdges(tailLabel - indexLabelOffset, edges)*/

    fun addEdge(tail: Int, head: Int) =
        verticesEdges[tail].add(head)

    fun addEdgeByLabel(tailLabel: Int, headLabel: Int, indexLabelOffset: Int) =
        addEdge(tailLabel - indexLabelOffset, headLabel - indexLabelOffset)

    /*fun getEdgesByIndex(tailIndex: Int) =
        verticesEdges[tailIndex]*/

    fun trimEdges() {
        for (vertexEdges in verticesEdges)
            vertexEdges.trimToSize()
    }

    fun dfsAll(searchedVertices: BitSet = BitSet(numVertices), after: (Int) -> Unit = {}) {
        for (vertex in 0 until numVertices)
            dfsIfUnsearched(vertex, searchedVertices, after)
    }

    fun dfs(vertex: Int, searchedVertices: BitSet = BitSet(numVertices), after: (Int) -> Unit = {}) {
        //println(vertex)
        searchedVertices[vertex] = true

        for (edge in verticesEdges[vertex])
            dfsIfUnsearched(edge, searchedVertices, after)

        after(vertex)
    }

    @Suppress("NOTHING_TO_INLINE")
    inline fun dfsIfUnsearched(vertex: Int, searchedVertices: BitSet, noinline after: (Int) -> Unit = {}) {
        if (!searchedVertices[vertex])
            dfs(vertex, searchedVertices, after)
    }
}

data class Edge(val vertex1: Int, val vertex2: Int)
data class WeightedEdge(val vertex1: Int, val vertex2: Int, val weight: Int)
data class WeightedSimplifiedEdge(val otherVertex: Int, val weight: Int)
// Representing the adjacency list of a index-based graph that's immutable after constructed
typealias VerticesSimplifiedEdges = Array<List<WeightedSimplifiedEdge>>

// Representing a similar adjacency list but it's mutable
typealias VerticesArrayListSimplifiedEdges = Array<ArrayList<WeightedSimplifiedEdge>>

@Suppress("NOTHING_TO_INLINE", "UNCHECKED_CAST")
inline fun VerticesArrayListSimplifiedEdges.asVerticesSimplifiedEdges(): VerticesSimplifiedEdges =
    this as VerticesSimplifiedEdges

class SimpleWeightedGraph {}

@Suppress("EXPERIMENTAL_FEATURE_WARNING")
inline class SymmetricDistanceMatrix(val distances: Array<IntArray>) {
    constructor(numVertices: Int, initialDistance: Int = Int.MAX_VALUE) :
            this(Array(numVertices) { IntArray(it) { initialDistance } })

    constructor(numVertices: Int, init: (Int, Int) -> Int) :
            this(Array(numVertices) { vertex1 -> IntArray(vertex1) { vertex2 -> init(vertex1, vertex2) } })

    operator fun set(vertex1: Int, vertex2: Int, distance: Int) {
        if (vertex1 == vertex2) {
            if (distance != 0)
                throw IllegalArgumentException("distances between 2 same vertices can only be 0")
        } else {
            val vertices = intArrayOf(vertex1, vertex2).apply { sort() }
            val smaller = vertices[0]
            val larger = vertices[1]
            distances[larger][smaller] = distance
        }
    }

    operator fun get(vertex1: Int, vertex2: Int): Int {
        if (vertex1 == vertex2)
            return 0
        else {
            val vertices = intArrayOf(vertex1, vertex2).apply { sort() }
            val smaller = vertices[0]
            val larger = vertices[1]
            return distances[larger][smaller]
        }
    }

    fun asDistanceSequence() =
        distances.asSequence().flatMap { it.asSequence() }

    fun asEdgeSequence() =
        distances.asSequence().withIndex().flatMap { (vertex1, vertex1Distances) ->
            vertex1Distances.asSequence().withIndex().map { (vertex2, distance) ->
                WeightedEdge(vertex1, vertex2, distance)
            }
        }
}