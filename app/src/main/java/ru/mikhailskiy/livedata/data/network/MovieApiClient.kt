package ru.mikhailskiy.livedata.data.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.mikhailskiy.livedata.data.network.logger.CustomHttpLogging

object MovieApiClient {

    const val BASE_URL = "https://api.themoviedb.org/3/"

    var interceptor =
        HttpLoggingInterceptor(CustomHttpLogging()).apply { setLevel(HttpLoggingInterceptor.Level.BODY) }

    var client = OkHttpClient.Builder()
        .addInterceptor(Interceptor { chain: Interceptor.Chain ->
            val request = chain.request()
                .newBuilder()
                .build()
            chain.proceed(request)
        })
        .addInterceptor(interceptor)
        .build()

    val apiClient: MovieApiInterface by lazy {

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return@lazy retrofit.create(MovieApiInterface::class.java)
    }
}