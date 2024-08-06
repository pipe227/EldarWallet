package com.eldar.eldarwallet.model.userbalance

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_balances")
data class UserBalance(
    @PrimaryKey val userId: Int,
    val balance: Double
)
