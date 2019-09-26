package shreckye.coursera.algorithms

import kotlin.math.max
import kotlin.test.assertEquals

// Numbers are represented by a string in reverse order
/*inline*/ class NonnegativeDecimalInteger private constructor(private val standardizedNumberString: String) {
    companion object {
        // Standardized numbers have no redundant zeros, including 0 (should be represented as an empty string)
        private fun String.standardized() = trimEnd('0')

        fun fromUnstandardizedNumberString(unstandardizedNumberString: String) =
            NonnegativeDecimalInteger(unstandardizedNumberString.standardized())

        fun fromString(string: String): NonnegativeDecimalInteger =
            fromUnstandardizedNumberString(string.reversed())

        val ZERO = NonnegativeDecimalInteger("")
    }

    // These 2 extension functions are moved inside the class due to an issue at https://youtrack.jetbrains.com/issue/KT-33764
    @Suppress("NOTHING_TO_INLINE")
    inline fun Char.toDigit(): Int = this - '0'

    @Suppress("NOTHING_TO_INLINE")
    inline fun Int.toDigitChar(): Char = '0' + this

    fun isZero() = standardizedNumberString.isEmpty()
    operator fun get(index: Int) = standardizedNumberString[index].toDigit()
    fun getOrZero(index: Int) = standardizedNumberString.getOrNull(index)?.toDigit() ?: 0
    val numDigits get() = standardizedNumberString.length
    fun substring(startIndex: Int, endIndex: Int = standardizedNumberString.length) =
        fromUnstandardizedNumberString(standardizedNumberString.substring(startIndex, endIndex))

    override fun toString() = standardizedNumberString.reversed().run { if (isEmpty()) "0" else this }

    infix operator fun plus(y: NonnegativeDecimalInteger): NonnegativeDecimalInteger {
        val m = max(numDigits, y.numDigits)
        val r = CharArray(m + 1)
        var carry = 0
        for (i in 0 until m) {
            val sum = getOrZero(i) + y.getOrZero(i) + carry
            if (sum < 10) {
                r[i] = sum.toDigitChar()
                carry = 0
            } else {
                r[i] = (sum - 10).toDigitChar()
                carry = 1
            }
        }

        return if (carry == 0)
            NonnegativeDecimalInteger(String(r, 0, m))
        else {
            r[m] = '0' + carry
            NonnegativeDecimalInteger(String(r))
        }
    }

    // Assuming positive
    infix operator fun minus(y: NonnegativeDecimalInteger): NonnegativeDecimalInteger {
        val r = CharArray(numDigits)
        var carry = 0
        for (i in 0 until numDigits) {
            // Converting to Int can be omitted
            val diff = this[i] - carry - y.getOrZero(i)
            if (diff >= 0) {
                r[i] = diff.toDigitChar()
                carry = 0
            } else {
                r[i] = (diff + 10).toDigitChar()
                carry = 1
            }
        }

        return fromUnstandardizedNumberString(String(r))
    }

    fun times10Pow(n: Int): NonnegativeDecimalInteger =
        NonnegativeDecimalInteger("0".repeat(n) + standardizedNumberString)
}

@Suppress("NOTHING_TO_INLINE")
inline fun String.toDecimalInteger() = NonnegativeDecimalInteger.fromString(this)

@Suppress("NOTHING_TO_INLINE")
inline fun Int.toDecimalInteger() = NonnegativeDecimalInteger.fromString(toString())

// All inputs and outputs of the following functions should be standardized

infix fun NonnegativeDecimalInteger.karatsubaTimes(y: NonnegativeDecimalInteger): NonnegativeDecimalInteger {
    // At least one is 0
    if (isZero() || y.isZero())
        return NonnegativeDecimalInteger.ZERO

    // Single non-zero digits
    var m = max(numDigits, y.numDigits)
    if (m == 1)
        return (this[0] * y[0]).toDecimalInteger()

    m /= 2

    // May throw exceptions when m is greater than length
    val x0: NonnegativeDecimalInteger
    val x1: NonnegativeDecimalInteger
    if (m < numDigits) {
        x0 = substring(0, m)
        x1 = substring(m)
    } else {
        x0 = this
        x1 = NonnegativeDecimalInteger.ZERO
    }

    val y0: NonnegativeDecimalInteger
    val y1: NonnegativeDecimalInteger
    if (m < y.numDigits) {
        y0 = y.substring(0, m)
        y1 = y.substring(m)
    } else {
        y0 = y
        y1 = NonnegativeDecimalInteger.ZERO
    }

    val z0 = x0 karatsubaTimes y0
    val z2 = x1 karatsubaTimes y1
    val z1 = ((x0 + x1) karatsubaTimes (y0 + y1)) - z0 - z2
    //println("$this $y $x0 $x1 $y0 $y1 $z0 $z1 $z2")
    return z0 + z1.times10Pow(m) + z2.times10Pow(2 * m)
}

fun karatsubaTimesWithString(xString: String, yString: String): String {
    val x = xString.toDecimalInteger()
    val y = yString.toDecimalInteger()
    val r = x karatsubaTimes y
    return r.toString()
}

println(karatsubaTimesWithString(args[0], args[1]))


// Test cases
assertEquals("6", karatsubaTimesWithString("2", "3"))
assertEquals("726", karatsubaTimesWithString("22", "33"))
assertEquals("100000000", karatsubaTimesWithString("10000", "10000"))
for (i in 0 until 1024)
    for (j in 0 until 1024) {
        val rString = karatsubaTimesWithString(i.toString(), j.toString())
        assertEquals((i * j).toString(), rString, "i = $i, j = $j")
    }