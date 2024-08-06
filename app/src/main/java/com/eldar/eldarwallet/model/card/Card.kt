package com.eldar.eldarwallet.model.card

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cards")
data class Card(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val name: String,
    val number: String,
    val securityCode: String,
    val expirationDate: String
)
