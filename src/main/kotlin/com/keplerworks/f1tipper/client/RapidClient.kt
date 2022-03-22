package com.keplerworks.f1tipper.client

import com.keplerworks.f1tipper.model.rapid.RapidConstructorStandingsResult
import com.keplerworks.f1tipper.model.rapid.RapidDriverStandingsResult
import com.keplerworks.f1tipper.model.rapid.RapidRacesResult
import com.keplerworks.f1tipper.model.rapid.RapidSessionResult
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
    fun getAllRaces(@Header("x-rapidapi-key") apiKey: String): CompletableFuture<RapidRacesResult>

    @GET("session/{sessionId}")
    @Headers("x-rapidapi-host: $apiHost")
    fun getSession(@Path("sessionId") sessionId: Long, @Header("x-rapidapi-key") apiKey: String): CompletableFuture<RapidSessionResult>

    @GET("drivers/standings/$YEAR")
    @Headers("x-rapidapi-host: $apiHost")
    fun getDriverStandings(@Header("x-rapidapi-key") apiKey: String): CompletableFuture<RapidDriverStandingsResult>

    @GET("constructors/standings/$YEAR")
    @Headers("x-rapidapi-host: $apiHost")
    fun getConstructorStandings(@Header("x-rapidapi-key") apiKey: String): CompletableFuture<RapidConstructorStandingsResult>

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