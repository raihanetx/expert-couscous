package com.horizonbrowser.data.repository

import com.horizonbrowser.data.local.database.*
import com.horizonbrowser.domain.model.ZoomSetting
import com.horizonbrowser.domain.repository.ZoomRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ZoomRepositoryImpl @Inject constructor(
    private val dao: ZoomPreferenceDao
) : ZoomRepository {
    
    override suspend fun getZoomForDomain(domain: String): Int {
        return dao.getZoomForDomain(domain)?.zoomLevel ?: 100
    }

    override suspend fun setZoomForDomain(domain: String, zoomLevel: Int) {
        dao.setZoomForDomain(ZoomPreference(domain, zoomLevel.coerceIn(50, 150)))
    }

    override fun getAllZoomPreferences(): Flow<List<ZoomSetting>> {
        return dao.getAllZoomPreferences().map { entities ->
            entities.map { ZoomSetting(it.domain, it.zoomLevel) }
        }
    }
}
