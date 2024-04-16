package roctik.app.health

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import roctik.app.health.di.repositoryModule
import roctik.app.health.di.singleModule
import roctik.app.health.di.useCaseModule
import roctik.app.health.di.viewModelModule

class HealthApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@HealthApp)
            modules(
                singleModule,
                repositoryModule,
                useCaseModule,
                viewModelModule
            )
        }
    }
}