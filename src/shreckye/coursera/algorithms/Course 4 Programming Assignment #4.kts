package shreckye.coursera.algorithms

import java.io.File

val filenames = args
val INDEX_LABEL_OFFSET = 1
val clauses = filenames.map {
    File(it).bufferedReader().use {
        it.readLine().toInt() to it.lineSequence().map { it.splitToInts() }.toList()
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun Int.literalToVertexIndex() =
    if (this >= 0) (this - INDEX_LABEL_OFFSET) * 2 else (-this - INDEX_LABEL_OFFSET) * 2 + 1

@Suppress("NOTHING_TO_INLINE")
inline fun Int.vertexIndexToVariableIndex() =
    this / 2

val results = clauses.map { (numVariables, clauses) ->
    val numVertices = numVariables * 2
    val graph = IndexGraph(numVertices)
    val inverseGraph = IndexGraph(numVertices)
    for ((literal1, literal2) in clauses) {
        val vertex1 = literal1.literalToVertexIndex()
        val vertex1Neg = (-literal1).literalToVertexIndex()
        val vertex2 = literal2.literalToVertexIndex()
        val vertex2Neg = (-literal2).literalToVertexIndex()
        graph.addEdge(vertex1Neg, vertex2)
        inverseGraph.addEdge(vertex2, vertex1Neg)
        graph.addEdge(vertex2Neg, vertex1)
        inverseGraph.addEdge(vertex1, vertex2Neg)
    }
    graph.trimEdges()
    inverseGraph.trimEdges()

    val sccs = sccsSortedBySize(numVertices, graph, inverseGraph)
    !sccs.any {
        it.sccVertices.groupingBy { it.vertexIndexToVariableIndex() }
            .eachCount().any { (_, variableCount) -> variableCount == 2 }
    }
}

println(results.map { if (it) 1 else 0 }.joinToString(","))