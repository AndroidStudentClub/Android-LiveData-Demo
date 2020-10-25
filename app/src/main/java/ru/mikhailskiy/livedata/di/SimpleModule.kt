package ru.mikhailskiy.livedata.di

import dagger.Component
import dagger.Module
import dagger.Provides
import ru.mikhailskiy.livedata.MainActivity

//// Компонент мост между Модулем и тем куда нужно инжектить
//@Component(modules = [CarModule::class])
//interface CarComponent {
//    fun inject(activity: MainActivity)
//}
//
//
//// Создаём зависимости здесь
//@Module
//open class CarModule {
//
//    // Метод, описывающий логику того, как нужно создать ту или иную зависимость
//    @Provides
//    fun provideCar(engine: Engine) = Car(engine)
//}