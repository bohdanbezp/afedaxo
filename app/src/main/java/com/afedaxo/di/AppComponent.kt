package com.afedaxo.di

import com.afedaxo.presentation.presenter.*
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {
    fun inject(afedaxoApp: PhotoTakingPresenter)
    fun inject(afedaxoApp: SelectRegionPresenter)
    fun inject(mainPresenter: MainPresenter)
    fun inject(foodListPresenter: FoodListPresenter)
    fun inject(chooseParamsPresenter: ChooseParamsPresenter)
    fun inject(resultPresenter: ResultPresenter)

}