package com.eldar.eldarwallet.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.eldar.eldarwallet.R
import com.eldar.eldarwallet.model.card.Card
import com.eldar.eldarwallet.util.EncryptionUtils
class CardAdapter(private val cards: List<Card>) : RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    private var selectedCard: Card? = null
    private val secretKey = "your-secret-key-123"

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardNameTextView: TextView = itemView.findViewById(R.id.cardNameTextView)
        val cardNumberTextView: TextView = itemView.findViewById(R.id.cardNumberTextView)
        val cardSecurityCodeTextView: TextView = itemView.findViewById(R.id.cardSecurityCodeTextView)
        val cardExpirationDateTextView: TextView = itemView.findViewById(R.id.cardExpirationDateTextView)

        init {
            itemView.setOnClickListener {
                val previousSelectedPosition = cards.indexOf(selectedCard)
                selectedCard = if (selectedCard == cards[adapterPosition]) null else cards[adapterPosition]
                notifyItemChanged(previousSelectedPosition)
                notifyItemChanged(adapterPosition)
            }
        }

        fun bind(card: Card) {
            cardNameTextView.text = card.name
            cardNumberTextView.text = EncryptionUtils.decrypt(card.number, secretKey)
            cardSecurityCodeTextView.text = EncryptionUtils.decrypt(card.securityCode, secretKey)
            cardExpirationDateTextView.text = EncryptionUtils.decrypt(card.expirationDate, secretKey)
            itemView.setBackgroundColor(if (card == selectedCard) ContextCompat.getColor(itemView.context, R.color.selected_card_background) else ContextCompat.getColor(itemView.context, android.R.color.holo_blue_dark))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(cards[position])
    }

    override fun getItemCount() = cards.size

    fun getSelectedCard(): Card? = selectedCard

}