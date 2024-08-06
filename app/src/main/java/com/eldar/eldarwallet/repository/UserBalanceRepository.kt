package com.eldar.eldarwallet.repository

import com.eldar.eldarwallet.model.userbalance.UserBalance
import com.eldar.eldarwallet.model.userbalance.UserBalanceDao


class UserBalanceRepository(private val userBalanceDao: UserBalanceDao) {

    suspend fun getBalance(userId: Int): UserBalance? {
        return userBalanceDao.getBalance(userId)
    }

    suspend fun insertBalance(userBalance: UserBalance) {
        userBalanceDao.insertBalance(userBalance)
    }

    suspend fun updateBalance(userBalance: UserBalance) {
        userBalanceDao.updateBalance(userBalance)
    }
}