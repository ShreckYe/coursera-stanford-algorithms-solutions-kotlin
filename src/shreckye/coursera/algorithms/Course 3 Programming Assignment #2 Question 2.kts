package shreckye.coursera.algorithms

// number of bits shouldn't exceed 32
val filename = args[0]
val (numNodes, numBits, nodes) = java.io.File(filename).bufferedReader().use {
    val (numNodes, numBits) = it.readLine().splitToInts()

    val nodes = it.lineSequence().map {
        it.splitWithWhiteSpaceAndFilterEmpty().joinToString("")
            .toInt(2)
    }.toIntArray(numNodes)

    Triple(numNodes, numBits, nodes)
}


fun largestKWithSpacingAtLeast(numNodes: Int, numBits: Int, nodes: IntArray, spacingAtLeast: Int): Int {
    val indexClusterUnionFind = UnionFind(numNodes)
    val indicesAtNodes = Array<SimpleIntArrayList?>(1 shl numBits) { null }
    for ((index, node) in nodes.withIndex()) {
        val indicesAtNode = indicesAtNodes[node]
        if (indicesAtNode === null)
            indicesAtNodes[node] = simpleIntArrayListOf(index)
        else
            indicesAtNode.add(index)
    }

    val nodeIndexPairsAtDistances = (0 until spacingAtLeast).map { distance ->
        nodes.withIndex().asSequence().flatMap { (index1, node1) ->
            node1.flipPossibilitySequence(numBits, distance)
                .mapNotNull { indicesAtNodes[it] }
                .flatMap { it.asSequence() }
                .filter { index2 -> index1 < index2 }
                .map { index2 -> Edge(index1, index2) }
        }
    }

    return numNodes - nodeIndexPairsAtDistances.sumBy {
        it.count {
            indexClusterUnionFind.findAndUnionIfNecessary(it.vertex1, it.vertex2)
        }
    }
}

fun Int.flipPossibilitySequence(numBits: Int, hammingDistance: Int, bitsFlipped: Int = 0): Sequence<Int> =
    if (hammingDistance == 0)
        sequenceOf(this)
    else
        (0 until numBits).asSequence()
            .map { 1 shl it }
            .filter { bitsFlipped and it == 0 }
            .flatMap {
                (this xor it).flipPossibilitySequence(numBits, hammingDistance - 1, bitsFlipped or it)
            }

println(largestKWithSpacingAtLeast(numNodes, numBits, nodes, 3))