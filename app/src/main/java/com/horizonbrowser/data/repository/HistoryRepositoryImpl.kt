package com.horizonbrowser.data.repository

import com.horizonbrowser.data.local.database.*
import com.horizonbrowser.domain.model.HistoryItem
import com.horizonbrowser.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HistoryRepositoryImpl @Inject constructor(
    private val dao: BrowsingHistoryDao
) : HistoryRepository {
    
    override fun getAllHistory(): Flow<List<HistoryItem>> {
        return dao.getAllHistory().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun searchHistory(query: String): Flow<List<HistoryItem>> {
        return dao.searchHistory(query).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun addToHistory(item: HistoryItem) {
        dao.insertHistory(item.toEntity())
    }

    override suspend fun removeFromHistory(id: Long) {
        dao.deleteHistoryById(id)
    }

    override suspend fun clearHistory() {
        dao.deleteAllHistory()
    }

    private fun BrowsingHistory.toDomain() = HistoryItem(
        id = id,
        url = url,
        title = title,
        visitedAt = visitedAt
    )

    private fun HistoryItem.toEntity() = BrowsingHistory(
        id = id,
        url = url,
        title = title,
        visitedAt = visitedAt
    )
}
