import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class SoilKtTest {

  @Test
  fun calculateMinLocationSeedRanges() {
    val seeds = sequenceOf(1L, 2L, 9L, 5L, 5L, 3L) // 1..2, 9..13, 5..7
    val mappings = listOf(
      Mapping("seeds2Things", setOf(
        Range(sourceStart = 8L, destinationStart = 20L, rangeLength = 2L), // 8..10 -> 20..21
        Range(sourceStart = 0L, destinationStart = 10L, rangeLength = 4L)  // 0..3  -> 10..13
      )),
      Mapping("things2Locations", setOf(
        Range(sourceStart = 11L, destinationStart = 100L, rangeLength = 10L), // 11..20 -> 100..109
        Range(sourceStart = 20L, destinationStart = 0L, rangeLength = 2L)     // 20..21 -> 0..1
      ))
    )

    // reversing through the mappings, lowest location that maps to a seed: 1 -> 21 -> 9
    assertThat(calculateMinLocationSeedRanges(seeds, mappings)).isEqualTo(9)
  }

  @Test
  fun calculateMinLocationSeedRanges_notCoveredByRange() {
    val seeds = sequenceOf(1L, 2L, 9L, 20L, 5L, 3L) // 1..2, 9..28, 5..7
    val mappings = listOf(
      Mapping("seeds2Things", setOf(
        Range(sourceStart = 8L, destinationStart = 21L, rangeLength = 2L), // 8..10 -> 21..23
        Range(sourceStart = 0L, destinationStart = 10L, rangeLength = 4L)  // 0..3  -> 10..13
      )),
      Mapping("things2Locations", setOf(
        Range(sourceStart = 11L, destinationStart = 100L, rangeLength = 10L), // 11..20 -> 100..109
        Range(sourceStart = 20L, destinationStart = 4L, rangeLength = 2L)     // 20..21 -> 4..5
      ))
    )

    // reversing through the mappings, lowest location that maps to a seed: 4 -> 20 -> 20 (no match)
    assertThat(calculateMinLocationSeedRanges(seeds, mappings)).isEqualTo(4)
  }
}
