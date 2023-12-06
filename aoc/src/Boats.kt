import java.io.File

data class Race(val time: Long, val highscore: Long) {
  fun waysToWin(): Long {
    var ways = 0L
    for (i in 1..time) {
      val score = (time - i) * i
      if (score > highscore) ways++
    }
    return ways
  }
}

fun main() {
  val lines = File("aoc/inputs/boats.txt").readLines()
  // printSeparateRaces(lines)
  val time = lines[0].split(":")[1].replace(" ", "").toLong()
  val score = lines[1].split(":")[1].replace(" ", "").toLong()
  println(Race(time, score).waysToWin())

}

private fun printSeparateRaces(lines: List<String>) {
  val times = Regex("\\d+").findAll(lines[0]).map { it.value.toLong() }
  val scores = Regex("\\d+").findAll(lines[1]).map { it.value.toLong() }

  val races = times.zip(scores).map { (time, score) -> Race(time, score) }

  println(races.map { it.waysToWin() }.reduce(Long::times))
}