package ru.mikhailskiy.livedata.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.mikhailskiy.livedata.network.logger.CustomHttpLogging

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
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return@lazy retrofit.create(MovieApiInterface::class.java)
    }
}