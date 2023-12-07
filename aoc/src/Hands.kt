import Card.Companion.toCard
import java.io.File

fun main() {

  val lines = File("aoc/inputs/hands.txt").readLines()

  println(linesToWinnings(lines))
}

internal fun linesToWinnings(lines: List<String>): Int {
  return lines
    .map { line ->
      HandWinnings(
        Hand(line.substring(0, 5).map { it.toCard() }),
        line.substring(6).toInt()
      )
    }
    .sortedByDescending(HandWinnings::hand)
    .mapIndexed { ix, handWinnings -> (ix + 1) * handWinnings.winnings }
    .sum()
}

data class HandWinnings(val hand: Hand, val winnings: Int)

enum class Card(val representation: Char) {
  Ace('A'),
  King('K'),
  Queen('Q'),
  // Jack('J'),
  Ten('T'),
  Nine('9'),
  Eight('8'),
  Seven('7'),
  Six('6'),
  Five('5'),
  Four('4'),
  Three('3'),
  Two('2'),
  Joker('J');

  companion object {
    fun Char.toCard(): Card {
      for (card in entries) {
        if (this == card.representation) {
          return card
        }
      }
      throw IllegalArgumentException("Non-card character '$this'")
    }
  }
}

enum class Type { Five, Four, FullHouse, Three, TwoPair, OnePair, High }

class Hand(private val cards: List<Card>) : Comparable<Hand> {

  private val distribution: Map<Card, Int> =
    cards.fold(mutableMapOf()) { dist, card ->
      dist[card] = dist.getOrDefault(card, 0) + 1
      dist
    }

  private val type: Type
    get() {
      return when (distribution.size) {
        1 -> Type.Five
        2 -> if (distribution.values.any { it == 4 }) Type.Four else Type.FullHouse
        3 -> if (distribution.values.any { it == 3 }) Type.Three else Type.TwoPair
        4 -> Type.OnePair
        5 -> Type.High
        else -> throw IllegalStateException("Wrong number of cards")
      }
    }

  private val complexType: Type
    get() {

      var nonJokers = 0
      val distribution = cards.fold(mutableMapOf<Card, Int>()) { dist, card ->
        if (card != Card.Joker) {
          dist[card] = dist.getOrDefault(card, 0) + 1
          nonJokers++
        }
        dist
      }
      val jokers = 5 - nonJokers

      return when (distribution.size) {
        0 -> Type.Five // All jokers
        1 -> Type.Five // Jokers become the one other card type
        2 -> if (distribution.values.any { it == nonJokers - 1 }) Type.Four else Type.FullHouse
        3 -> if (distribution.values.any { it == 3-jokers }) Type.Three else Type.TwoPair
        4 -> Type.OnePair
        5 -> Type.High
        else -> throw IllegalStateException("Wrong number of cards")
      }

    }

  override operator fun compareTo(other: Hand): Int {
    if (this.complexType != other.complexType) {
      return this.complexType.compareTo(other.complexType)
    }

    for (i in 0..4) {
      if (this.cards[i] != other.cards[i]) {
        return this.cards[i].compareTo(other.cards[i])
      }
    }
    return 0
  }
}