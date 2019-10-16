package shreckye.coursera.algorithms

import java.io.File
import kotlin.streams.asStream
import kotlin.system.measureTimeMillis

val filename = args[0]
val (n, points) = File(filename).bufferedReader().use {
    val numCities = it.readLine().toInt()
    val cityPoints = it.lineSequence().map { it.splitToDoubles().run { Point(get(0), get(1)) } }.toList()
    numCities to cityPoints
}

val distanceMatrix = euclideanPlainDistanceMatrix(points)

var tspDistance: Double? = null
val timeMillis = measureTimeMillis {
    val setBound = 1 shl n
    val shortestDistances = Array(setBound) { DoubleArray(n) { Double.POSITIVE_INFINITY } }
    shortestDistances[1][0] = 0.0

    for (m in 2..n) {
        println("Set size: m = $m")
        1.supersetSequence(n, m - 1, 1).asStream().parallel().forEach { set ->
            val setShortestDistances = shortestDistances[set]
            set.subsetSequenceWithSingleDifference(n).forEach { (j, subset) ->
                val subsetShortestDistances = shortestDistances[subset]
                setShortestDistances[j] = subset.elementBitIndices(n)
                    .map { k -> subsetShortestDistances[k] + distanceMatrix[k][j] }
                    .min()!!
            }
        }
    }

    tspDistance =
        shortestDistances[setBound - 1].mapIndexed { j, distance -> distance + distanceMatrix[j][0] }.min()
}
println("$tspDistance (took $timeMillis ms)")