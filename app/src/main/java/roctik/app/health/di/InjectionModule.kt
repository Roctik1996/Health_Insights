package roctik.app.health.di

import androidx.health.connect.client.HealthConnectClient
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module
import roctik.app.health.data.repository.HealthDataRepositoryImpl
import roctik.app.health.domain.repository.HealthDataRepository
import roctik.app.health.domain.usecase.GetAggregateDataUseCase
import roctik.app.health.ui.main.MainViewModel

val singleModule = module {
    single { PreferencesImpl(androidApplication()) } bind Preferences::class
    single { HealthConnectClient.getOrCreate(androidContext()) } bind HealthConnectClient::class
}

val repositoryModule = module {
    single { HealthDataRepositoryImpl(get()) } bind HealthDataRepository::class
}

val useCaseModule = module {
    single { GetAggregateDataUseCase(get(), get()) }
}

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
}

