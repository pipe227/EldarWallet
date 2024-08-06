package com.eldar.eldarwallet.presenter

import android.util.Log
import com.eldar.eldarwallet.model.qr.QRApiService
import com.eldar.eldarwallet.model.qr.QRContract
import com.eldar.eldarwallet.model.user.UserDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
class QRPresenter(
    private val view: QRContract.View,
    private val userDao: UserDao,
    private val qrService: QRApiService
) : QRContract.Presenter {

    override fun generateQRCode(userId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val user = userDao.getUserById(userId)
            if (user != null) {
                val nameAndLastName = "${user.firstName} ${user.lastName}"
                val qrUrl = qrService.generateQRCode(nameAndLastName)
                withContext(Dispatchers.Main) {
                    if (qrUrl != null) {
                        view.onQRCodeGenerated(qrUrl)
                    } else {
                        view.onQRCodeGenerationFailed("Failed to generate QR code")
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    view.onQRCodeGenerationFailed("User not found")
                }
            }
        }
    }
}
