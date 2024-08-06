package com.eldar.eldarwallet.presenter

import com.eldar.eldarwallet.model.login.LoginContract
import com.eldar.eldarwallet.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginPresenter(
    private val view: LoginContract.View,
    private val userRepository: UserRepository
) : LoginContract.Presenter {

    override fun login(username: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val user = userRepository.authenticate(username, password)
            withContext(Dispatchers.Main) {
                if (user != null) {
                    view.onLoginSuccess(user)
                } else {
                    view.onLoginFailed("Invalid username or password")
                }
            }
        }
    }
}