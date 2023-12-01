import Calibration.digitAndWordLineToNumber
import Calibration.readCalibrationDigitsAndWords
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class CalibrationTest {

  @Test
  fun digitAndWordLineToNumber() {
    assertThat(digitAndWordLineToNumber("two1nine")).isEqualTo(29)
    assertThat(digitAndWordLineToNumber("eightwothree")).isEqualTo(83)
    assertThat(digitAndWordLineToNumber("abcone2threexyz")).isEqualTo(13)
    assertThat(digitAndWordLineToNumber("xtwone3four")).isEqualTo(24)
    assertThat(digitAndWordLineToNumber("4nineeightseven2")).isEqualTo(42)
    assertThat(digitAndWordLineToNumber("zoneight234")).isEqualTo(14)
    assertThat(digitAndWordLineToNumber("7pqrstsixteen")).isEqualTo(76)
  }

  @Test
  fun digitAndWordLineToNumber_overlapping_trailingNumber() {
    assertThat(digitAndWordLineToNumber("1oneight")).isEqualTo(18)
  }

  @Test
  fun readCalibrationDigitsAndWords() {
    assertThat(readCalibrationDigitsAndWords("inputs/testCalibration.txt").sum()).isEqualTo(281)
  }
}
