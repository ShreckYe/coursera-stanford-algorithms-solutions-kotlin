package shreckye.coursera.algorithms

import java.io.File
import kotlin.math.max
import kotlin.random.Random
import kotlin.system.measureNanoTime
import kotlin.test.assertEquals

data class Item(val value: Int, val weight: Int)

fun File.readKnapsackData() =
    bufferedReader().use {
        val (knapsackSize, numItems) = it.readLine().splitToInts()

        val items = it.lineSequence().map {
            val lineInts = it.splitToInts()
            Item(lineInts[0], lineInts[1])
        }.toList()

        Triple(knapsackSize, numItems, items)
    }


fun knapsackIterativeWith2DimArray(items: List<Item>, n: Int, w: Int): Int {
    val mvs = Array(n + 1) { IntArray(w + 1) }
    for (i in 0..n)
        for (x in 0..w)
            mvs[i][x] = if (i == 0)
                0
            else {
                val iMinus1 = i - 1
                val item = items[iMinus1]
                val xLeft = x - item.weight

                if (xLeft < 0) mvs[iMinus1][x]
                else max(mvs[iMinus1][x], mvs[iMinus1][xLeft] + item.value)
            }

    return mvs[n][w]
}

fun knapsackIterativeWithSingleArray(items: List<Item>, n: Int, w: Int): Int {
    val mvs = IntArray(w + 1)
    for (i in 0..n)
        for (x in w downTo 0)
            mvs[x] = if (i == 0)
                0
            else {
                val item = items[i - 1]
                val xLeft = x - item.weight

                if (xLeft < 0) mvs[x]
                else max(mvs[x], mvs[xLeft] + item.value)
            }

    return mvs[w]
}

val UNKNWON = Int.MIN_VALUE
fun knapsackRecursive(
    items: List<Item>, n: Int, w: Int,
    mvs: SimpleMutableMap<IntPair, Int>
): Int {
    val nw = n to w
    var mvwn = mvs[nw]
    if (mvwn != UNKNWON)
        return mvwn

    mvwn = if (n == 0)
        0
    else {
        val nMinus1 = n - 1
        val item = items[nMinus1]
        val wLeft = w - item.weight

        if (wLeft < 0) knapsackRecursive(items, nMinus1, w, mvs)
        else max(knapsackRecursive(items, nMinus1, w, mvs), knapsackRecursive(items, nMinus1, wLeft, mvs) + item.value)
    }

    mvs[nw] = mvwn

    return mvwn
}

fun knapsackRecursiveWithHashMapOfArrays(items: List<Item>, n: Int, w: Int): Int {
    val nSize = n + 1
    return knapsackRecursive(items, n, w, object : SimpleMutableMap<IntPair, Int> {
        val mvs = HashMap<Int, IntArray>()
        @Suppress("NAME_SHADOWING")
        override fun get(key: IntPair): Int {
            val (n, w) = key
            val mvsw = mvs[w]
            return if (mvsw != null)
                mvsw[n]
            else
                UNKNWON
        }

        @Suppress("NAME_SHADOWING")
        override fun set(key: IntPair, value: Int) {
            val (n, w) = key
            var mvsw = mvs[w]
            if (mvsw == null) {
                mvsw = IntArray(nSize) { UNKNWON }
                mvs[w] = mvsw
            }

            mvsw[n] = value
        }
    })
}

fun knapsackRecursiveWithHashMap(items: List<Item>, n: Int, w: Int): Int =
    knapsackRecursive(items, n, w, HashMap<IntPair, Int>().asSimpleMutableMap({ (ni, wi) ->
        ni in 0..n && wi in 0..w
    }, UNKNWON))

fun knapsackRecursiveWith2DimArray(items: List<Item>, n: Int, w: Int): Int =
    knapsackRecursive(items, n, w, Array(n + 1) { IntArray(w + 1) { UNKNWON } }.asSimpleMutableMap())

fun knapsackRecursiveOnNWithSingleArray(items: List<Item>, n: Int, w: Int): Int {
    val mvs = IntArray(w + 1)
    knapsackRecursiveOnNWithSingleArrayRecursiveProcess(items, n, intArrayOf(w), mvs)
    return mvs[w]
}

fun knapsackRecursiveOnNWithSingleArrayRecursiveProcess(
    items: List<Item>, n: Int, ws: IntArray,
    mvs: IntArray
) {
    if (n == 0) {
        for (w in ws)
            mvs[w] = 0
        return
    }

    val (value, weight) = items[n - 1]
    //ws.assertSortedIncreasingWithNoDuplicates { "n = $n, ws = ${ws.contentToString()}" }
    knapsackRecursiveOnNWithSingleArrayRecursiveProcess(
        items, n - 1,
        mergeSortedDistinct(
            ws, ws.asSequence().map { it - weight }.filter { it >= 0 }.toList().toIntArray()
        ),
        mvs
    )

    for (w in ws.reversed()) {
        val wLeft = w - weight

        mvs[w] = if (wLeft < 0) mvs[w]
        else max(mvs[w], mvs[wLeft] + value)
    }
}

data class Evaluation(val algorithmName: String, val result: Int, val nanoTime: Long)

fun List<Evaluation>.getSingleResult() =
    map(Evaluation::result).assertAllEqualsAndGet()

val KNAPSACK_ALGORITHMS = listOf(
    ::knapsackIterativeWith2DimArray,
    ::knapsackIterativeWithSingleArray,
    ::knapsackRecursiveWithHashMapOfArrays,
    ::knapsackRecursiveWithHashMap,
    ::knapsackRecursiveWith2DimArray,
    ::knapsackRecursiveOnNWithSingleArray
)

fun evaluateKnapsackAlgorithms(
    items: List<Item>, n: Int, w: Int,
    knapsackAlgorithms: List<Pair<String, (List<Item>, Int, Int) -> Int>> = KNAPSACK_ALGORITHMS.map { it.name to it }
): List<Evaluation> =
    knapsackAlgorithms.map {
        var knapsack: Int = UNKNWON
        val knapsackTime = measureNanoTime { knapsack = it.second(items, n, w) }
        Evaluation(it.first, knapsack, knapsackTime)
    }

// Question 1
val question1Filename = args[0]
val (question1KnapsackSize, question1NumItems, question1Items) = File(question1Filename).readKnapsackData()
val question1Evaluations = evaluateKnapsackAlgorithms(question1Items, question1NumItems, question1KnapsackSize)
println(question1Evaluations)
println("1. ${question1Evaluations.getSingleResult()}")
println()


// Question 2
val question2Filename = args[1]
val (question2KnapsackSize, question2NumItems, question2Items) = File(question2Filename).readKnapsackData()
val question2Evaluations = evaluateKnapsackAlgorithms(
    question2Items, question2NumItems, question2KnapsackSize,
    listOf(
        ::knapsackIterativeWithSingleArray,
        ::knapsackRecursiveWithHashMapOfArrays,
        ::knapsackRecursiveOnNWithSingleArray
    ).map { it.name to it }
)
println(question2Evaluations)
println("2. ${question2Evaluations.getSingleResult()}")
println()


// Test cases
for (n in 0..16) {
    val items = List(n) { Item(Random.nextInt(0, 1024), Random.nextInt(0, 1024)) }
    val w = items.sumBy(Item::weight) / 2
    val expected =
        items.subsets().filter { it.sumBy(Item::weight) <= w }.map { it.sumBy(Item::value) }.max()

    val evaluations = evaluateKnapsackAlgorithms(items, n, w)
    println("n = $n, evaluations = $evaluations")
    for (evaluation in evaluations)
        assertEquals(
            expected, evaluation.result,
            "n = $n, algorithmName = ${evaluation.algorithmName}, knapsackSize = $w, items = $items"
        )
}