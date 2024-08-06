package com.eldar.eldarwallet.model.card

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CardDao {

    @Insert
    fun insertCard(card: Card)

    @Query("SELECT * FROM cards WHERE userId = :userId")
    fun getCardsByUserId(userId: Int): List<Card>
}