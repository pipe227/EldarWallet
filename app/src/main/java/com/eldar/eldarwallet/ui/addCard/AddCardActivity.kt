package com.eldar.eldarwallet.ui.addCard

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.braintreepayments.cardform.view.CardForm
import com.eldar.eldarwallet.databinding.ActivityAddCardBinding
import com.eldar.eldarwallet.model.AppDatabase
import com.eldar.eldarwallet.model.card.Card
import com.eldar.eldarwallet.model.userbalance.UserBalance
import com.eldar.eldarwallet.repository.CardRepository
import com.eldar.eldarwallet.repository.UserBalanceRepository
import com.eldar.eldarwallet.util.AppConstants
import com.eldar.eldarwallet.util.EncryptionUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddCardActivity : AppCompatActivity(), AddCardView {

    private lateinit var binding: ActivityAddCardBinding
    private lateinit var cardForm: CardForm
    private val userId = AppConstants.USER_ID

    private lateinit var cardRepository: CardRepository
    private lateinit var userBalanceRepository: UserBalanceRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = AppDatabase.getDatabase(this)
        cardRepository = CardRepository(db.cardDao())
        userBalanceRepository = UserBalanceRepository(db.userBalanceDao())


        cardForm = binding.cardForm
        cardForm.cardRequired(true)
            .expirationRequired(true)
            .cvvRequired(true)
            .cardholderName(CardForm.FIELD_REQUIRED)
            .setup(this)

        binding.saveButton.setOnClickListener {
            if (cardForm.isValid) {
                saveCard()
            } else {
                cardForm.validate()
            }
        }
    }

    private fun saveCard() {
        val secretKey = "your-secret-key-123"

        val cardNumber = EncryptionUtils.encrypt(cardForm.cardNumber, secretKey)
        val securityCode = EncryptionUtils.encrypt(cardForm.cvv, secretKey)
        val expirationDate = EncryptionUtils.encrypt("${cardForm.expirationMonth}/${cardForm.expirationYear}", secretKey)

        val card = Card(
            userId = userId,
            name = cardForm.cardholderName,
            number = cardNumber,
            securityCode = securityCode,
            expirationDate = expirationDate
        )


        lifecycleScope.launch(Dispatchers.IO) {
            cardRepository.insertCard(card)
            val userBalance = userBalanceRepository.getBalance(userId)
            if (userBalance != null) {
                val newBalance = userBalance.balance + 1500
                userBalanceRepository.updateBalance(UserBalance(userId, newBalance))
            } else {
                val initialBalance = UserBalance(userId, 1500.0)
                userBalanceRepository.insertBalance(initialBalance)
            }
            withContext(Dispatchers.Main) {
                Toast.makeText(this@AddCardActivity, "Tarjeta guardada exitosamente", Toast.LENGTH_SHORT).show()
                setResult(RESULT_OK)
                finish()
            }
        }
    }
}