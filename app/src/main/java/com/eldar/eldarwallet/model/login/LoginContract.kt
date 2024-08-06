package com.eldar.eldarwallet.model.login

import com.eldar.eldarwallet.model.user.User

interface LoginContract {
    interface View {
        fun onLoginSuccess(user: User)
        fun onLoginFailed(error: String)
    }

    interface Presenter {
        fun login(username: String, password: String)
    }
}