package shreckye.coursera.algorithms

val n = args.size
val weights = args.map(String::toFloat)

val minSearchTimes = Array(n) { FloatArray(n + 1) }
for (i in 0 until n)
    minSearchTimes[i][i] = 0f
for (s in 1..n)
    for (i in 0..n - s) {
        val j = i + s
        minSearchTimes[i][j] = weights.subList(i, j).sum() +
                (i until j)
                    .map { minSearchTimes[i][it] + (minSearchTimes.getOrNull(it + 1)?.get(j) ?: 0f) }.min()!!
    }

println(minSearchTimes[0][n])