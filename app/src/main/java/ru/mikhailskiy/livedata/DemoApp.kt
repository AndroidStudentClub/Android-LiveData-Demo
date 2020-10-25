package ru.mikhailskiy.livedata

import android.app.Activity
import android.app.Application
import ru.mikhailskiy.livedata.di.AppComponent
import ru.mikhailskiy.livedata.di.AppModule
import javax.inject.Inject
import ru.mikhailskiy.livedata.data.network.MovieApiClient.BASE_URL
import ru.mikhailskiy.livedata.di.DaggerAppComponent

class DemoApp : Application() {

    val component: AppComponent by lazy {
        DaggerAppComponent
            .builder()
            .appModule(AppModule(this))
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        component.inject(this)
    }
}