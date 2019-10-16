package shreckye.coursera.algorithms

import kotlin.math.sqrt

data class Point(val x: Double, val y: Double)

fun euclideanDistanceSquared(point1: Point, point2: Point) =
    (point1.x - point2.x).squared() + (point1.y - point2.y).squared()

fun euclideanDistance(point1: Point, point2: Point) =
    sqrt(euclideanDistanceSquared(point1, point2))

fun euclideanPlainDistanceMatrix(points: List<Point>): Array<DoubleArray> =
    points.map { point1 -> points.map { point2 -> euclideanDistance(point1, point2) }.toDoubleArray() }.toTypedArray()
