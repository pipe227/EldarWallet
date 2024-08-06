package com.eldar.eldarwallet.model.userbalance

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserBalanceDao {
    @Insert
     fun insertBalance(userBalance: UserBalance)

    @Update
     fun updateBalance(userBalance: UserBalance)

    @Query("SELECT * FROM user_balances WHERE userId = :userId")
     fun getBalance(userId: Int): UserBalance?
}