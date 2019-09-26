package shreckye.coursera.algorithms

import java.io.File
import java.util.*
import kotlin.collections.ArrayList

// Please add -Xss8m to VM options

val filename = args[0]
val NUM_VERTICES = 875714
val INDEX_LABEL_OFFSET = 1

class IndexGraph(val numVertices: Int, val indexLabelOffset: Int) {
    val verticesEdges = Array(numVertices) { SimpleIntArrayList() }

    /*fun setEdges(tail: Int, edges: SimpleIntArrayList) {
        verticesEdges[tail] = edges
    }

    fun setEdgesByLabel(tailLabel: Int, edges: SimpleIntArrayList) =
        setEdges(tailLabel - indexLabelOffset, edges)*/

    fun addEdge(tail: Int, head: Int) =
        verticesEdges[tail].add(head)

    fun addEdgeByLabel(tailLabel: Int, headLabel: Int) =
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

val graph = IndexGraph(NUM_VERTICES, INDEX_LABEL_OFFSET)
val inverseGraph = IndexGraph(NUM_VERTICES, INDEX_LABEL_OFFSET)
File(filename).forEachLine {
    val (tailLabel, headLabel) = it.splitToEdgeData()
    graph.addEdgeByLabel(tailLabel, headLabel)
    inverseGraph.addEdgeByLabel(headLabel, tailLabel)
}
graph.trimEdges()
inverseGraph.trimEdges()

// First DFS
val orderedVertices = Stack<Int>()
inverseGraph.dfsAll { orderedVertices.push(it) }

// Second DFS
val sccLeaderAndSizes = ArrayList<Pair<Int, Int>>()
val searchedVertices = BitSet(NUM_VERTICES)
while (orderedVertices.isNotEmpty()) {
    val leader = orderedVertices.pop()

    if (!searchedVertices[leader]) {
        var size = 0
        graph.dfs(leader, searchedVertices) { size++ }
        sccLeaderAndSizes.add(leader to size)
    }
}

sccLeaderAndSizes.sortByDescending(Pair<Int, Int>::second)

println(sccLeaderAndSizes.take(5).map(Pair<Int, Int>::second).joinToString(","))

