import java.io.File
import kotlin.math.pow

fun main() {
  println(countPoints())
  println(countCards())
}

private fun countCards(): Int {
  val cardCount = (0..211).associateWith { 1 }.toMutableMap()

  File("inputs/scratch.txt").readLines().mapIndexed() { ix, line ->
    if (line.isBlank()) return@mapIndexed

    val winningCount = countWinningNumbers(line)
    val times = cardCount[ix]!!
    for (j in (ix + 1)..ix + winningCount) {
      cardCount[j] = cardCount[j]!! + times
    }
  }

  return cardCount.values.sum()
}

private fun countPoints(): Int = File("inputs/scratch.txt").readLines().map { line ->
    val winningCount = countWinningNumbers(line)
    if (winningCount == 0) 0 else 2.0.pow(winningCount - 1).toInt()
  }.sum()

private fun countWinningNumbers(line: String): Int {
  val (winning, actual) = line.split(":")[1].split("|")
  val winningNumbers = winning.toNumbers().toSet()
  return actual.toNumbers().count { winningNumbers.contains(it) }
}

private fun String.toNumbers() = Regex("\\d+").findAll(this).map { it.value.toInt() }.toList()
