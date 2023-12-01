import java.io.File

fun main() {
  println(Calibration.readCalibrationDigitsAndWords("aoc/inputs/calibration.txt").sum())
}

object Calibration {
  fun readCalibrationDigits(): List<Int> = buildList {
    File("aoc/inputs/calibration.txt").forEachLine { line ->
      val allDigits = Regex("[1-9]").findAll(line).map { it.value }
      val twoDigits = (allDigits.first() + allDigits.last()).toInt()
      add(twoDigits)
    }
  }

  fun readCalibrationDigitsAndWords(fileLocation: String): List<Int> = buildList {
    File(fileLocation).forEachLine { line ->
      add(digitAndWordLineToNumber(line))
    }
  }

  fun digitAndWordLineToNumber(line: String): Int {
    val allDigits = Regex("(?=([1-9]|one|two|three|four|five|six|seven|eight|nine)).")
      .findAll(line)
      .mapNotNull { it.groups[1]?.value }
      .map(::numberToDigit)
      .toList()
    return (allDigits.first() + allDigits.last()).toInt()
  }

  private fun numberToDigit(number: String) = when (number) {
    // No zeros here!
    "one" -> "1"
    "two" -> "2"
    "three" -> "3"
    "four" -> "4"
    "five" -> "5"
    "six" -> "6"
    "seven" -> "7"
    "eight" -> "8"
    "nine" -> "9"
    else -> number
  }
}


