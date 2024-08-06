package com.eldar.eldarwallet.ui.payment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.eldar.eldarwallet.databinding.FragmentPaymentBinding
import com.eldar.eldarwallet.model.AppDatabase
import com.eldar.eldarwallet.model.card.Card
import com.eldar.eldarwallet.repository.CardRepository
import com.eldar.eldarwallet.adapter.CardAdapter
import com.eldar.eldarwallet.util.AppConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PaymentFragment : Fragment() {

    private var _binding: FragmentPaymentBinding? = null
    private val binding get() = _binding!!

    private lateinit var cardAdapter: CardAdapter
    private val cards = mutableListOf<Card>()
    private val userId = AppConstants.USER_ID

    private lateinit var cardRepository: CardRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = AppDatabase.getDatabase(requireContext())
        cardRepository = CardRepository(db.cardDao())

        setupRecyclerView()
        loadCards()

        binding.payButton.setOnClickListener {
            val selectedCard = cardAdapter.getSelectedCard()
            if (selectedCard != null) {
                val intent = Intent(requireContext(), PaymentActivity::class.java)
                intent.putExtra("cardId", selectedCard.id)
                startActivity(intent)
            } else {
                Toast.makeText(requireContext(), "Please select a card", Toast.LENGTH_SHORT).show()
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
}
