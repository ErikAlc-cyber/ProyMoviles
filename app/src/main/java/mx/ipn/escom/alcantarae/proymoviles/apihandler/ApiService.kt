package mx.ipn.escom.alcantarae.proymoviles.apihandler

// Archivo: ApiService.kt

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiService {
    private const val BASE_URL = "http://10.0.2.2:8085/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiServiceInterface = retrofit.create(ApiServiceInterface::class.java)
}
