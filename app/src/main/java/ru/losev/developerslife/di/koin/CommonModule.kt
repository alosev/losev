package ru.losev.developerslife.di.koin

import org.koin.dsl.module
import ru.losev.developerslife.data.source.network.ApiService
import ru.losev.developerslife.data.source.network.HttpClientFactory
import ru.losev.developerslife.model.setting.api.ApiSetting
import ru.losev.developerslife.model.setting.api.ApiSettingImpl

val commonModule = module {
    single<ApiSetting> { ApiSettingImpl() }
    single { HttpClientFactory.getInstance() }
    single { ApiService.getInstance(get(), get()) }
}