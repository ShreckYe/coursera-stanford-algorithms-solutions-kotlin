package shreckye.coursera.algorithms

fun Sequence<Int>.toIntArray(size: Int): IntArray {
    val iterator = iterator()
    return IntArray(size) { iterator.next() }
}

fun <T> List<T>.subsets(): Sequence<Sequence<T>> =
    if (size == 0)
        sequenceOf(emptySequence())
    else {
        val first = first()
        val tailSubsets = subList(1, size).subsets()
        tailSubsets + tailSubsets.map { sequenceOf(first) + it }
    }