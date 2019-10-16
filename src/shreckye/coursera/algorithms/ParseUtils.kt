package shreckye.coursera.algorithms

val WHITE_SPACE_REGEX = Regex("\\s+")
fun String.splitWithWhiteSpaceAndFilterEmpty(): List<String> = split(WHITE_SPACE_REGEX).filter(String::isNotEmpty)
fun String.splitToInts(): List<Int> = splitWithWhiteSpaceAndFilterEmpty().map(String::toInt)
fun String.splitToDoubles(): List<Double> = splitWithWhiteSpaceAndFilterEmpty().map(String::toDouble)