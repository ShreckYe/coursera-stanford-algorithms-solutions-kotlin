package shreckye.coursera.algorithms

import java.util.*
import kotlin.collections.ArrayList

data class Scc(val sccLeader: Int, val sccVertices: SimpleIntArrayList)

private inline fun searchSccs(
    numVertices: Int, graph: IndexGraph, inverseGraph: IndexGraph,
    beforeSearchingScc: (Int) -> Unit, crossinline searchingScc: (Int, Int) -> Unit, afterSearchingScc: (Int) -> Unit
) {
    // First DFS
    val orderedVertices = Stack<Int>()
    inverseGraph.dfsAll { orderedVertices.push(it) }

    // Second DFS
    val searchedVertices = BitSet(numVertices)
    while (orderedVertices.isNotEmpty()) {
        val leader = orderedVertices.pop()

        if (!searchedVertices[leader]) {
            beforeSearchingScc(leader)
            graph.dfs(leader, searchedVertices) { searchingScc(leader, it) }
            afterSearchingScc(leader)
        }
    }
}

fun sccLeaderAndSizesSortedBySize(numVertices: Int, graph: IndexGraph, inverseGraph: IndexGraph): List<IntPair> {
    val sccs = ArrayList<IntPair>()

    var size = Int.MIN_VALUE
    searchSccs(numVertices, graph, inverseGraph,
        { size = 0 },
        { _, _ -> size++ },
        { sccs.add(it to size) })

    sccs.sortByDescending(IntPair::second)
    return sccs
}

fun sccsSortedBySize(numVertices: Int, graph: IndexGraph, inverseGraph: IndexGraph): List<Scc> {
    val sccs = ArrayList<Scc>()

    var sccVertices: SimpleIntArrayList? = null
    searchSccs(numVertices, graph, inverseGraph,
        { sccVertices = SimpleIntArrayList() },
        { leader, vertex -> sccVertices!!.add(vertex) },
        { sccs.add(Scc(it, sccVertices!!)) })

    sccs.sortByDescending { it.sccVertices.size }
    return sccs
}