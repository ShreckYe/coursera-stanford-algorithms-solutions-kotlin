package shreckye.coursera.algorithms

interface SimpleMutableMap<K, V> {
    operator fun get(key: K): V
    operator fun set(key: K, value: V)
}

fun IntArray.asSimpleMutableMap(): SimpleMutableMap<Int, Int> = object : SimpleMutableMap<Int, Int> {
    override fun get(key: Int): Int =
        this@asSimpleMutableMap[key]

    override fun set(key: Int, value: Int) {
        this@asSimpleMutableMap[key] = value
    }
}

fun Array<IntArray>.asSimpleMutableMap(): SimpleMutableMap<Pair<Int, Int>, Int> =
    object : SimpleMutableMap<Pair<Int, Int>, Int> {
        override fun get(key: Pair<Int, Int>): Int =
            this@asSimpleMutableMap[key.first][key.second]

        override fun set(key: Pair<Int, Int>, value: Int) {
            this@asSimpleMutableMap[key.first][key.second] = value
        }
    }

inline fun <K, V> MutableMap<K, V>.asSimpleMutableMap(
    crossinline isInDomain: (K) -> Boolean, defaultValue: V
): SimpleMutableMap<K, V> =
    object : SimpleMutableMap<K, V> {
        override fun get(key: K): V =
            if (isInDomain(key))
                this@asSimpleMutableMap[key] ?: defaultValue
            else
                throw IndexOutOfBoundsException()

        override fun set(key: K, value: V) {
            this@asSimpleMutableMap[key] = value
        }
    }