package shreckye.coursera.algorithms

/** Represents a pair of primitive ints that is more efficient than [Pair] */
data class IntPair(val first: Int, val second: Int) {
    override fun toString(): String = "($first, $second)"
}

@Suppress("NOTHING_TO_INLINE")
inline infix fun Int.to(that: Int): IntPair = IntPair(this, that)