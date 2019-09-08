import java.io.File

val filename = args[0]

val integers = IntArray(10_000)
var i = 0
File(filename).forEachLine { integers[i++] = it.toInt() }

fun quickSortAndCountNumComparisons(
    integers: IntArray,
    left: Int = 0, right: Int = integers.size,
    swapPivot: IntArray.(Int, Int) -> Unit
): Int {
    val size = right - left
    //println("$left $right $size")
    if (size == 0 || size == 1)
        return 0

    integers.swapPivot(left, right)
    val pivot = integers[left]
    var i = 1
    for (j in 1 until size)
        if (integers[left + j] < pivot)
            integers.swap(left + i++, left + j)

    integers.swap(left, left + i - 1)

    return size - 1 +
            quickSortAndCountNumComparisons(integers, left, left + i - 1, swapPivot) +
            quickSortAndCountNumComparisons(integers, left + i, right, swapPivot)
}

@Suppress("NOTHING_TO_INLINE")
inline fun IntArray.swap(i: Int, j: Int) {
    this[i] = this[j].also { this[j] = this[i] }
}

inline fun quickSortAndCountNumComparisonsWithChoosePivot(
    integers: IntArray,
    crossinline choosePivot: IntArray.(Int, Int) -> Int
) =
    quickSortAndCountNumComparisons(integers) { left, right -> swap(left, choosePivot(left, right)) }

fun quickSortAndCountNumComparisons(integers: IntArray) =
    quickSortAndCountNumComparisons(integers) { _, _ -> }

fun quickSortAndCountNumComparisonsUsingFinalElementAsPivot(integers: IntArray) =
    quickSortAndCountNumComparisonsWithChoosePivot(integers) { _, right -> right - 1 }

fun quickSortAndCountNumComparisonsUsingMedianOfThreeAsPivot(integers: IntArray) =
    quickSortAndCountNumComparisonsWithChoosePivot(integers) { left, right ->
        val first = left.let { it to this[it] }
        val middle = ((left + right - 1) / 2).let { it to this[it] }
        val final = (right - 1).let { it to this[it] }
        val three = arrayOf(first, middle, final)
        three.sortBy(Pair<Int, Int>::second)
        three[1].first
    }

var integersToSort = integers.copyOf()
println("1. ${quickSortAndCountNumComparisons(integersToSort)}")
integersToSort = integers.copyOf()
println("2. ${quickSortAndCountNumComparisonsUsingFinalElementAsPivot(integersToSort)}")
integersToSort = integers.copyOf()
println("3. ${quickSortAndCountNumComparisonsUsingMedianOfThreeAsPivot(integersToSort)}")


for (n in 0..1000) {
    val maxNumComparisons = n * (n - 1) / 2
    quickSortAndCountNumComparisons(IntArray(n) { it }).let {
        assert(it == maxNumComparisons) { "n = $n and numComparisons = $it" }
    }
    quickSortAndCountNumComparisons(IntArray(n) { n - it }).let {
        assert(it == maxNumComparisons) { "n = $n and numComparisons = $it" }
    }

    quickSortAndCountNumComparisonsUsingFinalElementAsPivot(IntArray(n) { it }).let {
        assert(it == maxNumComparisons) { "n = $n and numComparisons = $it" }
    }
    quickSortAndCountNumComparisonsUsingFinalElementAsPivot(IntArray(n) { n - it }).let {
        assert(it == maxNumComparisons) { "n = $n and numComparisons = $it" }
    }
}

for (n in 0..10) {
    val size = (2 pow n + 1) - 1
    val expectedNumOfComparisons = (n - 1) * (2 pow n + 1) + 2
    quickSortAndCountNumComparisonsUsingMedianOfThreeAsPivot(IntArray(size) { it }).let {
        assert(it == expectedNumOfComparisons) { "size = $size and numComparisons = $it" }
    }
}

infix fun Int.pow(y: Int): Int =
    when (y) {
        0 -> 1
        1 -> this
        else -> {
            val rPow = pow(y / 2)
            var r = rPow * rPow
            if (y % 2 == 1)
                r *= this
            r
        }
    }