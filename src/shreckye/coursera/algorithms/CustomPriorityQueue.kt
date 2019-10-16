package shreckye.coursera.algorithms

import java.util.*
import kotlin.collections.ArrayList

// A custom priority queue implementation that supports O(log(n)) deletion and replacement with holders
class CustomPriorityQueue<T : Any>
private constructor(private val queue: ArrayList<Holder<T>>, private val comparator: Comparator<T>) :
    AbstractQueue<T>() {
    class Holder<T>(internal var index: Int, element: T) {
        var value: T = element
            internal set
    }

    override val size: Int get() = queue.size

    companion object {
        @Suppress("NOTHING_TO_INLINE")
        inline fun Int.parentIndex() = (this - 1) / 2

        @Suppress("NOTHING_TO_INLINE")
        inline fun Int.childBaseIndex() = this * 2

        @Suppress("NOTHING_TO_INLINE")
        inline fun Int.leftChildIndexIndex() = childBaseIndex() + 1

        @Suppress("NOTHING_TO_INLINE")
        inline fun Int.rightChildIndexIndex() = childBaseIndex() + 2
    }

    fun swap(index1: Int, index2: Int) =
        swap(index1, queue[index1], index2, queue[index2])

    fun moveTo(index: Int, holder: Holder<T>) {
        queue[index] = holder
        holder.index = index
    }

    fun swap(index1: Int, holder1: Holder<T>, index2: Int, holder2: Holder<T>) {
        moveTo(index1, holder2)
        moveTo(index2, holder1)
    }

    constructor(initialCapacity: Int, comparator: Comparator<T>) :
            this(ArrayList<Holder<T>>(initialCapacity), comparator)

    // Heapify
    constructor(elements: List<T>, comparator: Comparator<T>) :
            this(ArrayList(elements.mapIndexed(::Holder)), comparator) {
        for ((index, holder) in queue.asReversed().withIndex())
            bubbleDown(index, holder)
    }

    fun offerAndGetHolder(e: T): Holder<T> {
        val index = size
        val holder = Holder(index, e)
        queue.add(index, holder)
        bubbleUp(index, holder)
        return holder
    }

    override fun offer(e: T): Boolean {
        offerAndGetHolder(e)
        return true
    }

    override fun iterator(): MutableIterator<T> = object : MutableIterator<T> {
        var index = 0
        override fun hasNext(): Boolean = index < size
        override fun next(): T = queue[index++].value
        // Implementing an iterator for a priority queue is too complicated and not necessary here
        override fun remove() = throw NotImplementedError()
    }

    private fun removeAt(index: Int, holder: Holder<T> = queue[index]) {
        val lastIndex = queue.size - 1
        val newHolder = queue.removeAt(lastIndex)
        if (index < lastIndex) {
            moveTo(index, newHolder)
            bubbleUpOrDown(comparator.compare(newHolder.value, holder.value), index, newHolder)
        }
    }

    fun removeByHolder(holder: Holder<T>) =
        removeAt(holder.index, holder)

    fun replaceByHolder(holder: Holder<T>, newValue: T): Holder<T> {
        val oldValue = holder.value
        holder.value = newValue
        bubbleUpOrDown(comparator.compare(newValue, oldValue), holder.index, holder)
        return holder
    }

    override fun peek(): T =
        queue.first().value

    override fun poll(): T {
        val value = queue.first().value
        val newHolder = queue.removeAt(queue.size - 1)
        if (queue.isNotEmpty()) {
            moveTo(0, newHolder)
            bubbleDown(0, newHolder)
        }

        return value
    }

    private tailrec fun bubbleUp(index: Int, holder: Holder<T> = queue[index]) {
        val parentIndex = index.parentIndex()
        if (parentIndex < 0) return

        val parentHolder = queue[parentIndex]
        if (comparator.compare(parentHolder.value, holder.value) > 0) {
            swap(index, holder, parentIndex, parentHolder)
            bubbleUp(parentIndex, holder)
        }
    }

    private fun getSmallerChildHolder(index: Int): Holder<T>? {
        val childBaseIndex = index.childBaseIndex()
        val leftChildIndex = childBaseIndex + 1
        if (leftChildIndex >= size)
            return null
        val leftChildHolder = queue[leftChildIndex]
        val rightChildIndex = childBaseIndex + 2
        return if (rightChildIndex >= size)
            leftChildHolder
        else {
            val rightChildHolder = queue[rightChildIndex]
            if (comparator.compare(leftChildHolder.value, rightChildHolder.value) <= 0)
                leftChildHolder
            else
                rightChildHolder
        }
    }

    private tailrec fun bubbleDown(index: Int, holder: Holder<T> = queue[index]) {
        val smallerChildHolder = getSmallerChildHolder(index)

        if (smallerChildHolder !== null && comparator.compare(holder.value, smallerChildHolder.value) > 0) {
            val smallerChildIndex = smallerChildHolder.index
            swap(index, holder, smallerChildIndex, smallerChildHolder)
            bubbleDown(smallerChildIndex, holder)
        }
    }

    /*private fun bubbleDownEmpty(index: Int) {
        val smallerChildHolder = getSmallerChildHolder(index)

        if (smallerChildHolder !== null)
            moveTo(index, smallerChildHolder)
    }*/

    /*private fun bubbleUpOrDown(index: Int, holder: Holder<T> = queue[index]) {
        // Simple implementation with redundant checks
        bubbleUp(index, holder)
        bubbleDown(index, holder)
    }*/
    private fun bubbleUpOrDown(compareResult: Int, index: Int, holder: Holder<T> = queue[index]) {
        if (compareResult < 0)
            bubbleUp(index, holder)
        else if (compareResult > 0)
            bubbleDown(index, holder)
    }
}