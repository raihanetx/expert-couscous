package com.horizonbrowser.data.repository

import com.horizonbrowser.data.local.database.*
import com.horizonbrowser.data.local.preferences.UserPreferencesManager
import com.horizonbrowser.domain.model.BlockedDomain
import com.horizonbrowser.domain.model.CookieMode
import com.horizonbrowser.domain.repository.CookieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CookieRepositoryImpl @Inject constructor(
    private val preferencesManager: UserPreferencesManager,
    private val dao: CookieBlockedDomainDao
) : CookieRepository {
    
    override fun getCookieMode(): Flow<CookieMode> {
        return preferencesManager.cookieMode.map { mode ->
            when (mode) {
                0 -> CookieMode.ALLOW_ALL
                1 -> CookieMode.BLOCK_THIRD_PARTY
                2 -> CookieMode.BLOCK_ALL
                else -> CookieMode.ALLOW_ALL
            }
        }
    }

    override suspend fun setCookieMode(mode: CookieMode) {
        val modeInt = when (mode) {
            CookieMode.ALLOW_ALL -> 0
            CookieMode.BLOCK_THIRD_PARTY -> 1
            CookieMode.BLOCK_ALL -> 2
        }
        preferencesManager.setCookieMode(modeInt)
    }

    override fun getBlockedDomains(): Flow<List<BlockedDomain>> {
        return dao.getAllBlockedDomains().map { entities ->
            entities.map { BlockedDomain(it.domain) }
        }
    }

    override suspend fun blockDomain(domain: String) {
        dao.blockDomain(CookieBlockedDomain(domain = domain))
    }

    override suspend fun unblockDomain(domain: String) {
        dao.unblockDomain(domain)
    }

    override suspend fun isDomainBlocked(domain: String): Boolean {
        return dao.isDomainBlocked(domain)
    }

    override suspend fun exportBlockedDomains(): String {
        val domains = dao.getAllBlockedDomains().map { it.toList() }.first()
        val jsonArray = JSONArray()
        domains.forEach { jsonArray.put(it.domain) }
        return jsonArray.toString(2)
    }

    private suspend fun <T> Flow<T>.first(): T {
        var result: T? = null
        collect { 
            result = it
            return@collect
        }
        @Suppress("UNCHECKED_CAST")
        return result as T
    }

    private fun <T> Flow<List<T>>.toList(): Flow<List<T>> = this
}
