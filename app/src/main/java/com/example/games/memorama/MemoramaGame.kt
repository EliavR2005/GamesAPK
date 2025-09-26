package com.example.games.memorama

// Modelo de carta
data class CardModel(
    val id: Int,
    val content: String,
    val isFaceUp: Boolean = false,
    val isMatched: Boolean = false
)

class MemoramaGame {
    private val emojis = listOf("🍎", "🚗", "🐶", "🎲", "⚽", "🍕", "🎸", "🚀")

    fun createCards(): List<CardModel> {
        val cards = (emojis + emojis)
            .shuffled()
            .mapIndexed { index, emoji ->
                CardModel(id = index, content = emoji)
            }
        return cards
    }

    fun checkWinCondition(cards: List<CardModel>): Boolean {
        return cards.all { it.isMatched }
    }

    fun processTurn(selectedCards: List<CardModel>, currentCards: List<CardModel>): List<CardModel> {
        return if (selectedCards.size == 2 && selectedCards[0].content == selectedCards[1].content) {
            currentCards.map { card ->
                if (card.id == selectedCards[0].id || card.id == selectedCards[1].id) {
                    card.copy(isMatched = true, isFaceUp = true)
                } else {
                    card
                }
            }
        } else {
            currentCards.map { card ->
                if (card.id == selectedCards[0].id || card.id == selectedCards[1].id) {
                    card.copy(isFaceUp = false)
                } else {
                    card
                }
            }
        }
    }
}
