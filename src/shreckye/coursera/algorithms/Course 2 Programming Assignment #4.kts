package shreckye.coursera.algorithms

import java.io.File
import kotlin.system.measureTimeMillis
import kotlin.test.assertEquals

val filename = args[0]
val TARGET_RANGE = -10000..10000
val integers = File(filename).useLines { it.map(String::toLong).toList().toLongArray() }

fun countTargets(targetRange: IntRange, integers: LongArray): Int {
    val integerHashMap = integers.asIterable().groupingBy { it }.eachCountTo(
        HashMap(integers.size, 0.25f)
    )
    return targetRange.count { target ->
        println("Counting target = $target")
        integers.any { integer ->
            val other = target - integer
            val otherCount = integerHashMap.getOrDefault(other, 0)
            if (integer == other) otherCount >= 2 else otherCount > 0
        }
    }
}

var numTargets = 0
val timeMillis = measureTimeMillis {
    numTargets = countTargets(TARGET_RANGE, integers)
}
println("$numTargets ($timeMillis ms)")


// Test cases
for (n in 2..513)
    assertEquals(n * 2 - 3, countTargets(0..1024, LongArray(n) { it.toLong() }))
for (n in 2..513)
    assertEquals(n * 2 - 2, countTargets(0..1024, LongArray(n) { it.toLong() } + 0L))