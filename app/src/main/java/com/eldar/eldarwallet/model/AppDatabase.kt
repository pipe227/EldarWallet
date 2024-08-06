package com.eldar.eldarwallet.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.eldar.eldarwallet.model.card.Card
import com.eldar.eldarwallet.model.card.CardDao
import com.eldar.eldarwallet.model.user.User
import com.eldar.eldarwallet.model.userbalance.UserBalance
import com.eldar.eldarwallet.model.userbalance.UserBalanceDao
import com.eldar.eldarwallet.model.user.UserDao

@Database(entities = [Card::class, UserBalance::class, User::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cardDao(): CardDao
    abstract fun userBalanceDao(): UserBalanceDao
    abstract fun userDao(): UserDao


    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            instance ?: synchronized(this) { instance ?: buildDatabase(context).also { instance = it } }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "eldarwallet.db")
                .build()
    }
}