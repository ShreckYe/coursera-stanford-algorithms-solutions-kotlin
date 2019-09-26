package shreckye.coursera.algorithms

// Representing the adjacency list of a index-based graph that's immutable after constructed

data class Edge(val vertex1: Int, val vertex2: Int)
data class WeightedEdge(val vertex1: Int, val vertex2: Int, val weight: Int)
data class WeightedSimplifiedEdge(val otherVertex: Int, val weight: Int)
typealias VerticesSimplifiedEdges = Array<List<WeightedSimplifiedEdge>>
typealias VerticesArrayListSimplifiedEdges = Array<ArrayList<WeightedSimplifiedEdge>>

class SymmetricDistanceMatrix(numVertices: Int, initialDistance: Int = Int.MAX_VALUE) {
    val distances = Array(numVertices) { IntArray(it) { initialDistance } }
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