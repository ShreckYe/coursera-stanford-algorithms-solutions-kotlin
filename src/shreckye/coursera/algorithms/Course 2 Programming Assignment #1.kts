package shreckye.coursera.algorithms

import java.io.File

// Please add -Xss8m to VM options

val filename = args[0]
val NUM_VERTICES = 875714
val INDEX_LABEL_OFFSET = 1

val graph = IndexGraph(NUM_VERTICES)
val inverseGraph = IndexGraph(NUM_VERTICES)
File(filename).forEachLine {
    val (tailLabel, headLabel) = it.splitToEdgeData()
    graph.addEdgeByLabel(tailLabel, headLabel, INDEX_LABEL_OFFSET)
    inverseGraph.addEdgeByLabel(headLabel, tailLabel, INDEX_LABEL_OFFSET)
}
graph.trimEdges()
inverseGraph.trimEdges()

println(sccLeaderAndSizesSortedBySize(NUM_VERTICES, graph, inverseGraph).take(5).map(IntPair::second).joinToString(","))

