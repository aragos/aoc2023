import java.io.File
import java.lang.IllegalArgumentException

data class Range(val sourceStart: Long, val destinationStart: Long, val rangeLength: Long) {
  val sources = LongRange(sourceStart, sourceStart + rangeLength - 1)
  override fun toString(): String {
    return "$sources -> ${LongRange(destinationStart, destinationStart + rangeLength - 1)}"
  }

  fun mapId(id: Long) = destinationStart + id - sourceStart

  fun invert() = Range(destinationStart, sourceStart, rangeLength)
}

class Mapping(val name: String, ranges: Set<Range>) {
  val sortedRanges = ranges.sortedBy { it.sources.start }

  fun map(sourceId: Long): Long {
    for (range in sortedRanges) {
      if (sourceId < range.sources.start) {
        return sourceId
      } else if (sourceId in range.sources) {
        return range.mapId(sourceId)
      }
    }
    return sourceId
  }

  override fun toString(): String {
    return "Mapping:\n" + sortedRanges.joinToString(separator = "\n") { "  $it" }
  }
}

fun main() {
  val lines = File("inputs/soil.txt").readLines()

  val rawSeeds = Regex("\\d+").findAll(lines[0].split(":")[1]).map { it.value.toLong() }
  val mappings = lines.parseMappings()

  // println(calculateMinLocationSimpleSeeds(rawSeeds, mappings))

  println(calculateMinLocationSeedRanges(rawSeeds, mappings))
}

fun calculateMinLocationSeedRanges(rawSeeds: Sequence<Long>, forwardMappings: List<Mapping>): Long {
  val seedRanges = rawSeeds
    .chunked(2)
    .map { pair -> LongRange(start = pair[0], endInclusive = pair[0] + pair[1] - 1) }
    .sortedBy { range -> range.start }
    .toList()

  val reverseMappings = forwardMappings.map { mapping ->
    Mapping("~${mapping.name}", mapping.sortedRanges.map { it.invert() }.toSet())
  }.reversed()

  val locations2Humidity = reverseMappings.first()

  locations2Humidity.sortedRanges.forEach { range ->
    range.sources.forEach { location ->
      val seed = reverseMappings.fold(location)
      seedRanges.forEach { seedRange ->
        if (seed in seedRange) {
          return location
        }
      }
    }
  }
  throw IllegalArgumentException()
}

private fun calculateMinLocationSimpleSeeds(
  rawSeeds: Sequence<Long>,
  mappings: List<Mapping>,
): Long = rawSeeds.map { value ->
  mappings.fold(value)
}.min()

private fun List<Mapping>.fold(value: Long) =
  this.fold(value) { acc, mapping ->
    mapping.map(acc)
  }

private fun List<String>.parseMappings(): List<Mapping> {
  val seed2Soil = parseMapping("seed2Soil", startInclusive = 3, endExclusive = 32)
  val soil2Fertilizer = parseMapping("soil2Fertilizer", startInclusive = 34, endExclusive = 53)
  val fertilizer2Water = parseMapping("fertilizer2Water", startInclusive = 55, endExclusive = 97)
  val water2Light = parseMapping("water2Light", startInclusive = 99, endExclusive = 117)
  val light2Temperature =
    parseMapping("light2Temperature", startInclusive = 119, endExclusive = 132)
  val temperature2Humidity =
    parseMapping("temperature2Humidity", startInclusive = 135, endExclusive = 144)
  val humidity2Location =
    parseMapping("humidity2Location", startInclusive = 146, endExclusive = 190)

  return listOf(
    seed2Soil,
    soil2Fertilizer,
    fertilizer2Water,
    water2Light,
    light2Temperature,
    temperature2Humidity,
    humidity2Location
  )
}

private fun List<String>.parseMapping(
  name: String,
  startInclusive: Int,
  endExclusive: Int,
): Mapping =
  Mapping(name, subList(startInclusive, endExclusive).map { line ->
    val numbers = Regex("\\d+").findAll(line).map { it.value.toLong() }.toList()
    Range(sourceStart = numbers[1], destinationStart = numbers[0], rangeLength = numbers[2])
  }.toSet())
