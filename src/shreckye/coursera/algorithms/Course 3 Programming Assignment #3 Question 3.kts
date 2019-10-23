package shreckye.coursera.algorithms

import java.io.File
import java.util.*
import kotlin.test.assertEquals

val filename = args[0]
val INDEX_LABEL_OFFSET = 1
val questionVertexLabels = intArrayOf(1, 2, 3, 4, 17, 117, 517, 997)
val (numVertices, vertexWeights) = File(filename).bufferedReader().use {
    val numVertices = it.readLine().toInt()
    val vertexWeights = it.lineSequence().map(String::toInt).toIntArray(numVertices)
    numVertices to vertexWeights
}
fun mwis(numVertices: Int, vertexWeights: IntArray): Pair<Long, BitSet> {
    //val maxWeights = IntArray(numVertices + 1)
    val choiceSet = BitSet(numVertices + 1)

    // Int causes overflow
    var maxWeightNMinus2 = 0L
    var maxWeightNMinus1 = vertexWeights[0].toLong()
    choiceSet[1] = false

    for (n in 2..numVertices) {
        val maxWeight2 = maxWeightNMinus2 + vertexWeights[n - 1]
        val choice = maxWeightNMinus1 >= maxWeight2
        choiceSet[n] = choice
        val maxWeightI = if (choice) maxWeightNMinus1 else maxWeight2

        maxWeightNMinus2 = maxWeightNMinus1
        maxWeightNMinus1 = maxWeightI
    }

    // Reconstruction
    val takenSet = BitSet(numVertices)
    var n = numVertices
    while (n > 0)
        if (choiceSet[n])
            n--
        else {
            takenSet[n - 1] = true
            n -= 2
        }

    return maxWeightNMinus1 to takenSet
}

val mwisSet = mwis(numVertices, vertexWeights).second
println(questionVertexLabels.map { if (mwisSet[it - INDEX_LABEL_OFFSET]) '1' else '0' }.joinToString(""))


// Test cases
// n = 0 case: not supported, n = 1 case: special case that takes 1 element
for (n in 2 until 1024) {
    val (mwisWeight, mwisSet) = mwis(n, IntArray(n) { it % 2 })
    assertEquals(n / 2, mwisWeight.toInt(), "n = $n")
    assertEquals(
        BitSet(n).also { set -> (0 until n).filter { it % 2 != 0 }.forEach { set[it] = true } },
        mwisSet,
        "n = $n"
    )
}
