import java.io.File

val filename = args[0]

val integers = IntArray(100_000)
var i = 0
File(filename).forEachLine { integers[i++] = it.toInt() }

fun mergeSortAndCountNumInversions(
    integers: IntArray,
    left: Int = 0, right: Int = integers.size
): Pair<IntArray, Long> {
    val size = right - left
    if (size == 0)
        return Pair(IntArray(0), 0)
    if (size == 1)
        return Pair(IntArray(1) { integers[left] }, 0)

    val half = size / 2
    val rightHalf = size - half
    val mid = left + half
    val (leftSortedIntegers, numLeftInversions) = mergeSortAndCountNumInversions(integers, left, mid)
    val (rightSortedIntegers, numRightInversions) = mergeSortAndCountNumInversions(integers, mid, right)

    val sortedIntegers = IntArray(size)
    var i = 0
    var j = 0
    var numSplitInversions = 0L
    while (i < half && j < rightHalf) {
        // Equal is not needed as there no repeated values
        if (leftSortedIntegers[i] <= rightSortedIntegers[j]) {
            sortedIntegers[i + j] = leftSortedIntegers[i++]
            numSplitInversions += j
        } else {
            sortedIntegers[i + j] = rightSortedIntegers[j++]
        }
    }
    numSplitInversions += (half - i) * j
    while (i < half) sortedIntegers[i + j] = leftSortedIntegers[i++]
    while (j < rightHalf) sortedIntegers[i + j] = rightSortedIntegers[j++]

    //println(leftSortedIntegers.contentToString() + rightSortedIntegers.contentToString() + sortedIntegers.contentToString())
    return Pair(sortedIntegers, numLeftInversions + numRightInversions + numSplitInversions)
}

println(mergeSortAndCountNumInversions(integers).second)

for (n in 1..1000) {
    mergeSortAndCountNumInversions(IntArray(n) { it }).second.let {
        assert(it == 0L) { "Assertion failed for n = $n incremental case with $it inversions" }
    }
    mergeSortAndCountNumInversions(IntArray(n) { n - it }).second.let {
        val nL = n.toLong()
        assert(it == nL * (nL - 1) / 2) { "Assertion failed for n = $n decremental case with $it inversions" }
    }
}