package shreckye.coursera.algorithms

import java.io.File
import java.util.*
import kotlin.math.max
import kotlin.math.min

val filename = args[0]
val (numSymbols, symbolWeights) = File(filename).bufferedReader().use {
    val numSymbols = it.readLine().toInt()
    val symbolWeights = it.useLines { it.map(String::toInt).toIntArray(numSymbols) }
    numSymbols to symbolWeights
}

interface HuffmanNode : Comparable<HuffmanNode> {
    val weight: Int
    override fun compareTo(other: HuffmanNode): Int =
        weight - other.weight
}

data class HuffmanLeafNode(override val weight: Int) : HuffmanNode
data class HuffmanInternalNode(
    override val weight: Int,
    val leftChild: HuffmanNode, val rightChild: HuffmanNode
) : HuffmanNode

val minNodeHeap = PriorityQueue<HuffmanNode>(symbolWeights.map(::HuffmanLeafNode))
while (minNodeHeap.size > 1) {
    val minNode1 = minNodeHeap.poll()
    val minNode2 = minNodeHeap.poll()
    minNodeHeap.offer(HuffmanInternalNode(minNode1.weight + minNode2.weight, minNode1, minNode2))
}

fun HuffmanNode.maxAndMinLength(): Pair<Int, Int> =
    when (this) {
        is HuffmanLeafNode ->
            1 to 1
        is HuffmanInternalNode -> {
            val (leftMaxLength, leftMinLength) = leftChild.maxAndMinLength()
            val (rightMaxLength, rightMinLength) = rightChild.maxAndMinLength()
            max(leftMaxLength, rightMaxLength) + 1 to min(leftMinLength, rightMinLength) + 1
        }
        else ->
            throw IllegalArgumentException()
    }

val (maxLength, minLength) = minNodeHeap.poll().maxAndMinLength()
println("1. $maxLength\n2. $minLength")