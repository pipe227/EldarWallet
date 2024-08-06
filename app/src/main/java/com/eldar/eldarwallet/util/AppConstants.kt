package com.eldar.eldarwallet.util

object AppConstants {
    var USER_ID: Int = 1
        private set

    fun updateUserId(newUserId: Int) {
        USER_ID = newUserId
    }
}