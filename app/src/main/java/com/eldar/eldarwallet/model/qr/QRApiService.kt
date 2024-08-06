package com.eldar.eldarwallet.model.qr



import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject

class QRApiService {

    fun generateQRCode(nameAndLastName: String): String? {
        val client = OkHttpClient()

        val mediaType =
            "multipart/form-data; boundary=---011000010111000001101001".toMediaTypeOrNull()
        val body = RequestBody.create(mediaType, """
            -----011000010111000001101001
            Content-Disposition: form-data; name="data"
            
            $nameAndLastName
            -----011000010111000001101001
            Content-Disposition: form-data; name="width"
            
            500
            -----011000010111000001101001
            Content-Disposition: form-data; name="height"
            
            500
            -----011000010111000001101001
            Content-Disposition: form-data; name="qr_color"
            
            black
            -----011000010111000001101001
            Content-Disposition: form-data; name="transparent_background"
            
            true
            -----011000010111000001101001--
        """.trimIndent())
        val request = Request.Builder()
            .url("https://qr-code-generator160.p.rapidapi.com/custom_qr_generator")
            .post(body)
            .addHeader("x-rapidapi-key", "f80537c055msh47a1eeeb89338e3p119c36jsnc6ae7e905586")
            .addHeader("x-rapidapi-host", "qr-code-generator160.p.rapidapi.com")
            .addHeader("Content-Type", "multipart/form-data; boundary=---011000010111000001101001")
            .build()

        val response = client.newCall(request).execute()
        val responseBody = response.body?.string()

        return if (response.isSuccessful && responseBody != null) {
            val jsonResponse = JSONObject(responseBody)
            jsonResponse.getString("qr_code_link")
        } else {
            null
        }
    }
}