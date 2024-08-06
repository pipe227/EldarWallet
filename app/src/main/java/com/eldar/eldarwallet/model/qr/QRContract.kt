package com.eldar.eldarwallet.model.qr


interface QRContract {
    interface View {
        fun onQRCodeGenerated(qrUrl: String)
        fun onQRCodeGenerationFailed(error: String)
    }

    interface Presenter {
        fun generateQRCode(userId: Int)
    }
}