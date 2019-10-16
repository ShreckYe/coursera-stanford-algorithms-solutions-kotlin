package shreckye.coursera.algorithms

import java.io.File
import java.lang.Math.sqrt

val filename = args[0]
val (n, points) = File(filename).bufferedReader().use {
    val numCities = it.readLine().toInt()
    val cityPoints = it.lineSequence()
        .map { it.splitWithWhiteSpaceAndFilterEmpty().run { Point(get(1).toDouble(), get(2).toDouble()) } }.toList()
    numCities to cityPoints
}

var tspDistance = 0.0
val firstPoint = points.first()
var endPoint = firstPoint
val remainingPoints = points.subList(1, points.size).toMutableList()
while (remainingPoints.isNotEmpty()) {
    val (index, minDistanceSquared) = remainingPoints.asSequence()
        .map { euclideanDistanceSquared(endPoint, it) }
        .withIndex()
        .minWith(compareBy(IndexedValue<Double>::value).thenBy(IndexedValue<Double>::index))!!

    endPoint = remainingPoints.removeAt(index)
    tspDistance += sqrt(minDistanceSquared)
}
tspDistance += euclideanDistance(endPoint, firstPoint)

println("%f".format(tspDistance))