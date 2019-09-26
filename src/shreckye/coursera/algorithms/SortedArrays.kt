package shreckye.coursera.algorithms

fun mergeSortedDistinct(numbers1: IntArray, numbers2: IntArray): IntArray {
    val size1 = numbers1.size
    val size2 = numbers2.size
    val numbers = IntArray(size1 + size2)

    var i = 0
    var j = 0
    var k = 0
    while (i < size1 && j < size2) {
        val number1 = numbers1[i]
        val number2 = numbers2[j]
        numbers[k++] = if (number1 < number2) {
            i++
            number1
        } else if (number1 > number2) {
            j++
            number2
        } else {
            i++
            j++
            number1
        }
    }

    while (i < size1)
        numbers[k++] = numbers1[i++]
    while (j < size2)
        numbers[k++] = numbers2[j++]

    return numbers.copyOf(k)
}