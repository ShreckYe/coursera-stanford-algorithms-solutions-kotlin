package shreckye.coursera.algorithms

import kotlin.test.assertEquals

fun <E> Iterable<E>.assertAllEqualsAndGet() =
    reduce { e1, e2 ->
        assertEquals(e1, e2)
        e1
    }

//fun assertDistinct(numbers: Iterable<Int>) {}

fun IntArray.assertSortedIncreasingWithNoDuplicates(lazyMessage: () -> Any) {
    reduce { e1, e2 ->
        assert(e1 < e2, lazyMessage)
        e2
    }
}