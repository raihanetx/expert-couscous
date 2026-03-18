package com.horizonbrowser.di

import android.content.Context
import androidx.room.Room
import com.horizonbrowser.data.local.database.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): HorizonDatabase {
        return Room.databaseBuilder(
            context,
            HorizonDatabase::class.java,
            "horizon_browser_db"
        ).build()
    }

    @Provides
    fun provideBrowserTabDao(database: HorizonDatabase): BrowserTabDao {
        return database.browserTabDao()
    }

    @Provides
    fun provideBrowsingHistoryDao(database: HorizonDatabase): BrowsingHistoryDao {
        return database.browsingHistoryDao()
    }

    @Provides
    fun provideFavoriteSiteDao(database: HorizonDatabase): FavoriteSiteDao {
        return database.favoriteSiteDao()
    }

    @Provides
    fun provideZoomPreferenceDao(database: HorizonDatabase): ZoomPreferenceDao {
        return database.zoomPreferenceDao()
    }

    @Provides
    fun provideCookieBlockedDomainDao(database: HorizonDatabase): CookieBlockedDomainDao {
        return database.cookieBlockedDomainDao()
    }
}
