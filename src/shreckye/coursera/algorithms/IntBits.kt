package shreckye.coursera.algorithms

fun Int.flipSequence(length: Int, hammingDistance: Int, startIndex: Int = 0): Sequence<Int> =
    if (hammingDistance == 0)
        sequenceOf(this)
    else
        (startIndex..length - hammingDistance).asSequence().flatMap {
            (this xor (1 shl it)).flipSequence(length, hammingDistance - 1, it + 1)
        }

fun setSequence(length: Int, setSize: Int) =
    0.flipSequence(length, setSize)

/** [this] must be smaller than 1 [shl] [startIndex] */
fun Int.supersetSequence(length: Int, extraSetSize: Int, startIndex: Int) =
    this.flipSequence(length, extraSetSize, startIndex)

fun Int.subsetSequenceWithSingleDifference(length: Int): Sequence<IntPair> =
    (0 until length).asSequence()
        .map { it to (1 shl it) }
        .filter { (_, mask) -> this and mask != 0 }
        .map { (index, mask) -> index to (this xor mask) }

fun Int.subsetsWithSingleDifference(length: Int): List<IntPair> =
    subsetSequenceWithSingleDifference(length).toList()

fun Int.elementBitIndices(length: Int): Sequence<Int> =
    (0 until length).asSequence()
        .filter { this and (1 shl it) != 0 }