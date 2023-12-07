import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class HandsKtTest {

  @Test
  fun linesToWinnings() {
    val lines = """
    32T3K 765
    T55J5 684
    KK677 28
    KTJJT 220
    QQQJA 483
    """.trimIndent().lines()

    assertThat(linesToWinnings(lines)).isEqualTo(6440)
  }
}