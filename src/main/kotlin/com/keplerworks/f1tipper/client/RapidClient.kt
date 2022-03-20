package com.keplerworks.f1tipper.client

import com.keplerworks.f1tipper.model.rapid.RapidRaces
import com.keplerworks.f1tipper.model.rapid.RapidResult
import org.springframework.stereotype.Service
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path
import java.util.concurrent.CompletableFuture

@Service
interface RapidClient {



    @GET("races/${YEAR}")
    @Headers("x-rapidapi-host: $apiHost")
    fun getAllRaces(@Header("x-rapidapi-key") apiKey: String): CompletableFuture<RapidRaces>


    @GET("session/{sessionId}")
    @Headers("x-rapidapi-host: $apiHost")
    fun getSession(@Path("sessionId") sessionId: Long, @Header("x-rapidapi-key") apiKey: String): CompletableFuture<RapidResult>

    companion object {
        private const val apiHost = "f1-live-motorsport-data.p.rapidapi.com"
        private const val YEAR = 2022
        private const val BASE_URL = "https://f1-live-motorsport-data.p.rapidapi.com/"

        fun create(): RapidClient {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(RapidClient::class.java)
        }
    }
}