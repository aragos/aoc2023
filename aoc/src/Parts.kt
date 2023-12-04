import java.io.File


fun main() {
  // countPartNumbers()
  countGearRatios()
}

private data class PartNumber(val value: Int, val location: IntRange) {
  fun isAdjacentTo(i: Int) = i in (location.first-1)..(location.last+1)
}

private fun countGearRatios() {
  var line1: Set<PartNumber>
  var line2 = setOf<PartNumber>()
  var line3 = setOf<PartNumber>()
  var previousLine = ""
  val allRatios = File("inputs/parts.txt").readLines().map { line ->
    line1 = line2
    line2 = line3
    line3 = Regex("\\d+").findAll(line).map { PartNumber(it.value.toInt(), it.range) }.toSet()

    val ratioSum = scanRatios(previousLine, line1 + line2 + line3)

    previousLine = line
    ratioSum
  }.sum()

  println(allRatios + scanRatios(previousLine, line2 + line3))
}

private fun scanRatios(
  previousLine: String,
  partNumbers1: Set<PartNumber>,
): Int {
  val ratioSum = previousLine.mapIndexedNotNull { ix, char ->
    if (char != '*') {
      null
    } else {
      val partNumbers = partNumbers1.filter { it.isAdjacentTo(ix) }
      if (partNumbers.size == 2) {
        partNumbers[0].value * partNumbers[1].value
      } else {
        null
      }
    }
  }.sum()
  return ratioSum
}


private fun countPartNumbers() {
  var line1: Set<Int>
  var line2 = setOf<Int>()
  var line3 = setOf<Int>()
  var previousLine = ""
  val allNumbers = File("inputs/parts.txt").readLines().map { line ->
    line1 = line2
    line2 = line3
    line3 =
      line.mapIndexedNotNull { ix, char -> if (char != '.' && char.isNotANumber()) ix else null }
        .toSet()
    val line2PartNumberSum = scanPartNumbers(previousLine, line1 + line2 + line3)
    previousLine = line
    line2PartNumberSum
  }.sum()

  println(allNumbers + scanPartNumbers(previousLine, line2 + line3))
}

private fun scanPartNumbers(
  partNumberLine: String,
  partLocations: Set<Int>,
): Int {
  return Regex("\\d+").findAll(partNumberLine).mapNotNull {
    val number = it.value.toInt()
    for (i in (it.range.first - 1)..(it.range.last + 1)) {
      if (i in partLocations) {
        return@mapNotNull number
      }
    }
    null
  }.sum()
}

private fun Char.isNotANumber() = digitToIntOrNull() == null
