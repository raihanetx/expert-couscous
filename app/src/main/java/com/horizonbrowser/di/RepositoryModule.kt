package com.horizonbrowser.di

import com.horizonbrowser.data.repository.*
import com.horizonbrowser.domain.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindTabRepository(impl: TabRepositoryImpl): TabRepository

    @Binds
    @Singleton
    abstract fun bindHistoryRepository(impl: HistoryRepositoryImpl): HistoryRepository

    @Binds
    @Singleton
    abstract fun bindFavoritesRepository(impl: FavoritesRepositoryImpl): FavoritesRepository

    @Binds
    @Singleton
    abstract fun bindZoomRepository(impl: ZoomRepositoryImpl): ZoomRepository

    @Binds
    @Singleton
    abstract fun bindCookieRepository(impl: CookieRepositoryImpl): CookieRepository

    @Binds
    @Singleton
    abstract fun bindPreferencesRepository(impl: PreferencesRepositoryImpl): PreferencesRepository
}
