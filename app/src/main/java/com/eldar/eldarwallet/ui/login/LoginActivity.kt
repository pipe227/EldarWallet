package com.eldar.eldarwallet.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.eldar.eldarwallet.R
import com.eldar.eldarwallet.model.AppDatabase
import com.eldar.eldarwallet.model.user.User
import com.eldar.eldarwallet.presenter.LoginPresenter
import com.eldar.eldarwallet.model.login.LoginContract
import com.eldar.eldarwallet.repository.UserRepository
import com.eldar.eldarwallet.ui.main.MainActivity
import com.eldar.eldarwallet.util.AppConstants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity(), LoginContract.View {

    private lateinit var presenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val db = AppDatabase.getDatabase(this)
        val userDao = db.userDao()
        val userRepository = UserRepository(userDao)
        presenter = LoginPresenter(this, userRepository)

        val usernameEditText: EditText = findViewById(R.id.usernameEditText)
        val passwordEditText: EditText = findViewById(R.id.passwordEditText)
        val loginButton: Button = findViewById(R.id.loginButton)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            presenter.login(username, password)
        }

        createTestUsers(userRepository)
    }

    override fun onLoginSuccess(user: User) {
        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
        AppConstants.updateUserId(user.id)
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onLoginFailed(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }

    private fun createTestUsers(userRepository: UserRepository) {
        CoroutineScope(Dispatchers.IO).launch {
            userRepository.insertUser(User(username = "user1", password = "password1", firstName = "John", lastName = "Doe"))
            userRepository.insertUser(User(username = "user2", password = "password2", firstName = "Jane", lastName = "Smith"))
            userRepository.insertUser(User(username = "user3", password = "password3", firstName = "Bob", lastName = "Johnson"))
        }
    }
}