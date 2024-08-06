package com.eldar.eldarwallet.ui.payment

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.eldar.eldarwallet.R
import com.eldar.eldarwallet.model.AppDatabase
import com.eldar.eldarwallet.model.userbalance.UserBalance
import com.eldar.eldarwallet.repository.UserBalanceRepository
import com.eldar.eldarwallet.util.AppConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PaymentActivity : AppCompatActivity() {

    private lateinit var userBalanceRepository: UserBalanceRepository
    private val userId = AppConstants.USER_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        val amountEditText: EditText = findViewById(R.id.amountEditText)
        val payButton: Button = findViewById(R.id.payButton)

        val db = AppDatabase.getDatabase(this)
        userBalanceRepository = UserBalanceRepository(db.userBalanceDao())

        payButton.setOnClickListener {
            val amount = amountEditText.text.toString().toDoubleOrNull()
            if (amount != null && amount > 0) {
                simulateNfcPayment(amount)
            } else {
                Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun simulateNfcPayment(amount: Double) {
        GlobalScope.launch(Dispatchers.IO) {
            val userBalance = userBalanceRepository.getBalance(userId)
            if (userBalance != null) {
                val newBalance = userBalance.balance - amount
                if (newBalance >= 0) {
                    userBalanceRepository.updateBalance(UserBalance(userId, newBalance))
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@PaymentActivity, "Payment successful. New balance: $newBalance", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@PaymentActivity, "Insufficient balance", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}