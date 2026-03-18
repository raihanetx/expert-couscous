package com.horizonbrowser.data.repository

import com.horizonbrowser.data.local.database.*
import com.horizonbrowser.domain.model.Favorite
import com.horizonbrowser.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoritesRepositoryImpl @Inject constructor(
    private val dao: FavoriteSiteDao
) : FavoritesRepository {
    
    override fun getAllFavorites(): Flow<List<Favorite>> {
        return dao.getAllFavorites().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun addToFavorites(favorite: Favorite) {
        dao.insertFavorite(favorite.toEntity())
    }

    override suspend fun removeFromFavorites(url: String) {
        dao.deleteFavoriteByUrl(url)
    }

    override suspend fun isFavorite(url: String): Boolean {
        return dao.isFavorite(url)
    }

    private fun FavoriteSite.toDomain() = Favorite(
        url = url,
        title = title,
        position = position
    )

    private fun Favorite.toEntity() = FavoriteSite(
        url = url,
        title = title,
        position = position
    )
}
