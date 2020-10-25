package ru.mikhailskiy.livedata.di

import dagger.Component
import ru.mikhailskiy.livedata.DemoApp
import ru.mikhailskiy.livedata.MainActivity
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, ViewModelModule::class])
interface AppComponent {

    fun inject(app: DemoApp)
    fun inject(mainActivity: MainActivity)
}