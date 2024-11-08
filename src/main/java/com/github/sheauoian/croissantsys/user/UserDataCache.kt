package com.github.sheauoian.croissantsys.user

import com.github.sheauoian.croissantsys.user.online.UserDataOnline
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class UserDataCache {
    private val datum: ConcurrentHashMap<UUID, UserData> = ConcurrentHashMap()
    private val executor: ScheduledExecutorService = Executors.newScheduledThreadPool(1)
    private val maxCacheSize = 1000
    init {
        executor.scheduleAtFixedRate({ clearExpiredCache() }, 10, 10, TimeUnit.MINUTES)
    }
    private fun clearExpiredCache() {
        val now = System.currentTimeMillis()
        datum.entries.removeIf {
            it.value.lastAccessed + TimeUnit.HOURS.toMillis(1) < now
        }
    }

    fun put(data: UserData): UserData {
        data.access()
        if (datum.size >= maxCacheSize) {
            val oldestEntry = datum.entries.minByOrNull { it.value.lastAccessed }
            oldestEntry?.let { datum.remove(it.key) }
        }
        datum[data.uuid] = data
        return data
    }

    fun put(data: UserDataOnline): UserDataOnline {
        return put(data as UserData) as UserDataOnline
    }

    fun get(uuid: UUID): UserData? {
        return datum[uuid]?.apply { access() }
    }

    fun remove(uuid: UUID) {
        datum.remove(uuid)
    }

    fun getAll(): List<UserData> {
        return datum.values.toList()
    }
}