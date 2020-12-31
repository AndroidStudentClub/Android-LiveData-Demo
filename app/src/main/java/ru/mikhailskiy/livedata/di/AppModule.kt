package ru.mikhailskiy.livedata.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.mikhailskiy.livedata.DemoApp
import ru.mikhailskiy.livedata.data.database.MovieDao
import ru.mikhailskiy.livedata.data.database.MovieDatabase
import ru.mikhailskiy.livedata.data.network.MovieApiClient
import ru.mikhailskiy.livedata.data.network.MovieApiClient.BASE_URL
import ru.mikhailskiy.livedata.data.network.MovieApiInterface
import ru.mikhailskiy.livedata.data.repository.FakeRemoteRepository
import ru.mikhailskiy.livedata.data.repository.MovieLocalRepository
import ru.mikhailskiy.livedata.domain.usecase.MovieInterface
import javax.inject.Named
import javax.inject.Singleton

@Module
class AppModule(val appContext: DemoApp) {


    @Provides
    @Singleton
    fun provideApp() = appContext

    @Provides
    @Singleton
    fun provideClient() =
        OkHttpClient.Builder()
            .addInterceptor(Interceptor { chain: Interceptor.Chain ->
                val request = chain.request()
                    .newBuilder()
                    .build()
                chain.proceed(request)
            })
            .addInterceptor(MovieApiClient.interceptor)
            .build()

    @Singleton
    @Provides
    fun provideGithubService(client: OkHttpClient): MovieApiInterface {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(MovieApiInterface::class.java)
    }

    @Singleton
    @Provides
    fun provideDb(app: DemoApp): MovieDatabase {
        return MovieDatabase.get(app)
    }

    @Singleton
    @Provides
    fun provideUserDao(db: MovieDatabase): MovieDao {
        return db.movieDao()
    }


    @Singleton
    @Named("local")
    @Provides
    fun provideLocalRepository(dao: MovieDao): MovieInterface {
        return MovieLocalRepository(dao)
    }

    @Singleton
    @Named("fake")
    @Provides
    fun provideFakeRepository(api: MovieApiInterface): MovieInterface {
        return FakeRemoteRepository(api)
    }


}