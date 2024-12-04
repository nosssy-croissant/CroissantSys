package com.github.sheauoian.croissantsys.user

import com.github.sheauoian.croissantsys.user.online.UserDataOnline
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class UserDataCache {
    // ユーザーデータのキャッシュ
    private val datum: ConcurrentHashMap<UUID, UserData> = ConcurrentHashMap()

    // キャッシュの最大サイズ
    private val executor: ScheduledExecutorService = Executors.newScheduledThreadPool(1)

    // キャッシュの有効期限 : 1時間
    private val maxCacheSize = 1000
    init {
        // キャッシュの有効期限を監視する
        executor.scheduleAtFixedRate({ clearExpiredCache() }, 10, 10, TimeUnit.MINUTES)
    }

    // キャッシュの有効期限を監視する
    private fun clearExpiredCache() {
        val now = System.currentTimeMillis()
        datum.entries.removeIf {
            it.value.lastAccessed + TimeUnit.HOURS.toMillis(1) < now
        }
    }


    // ユーザーデータをキャッシュに追加する
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

    // キャッシュからユーザーデータを取得する
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