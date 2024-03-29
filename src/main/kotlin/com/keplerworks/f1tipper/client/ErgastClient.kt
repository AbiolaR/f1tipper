package com.keplerworks.f1tipper.client

import com.keplerworks.f1tipper.model.ergast.ErgastEventResponse
import com.keplerworks.f1tipper.model.ergast.ErgastStandingsResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import java.util.concurrent.CompletableFuture

interface ErgastClient {

    @GET("{round}/results.json")
    @Headers("accept: application/json")
    fun getRaceResult(@Path("round") round: Int): CompletableFuture<ErgastEventResponse>

    @GET("{round}/sprint.json")
    @Headers("accept: application/json")
    fun getSprintRaceResult(@Path("round") round: Int): CompletableFuture<ErgastEventResponse>

    @GET("{round}/qualifying.json")
    @Headers("accept: application/json")
    fun getQualifyingResult(@Path("round") round: Int): CompletableFuture<ErgastEventResponse>

    @GET("driverStandings.json")
    @Headers("accept: application/json")
    fun getDriverStandings(): CompletableFuture<ErgastStandingsResponse>

    @GET("constructorStandings.json")
    @Headers("accept: application/json")
    fun getConstructorStandings(): CompletableFuture<ErgastStandingsResponse>

    companion object {
        private const val YEAR = 2022
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