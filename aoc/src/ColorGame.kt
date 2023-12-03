import java.io.File

fun main() {
 // problem1()
  problem2()
}

private fun problem2() {
  var powerSum = 0
  val colors = listOf("red", "green", "blue")
  File("inputs/colorGame.txt").forEachLine { line ->
    val split = line.split(":")
    val maxes = colors.associateWith { 0 }.toMutableMap()
    split[1].split(";").forEach {
      it.split(",").forEach { color ->
        for ((name, max) in maxes) {
          if (color.contains(name)) {
            val count = Regex("\\d+").find(color)?.value?.toInt() ?: 0
            if (count > max) {
              maxes[name] = count
            }
          }
        }
      }
    }
    powerSum += maxes.values.reduce { acc, value -> acc * value }
  }
  println(powerSum)
}

private fun problem1() {
  val colors = mapOf("red" to 12, "green" to 13, "blue" to 14)
  var validGamesSum = 0
  File("inputs/colorGame.txt").forEachLine { line ->
    val split = line.split(":")
    val gameId = split[0].substring(5).toInt()
    split[1].split(";").forEach {
      it.split(",").forEach { color ->
        for ((name, max) in colors) {
          if (color.contains(name)) {
            if ((Regex("\\d+").find(color)?.value?.toInt() ?: 0) > max) {
              return@forEachLine
            }
          }
        }
      }
    }
    validGamesSum += gameId
  }
  println(validGamesSum)
}
