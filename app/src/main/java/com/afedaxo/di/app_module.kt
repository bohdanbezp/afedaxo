package com.afedaxo.di

import androidx.room.Room
import com.afedaxo.domain.usecase.CalcDishForPeopleUseCase
import com.afedaxo.domain.usecase.DetectDishPriceUseCase
import com.afedaxo.model.repository.FilesRepository
import com.afedaxo.model.repository.SessionsRepository
import com.afedaxo.model.room.AppDatabase
import com.afedaxo.presentation.ui.chooseparams.ChooseParamsViewModel
import com.afedaxo.presentation.ui.foodlist.FoodListViewModel
import com.afedaxo.presentation.ui.main.MainViewModel
import com.afedaxo.presentation.ui.photo.PhotoTakingViewModel
import com.afedaxo.presentation.ui.result.ResultViewModel
import com.afedaxo.presentation.ui.selectregion.SelectRegionViewModel
import com.afedaxo.presentation.ui.start.StartViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val afedaxoModule = module {

    single { Room.databaseBuilder(
        get(),
        AppDatabase::class.java, "app_database"
    ).fallbackToDestructiveMigration().build() }

    single { FilesRepository(get()) }


    single { SessionsRepository(get(), get()) }

}

val viewModelModule = module {
    viewModel { ChooseParamsViewModel(get()) }

    viewModel { FoodListViewModel(get(), get()) }

    viewModel { PhotoTakingViewModel(get(), get()) }

    viewModel { MainViewModel() }

    viewModel { StartViewModel(get()) }

    viewModel { ResultViewModel(get()) }

    viewModel { SelectRegionViewModel(get(), get(), get()) }
}

val useCaseModule = module {
    factory { CalcDishForPeopleUseCase(get(), get()) }

    factory { DetectDishPriceUseCase(get()) }

}

val mvvmModule = listOf(afedaxoModule, viewModelModule, useCaseModule)