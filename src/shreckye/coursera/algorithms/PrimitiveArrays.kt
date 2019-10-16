package shreckye.coursera.algorithms

//inline fun IntArray.mapToIntArray(transform: (Int) -> Int) {}

class Efficient2dDoubleArray(val n1: Int, val n2: Int) {
    val backingArray = DoubleArray(n1 * n2)

    operator fun set(index1: Int, index2: Int, value: Double) {
        backingArray[n2 * index1 + index2] = value
    }

    operator fun get(index1: Int, index2: Int): Double =
        backingArray[n2 * index1 + index2]

    operator fun get(index1: Int): DoubleArray {
        val start = n2 * index1
        return backingArray.copyOfRange(start, start + n2 - 1)
    }
}