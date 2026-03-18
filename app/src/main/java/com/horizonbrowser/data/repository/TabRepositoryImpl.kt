package com.horizonbrowser.data.repository

import com.horizonbrowser.data.local.database.*
import com.horizonbrowser.domain.model.Tab
import com.horizonbrowser.domain.repository.TabRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TabRepositoryImpl @Inject constructor(
    private val dao: BrowserTabDao
) : TabRepository {
    
    override fun getAllTabs(): Flow<List<Tab>> {
        return dao.getAllTabs().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getTabById(tabId: String): Tab? {
        return dao.getTabById(tabId)?.toDomain()
    }

    override suspend fun addTab(tab: Tab) {
        dao.insertTab(tab.toEntity())
    }

    override suspend fun updateTab(tab: Tab) {
        dao.updateTab(tab.toEntity())
    }

    override suspend fun removeTab(tabId: String) {
        dao.deleteTabById(tabId)
    }

    override suspend fun clearAllTabs() {
        dao.deleteAllTabs()
    }

    override fun getTabCount(): Flow<Int> {
        return dao.getTabCount()
    }

    private fun BrowserTab.toDomain() = Tab(
        tabId = tabId,
        url = url,
        title = title,
        position = position,
        isDesktopMode = isDesktopMode
    )

    private fun Tab.toEntity() = BrowserTab(
        tabId = tabId,
        url = url,
        title = title,
        position = position,
        isDesktopMode = isDesktopMode
    )
}
