package com.eldar.eldarwallet.repository

import com.eldar.eldarwallet.model.user.User
import com.eldar.eldarwallet.model.user.UserDao

class UserRepository(private val userDao: UserDao) {

    suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }

    suspend fun authenticate(username: String, password: String): User? {
        return userDao.authenticate(username, password)
    }

    suspend fun getUserById(userId: Int): User? {
        return userDao.getUserById(userId)
    }
}