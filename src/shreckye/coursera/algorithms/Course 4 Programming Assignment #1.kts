package shreckye.coursera.algorithms

val filenames = args
val INDEX_LABEL_OFFSET = 1
val graphData =
    filenames.map { readDirectedGraphVerticesArrayListSimplifiedEdgesAndInverseFromEdges(it, INDEX_LABEL_OFFSET) }


val smallestShortestShortestPathDistance = graphData.map { (numVertices, _, edgeVerticesAndInverse) ->
    val (verticesEdges, inverseGraphVerticesEdges) = edgeVerticesAndInverse
    val shortestDistances1 = floydWarshallAllPairsShortestPathDistances(
        inverseGraphVerticesEdges.asVerticesSimplifiedEdges(),
        numVertices
    )
    val shortestDistances2 = johnsonAllPairsShortestPathDistances(
        verticesEdges.asVerticesSimplifiedEdges(),
        inverseGraphVerticesEdges.asVerticesSimplifiedEdges(),
        numVertices
    )

    if (shortestDistances1 === null)
        if (shortestDistances2 === null)
            null
        else
            throw AssertionError()
    else
        if (shortestDistances2 === null)
            throw AssertionError()
        else {
            assert(shortestDistances1.contentDeepEquals(shortestDistances2))
            shortestDistances1.map { it.min()!! }.min()
        }
}.filterNotNull().min()

println(smallestShortestShortestPathDistance)