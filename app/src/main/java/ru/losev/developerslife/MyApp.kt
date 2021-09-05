package ru.losev.developerslife

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.losev.developerslife.di.koin.*

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MyApp)
            modules(
                commonModule,
                postCategoryModule
            )
        }
    }
}