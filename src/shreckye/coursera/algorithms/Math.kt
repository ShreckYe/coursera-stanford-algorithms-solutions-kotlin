package shreckye.coursera.algorithms

infix fun Int.pow(y: Int): Int =
    when (y) {
        0 -> 1
        1 -> this
        else -> {
            val rPow = pow(y / 2)
            var r = rPow * rPow
            if (y % 2 == 1)
                r *= this
            r
        }
    }

fun Double.squared(): Double =
    this * this