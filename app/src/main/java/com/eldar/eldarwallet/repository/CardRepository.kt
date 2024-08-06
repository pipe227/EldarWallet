package com.eldar.eldarwallet.repository

import com.eldar.eldarwallet.model.card.Card
import com.eldar.eldarwallet.model.card.CardDao

class CardRepository(private val cardDao: CardDao) {

    fun insertCard(card: Card) {
        cardDao.insertCard(card)
    }

    fun getCardsByUserId(userId: Int): List<Card> {
        return cardDao.getCardsByUserId(userId)
    }
}