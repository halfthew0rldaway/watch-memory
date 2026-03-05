package com.example.watchmemory.data

import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// --- API Response Models ---

data class ImdbSearchResponse(
    @SerializedName("description") val results: List<ImdbSearchResult>? = null,
    @SerializedName("ok") val ok: Boolean = false
)

data class ImdbSearchResult(
    @SerializedName("#IMDB_ID") val imdbId: String = "",
    @SerializedName("#TITLE") val title: String = "",
    @SerializedName("#TYPE") val type: String = "",
    @SerializedName("#YEAR") val year: String = "",
    @SerializedName("#AKA") val aka: String = "",
    @SerializedName("#ACTORS") val actors: String = "",
    @SerializedName("#IMG_POSTER") val posterUrl: String = "",
    @SerializedName("photo_width") val photoWidth: Int = 0,
    @SerializedName("photo_height") val photoHeight: Int = 0
)

// --- Retrofit Service ---

interface ImdbApiService {
    @GET("search")
    suspend fun search(
        @Query("q") query: String
    ): ImdbSearchResponse
}

// --- Singleton ---

object ImdbApiClient {
    private const val BASE_URL = "https://imdb.iamidiotareyoutoo.com/"

    val service: ImdbApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ImdbApiService::class.java)
    }
}
