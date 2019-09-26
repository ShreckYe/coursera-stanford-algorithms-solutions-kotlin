package shreckye.coursera.algorithms

import java.io.File
import java.util.*
import kotlin.random.Random
import kotlin.test.assertEquals

val NUM_INTEGERS = 10000
val filename = args[0]
val integers = File(filename).useLines { it.map(String::toInt).toIntArray(NUM_INTEGERS) }

fun medianSum(integers: IntArray): Int {
    val less = PriorityQueue<Int>(integers.size, compareByDescending { it })
    val greater = PriorityQueue<Int>(integers.size)

    var medianSum = 0
    for (i in 0 until integers.size) {
        val integer = integers[i]

        val median: Int
        if (less.isNotEmpty() && integer < less.peek()) {
            median = less.poll()
            less.offer(integer)
        } else if (greater.isNotEmpty() && integer > greater.peek()) {
            median = greater.poll()
            greater.offer(integer)
        } else
            median = integer

        //println("i = $i, median = $median")
        medianSum += median

        if (i % 2 == 0)
            greater.offer(median)
        else
            less.offer(median)
    }

    return medianSum
}

println(medianSum(integers))


// Test cases
for (n in 1 until 1000) {
    val actual = medianSum(IntArray(n) { it })
    val n2 = n / 2
    var expected = n2 * (n2 - 1)
    if (n % 2 == 1) expected += n2
    assertEquals(expected, actual, "n = $n")
}

repeat(10) {
    val integers = IntArray(1000) { Random.nextInt(0, 1000) }
    val actual = medianSum(integers)
    val expected = (1..integers.size).sumBy { integers.take(it).sorted()[(it - 1) / 2] }
    assert(actual == expected) { "integers = ${integers.contentToString()}, actual = $actual, expected = $expected" }
}
