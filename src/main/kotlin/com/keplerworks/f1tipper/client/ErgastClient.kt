package com.keplerworks.f1tipper.client

import com.keplerworks.f1tipper.model.ergast.ErgastRaceResult
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import java.util.concurrent.CompletableFuture

interface ErgastClient {

    @GET("{round}/results.json")
    @Headers("accept: application/json")
    fun getRaceResult(@Path("round") round: Int): CompletableFuture<ErgastRaceResult>

    companion object {
        private const val YEAR = 2021
        private const val BASE_URL = "http://ergast.com/api/f1/${YEAR}/"

        fun create(): ErgastClient {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(ErgastClient::class.java)
        }
    }
}