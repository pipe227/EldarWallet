package com.eldar.eldarwallet.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.eldar.eldarwallet.databinding.FragmentHomeBinding
import com.eldar.eldarwallet.model.AppDatabase
import com.eldar.eldarwallet.model.card.Card
import com.eldar.eldarwallet.model.userbalance.UserBalance
import com.eldar.eldarwallet.repository.CardRepository
import com.eldar.eldarwallet.repository.UserBalanceRepository
import com.eldar.eldarwallet.ui.addCard.AddCardActivity
import com.eldar.eldarwallet.adapter.CardAdapter
import com.eldar.eldarwallet.util.AppConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var cardAdapter: CardAdapter
    private val cards = mutableListOf<Card>()
    private val userId = AppConstants.USER_ID
    private lateinit var userBalanceRepository: UserBalanceRepository

    private lateinit var cardRepository: CardRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = AppDatabase.getDatabase(requireContext())
        cardRepository = CardRepository(db.cardDao())
        userBalanceRepository = UserBalanceRepository(db.userBalanceDao())


        setupRecyclerView()
        loadCards()
        loadBalance()


        binding.addCardButton.setOnClickListener {
            startActivityForResult(Intent(requireContext(), AddCardActivity::class.java), ADD_CARD_REQUEST_CODE)
        }
    }
    fun refreshData() {
        loadBalance()
        loadCards()
    }
    private fun loadBalance() {
        GlobalScope.launch(Dispatchers.IO) {
            val userBalance = userBalanceRepository.getBalance(userId)
            if (userBalance != null) {
                withContext(Dispatchers.Main) {
                    binding.balanceTextView.text = "Balance: ${userBalance.balance}"
                }
            } else {
                val initialBalance = UserBalance(userId, 0.0)
                userBalanceRepository.insertBalance(initialBalance)
                withContext(Dispatchers.Main) {
                    binding.balanceTextView.text = "Balance: 0.0"
                }
            }
        }
    }

    private fun setupRecyclerView() {
        cardAdapter = CardAdapter(cards)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = cardAdapter
    }

    private fun loadCards() {
        GlobalScope.launch(Dispatchers.IO) {
            val cardList = cardRepository.getCardsByUserId(userId)
            withContext(Dispatchers.Main) {
                cards.clear()
                cards.addAll(cardList)
                cardAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_CARD_REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK) {
            refreshData()
        }
    }

    companion object {
        private const val ADD_CARD_REQUEST_CODE = 100
    }
}