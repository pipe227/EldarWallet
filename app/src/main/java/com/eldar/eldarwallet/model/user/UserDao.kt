package com.eldar.eldarwallet.model.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {

    @Insert
     fun insertUser(user: User)

    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
     fun authenticate(username: String, password: String): User?

    @Query("SELECT * FROM users WHERE id = :userId")
     fun getUserById(userId: Int): User?
}