package shreckye.coursera.algorithms

import java.util.*

/**
 * A simple memory efficient int array list, with some code copied and adapted from [ArrayList].
 * @see ArrayList
 * @see IntArray.asList
 * @see AbstractMutableList
 */
class SimpleIntArrayList : AbstractMutableList<Int> {
    companion object {
        private const val DEFAULT_CAPACITY = 10
        private val EMPTY_ELEMENTDATA = intArrayOf()
        private val DEFAULTCAPACITY_EMPTY_ELEMENTDATA = intArrayOf()
        private const val MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8
    }

    override var size = 0
    var elementData: IntArray

    constructor(initialCapacity: Int) : super() {
        if (initialCapacity > 0) {
            this.elementData = IntArray(initialCapacity)
        } else if (initialCapacity == 0) {
            this.elementData = EMPTY_ELEMENTDATA
        } else {
            throw IllegalArgumentException("Illegal Capacity: $initialCapacity")
        }
    }

    constructor() : super() {
        elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA
    }

    constructor(elementData: IntArray) : super() {
        size = elementData.size
        this.elementData = elementData
    }


    override fun add(index: Int, element: Int) {
        if (index == elementData.size)
            elementData = grow()
        elementData[index] = element
        size = index + 1
    }

    private fun grow(minCapacity: Int = size + 1): IntArray =
        elementData.copyOf(newCapacity(minCapacity))

    private fun newCapacity(minCapacity: Int): Int {
        // overflow-conscious code
        val oldCapacity = elementData.size
        val newCapacity = oldCapacity + (oldCapacity shr 1)
        if (newCapacity - minCapacity <= 0) {
            if (elementData === DEFAULTCAPACITY_EMPTY_ELEMENTDATA)
                return kotlin.math.max(DEFAULT_CAPACITY, minCapacity)
            if (minCapacity < 0)
            // overflow
                throw OutOfMemoryError()
            return minCapacity
        }
        return if (newCapacity - MAX_ARRAY_SIZE <= 0)
            newCapacity
        else
            hugeCapacity(minCapacity)
    }

    private fun hugeCapacity(minCapacity: Int): Int {
        if (minCapacity < 0)
        // overflow
            throw OutOfMemoryError()
        return if (minCapacity > MAX_ARRAY_SIZE)
            Integer.MAX_VALUE
        else
            MAX_ARRAY_SIZE
    }

    override fun get(index: Int): Int {
        Objects.checkIndex(index, size)
        return elementData[index]
    }

    override fun removeAt(index: Int): Int {
        Objects.checkIndex(index, size)

        val oldValue = elementData[index]
        fastRemove(elementData, index)

        return oldValue
    }

    private fun fastRemove(es: IntArray, i: Int) {
        modCount++
        val newSize = size - 1
        if (newSize > i)
            System.arraycopy(es, i + 1, es, i, newSize - i)
        size = newSize
        //es[newSize] = 0
    }

    override fun set(index: Int, element: Int): Int {
        Objects.checkIndex(index, size)
        val oldValue = elementData[index]
        elementData[index] = element
        return oldValue
    }

    fun trimToSize() {
        modCount++
        if (size < elementData.size) {
            elementData = if (size == 0)
                EMPTY_ELEMENTDATA
            else
                Arrays.copyOf(elementData, size)
        }
    }
}

fun Iterable<Int>.toSimpleIntArrayList() =
    toCollection(SimpleIntArrayList())

fun Iterable<Int>.toSimpleIntArrayList(size: Int) =
    toCollection(SimpleIntArrayList(size))

fun Sequence<Int>.toSimpleIntArrayList() =
    toCollection(SimpleIntArrayList())

fun Sequence<Int>.toSimpleIntArrayList(size: Int) =
    toCollection(SimpleIntArrayList(size))

fun IntArray.asSimpleIntArrayList() =
    SimpleIntArrayList(this)

fun simpleIntArrayListOf(vararg elements: Int) =
    elements.asSimpleIntArrayList()