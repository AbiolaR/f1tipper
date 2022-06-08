package com.keplerworks.f1tipper.client

import com.keplerworks.f1tipper.model.rapid.f1live.RapidF1LiveConstructorStandingsResult
import com.keplerworks.f1tipper.model.rapid.f1live.RapidF1LiveDriverStandingsResult
import com.keplerworks.f1tipper.model.rapid.f1live.RapidF1LiveRacesResult
import com.keplerworks.f1tipper.model.rapid.f1live.RapidF1LiveSessionResult
import org.springframework.stereotype.Service
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path
import java.util.concurrent.CompletableFuture

@Service
interface RapidF1LiveClient {



    @GET("races/${YEAR}")
    @Headers("x-rapidapi-host: $apiHost")
    fun getAllRaces(@Header("x-rapidapi-key") apiKey: String): CompletableFuture<RapidF1LiveRacesResult>

    @GET("session/{sessionId}")
    @Headers("x-rapidapi-host: $apiHost")
    fun getSession(@Path("sessionId") sessionId: Long, @Header("x-rapidapi-key") apiKey: String): CompletableFuture<RapidF1LiveSessionResult>

    @GET("drivers/standings/$YEAR")
    @Headers("x-rapidapi-host: $apiHost")
    fun getDriverStandings(@Header("x-rapidapi-key") apiKey: String): CompletableFuture<RapidF1LiveDriverStandingsResult>

    @GET("constructors/standings/$YEAR")
    @Headers("x-rapidapi-host: $apiHost")
    fun getConstructorStandings(@Header("x-rapidapi-key") apiKey: String): CompletableFuture<RapidF1LiveConstructorStandingsResult>

    companion object {
        private const val apiHost = "f1-live-motorsport-data.p.rapidapi.com"
        private const val YEAR = 2022
        private const val BASE_URL = "https://f1-live-motorsport-data.p.rapidapi.com/"

        fun create(): RapidF1LiveClient {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(RapidF1LiveClient::class.java)
        }
    }
}