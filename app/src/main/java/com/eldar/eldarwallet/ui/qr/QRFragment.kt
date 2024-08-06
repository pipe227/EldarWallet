package com.eldar.eldarwallet.ui.qr

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.eldar.eldarwallet.R
import com.eldar.eldarwallet.model.AppDatabase
import com.eldar.eldarwallet.model.qr.QRApiService
import com.eldar.eldarwallet.model.qr.QRContract
import com.eldar.eldarwallet.presenter.QRPresenter
import com.eldar.eldarwallet.repository.UserBalanceRepository
import com.eldar.eldarwallet.util.AppConstants
class QRFragment : Fragment(), QRContract.View {

    private lateinit var presenter: QRContract.Presenter
    private lateinit var qrImageView: ImageView
    private lateinit var generateQRButton: Button
    private lateinit var amountEditText: EditText
    private lateinit var userBalanceRepository: UserBalanceRepository
    private val userId = AppConstants.USER_ID

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_qr, container, false)

        qrImageView = view.findViewById(R.id.qrImageView)
        generateQRButton = view.findViewById(R.id.generateQRButton)
        amountEditText = view.findViewById(R.id.amountEditText)

        val db = AppDatabase.getDatabase(requireContext())
        val userDao = db.userDao()
        val qrService = QRApiService()
        presenter = QRPresenter(this, userDao, qrService)

        userBalanceRepository = UserBalanceRepository(db.userBalanceDao())

        generateQRButton.setOnClickListener {
            val amount = amountEditText.text.toString().toDoubleOrNull()
            if (amount != null && amount > 0) {
                presenter.generateQRCode(userId)
            } else {
                Toast.makeText(context, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    override fun onQRCodeGenerated(qrUrl: String) {
        Log.d("QRFragment", "Displaying QR Code")
        activity?.runOnUiThread {
            Glide.with(this)
                .load(qrUrl)
                .into(qrImageView)
        }
    }

    override fun onQRCodeGenerationFailed(error: String) {
        Log.e("QRFragment", "Error: $error")
        activity?.runOnUiThread {
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        }
    }
}