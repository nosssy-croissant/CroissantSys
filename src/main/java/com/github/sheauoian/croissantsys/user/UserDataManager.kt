package com.github.sheauoian.croissantsys.user

import com.github.sheauoian.croissantsys.CroissantSys
import com.github.sheauoian.croissantsys.DbDriver
import com.github.sheauoian.croissantsys.user.online.UserDataOnline
import org.bukkit.entity.Player
import java.sql.Connection
import java.util.*
import java.util.concurrent.CompletableFuture

const val DEFAULT_MONEY         = 1000
const val DEFAULT_HEALTH        = 10.0
const val DEFAULT_MAX_HEALTH    = 10.0

class UserDataManager(con: Connection) {
    companion object {
        val instance = UserDataManager(DbDriver.con)
    }

    private val cache = UserDataCache()
    private val repository = UserDataRepository(con)

    fun saveAll() {
        cache.getAll().forEach { it.save() }
        CroissantSys.instance.logger.info("Saved ${cache.getAll().size} users!")
    }

    fun saveAsync(userData: UserData): CompletableFuture<Unit> {
        return CompletableFuture.supplyAsync {
            save(userData)
        }
    }

    fun save(userData: UserData) {
        repository.save(userData)
        println("Saved: ${userData.uuid}")
        cache.put(userData)
    }

    fun join(player: Player): UserDataOnline {
        cache.get(player.uniqueId)?.let { save(it) }
        val user = repository.load(player)
        return user.let { cache.put(it) }
    }

    fun quit(player: Player) {
        val uuid = player.uniqueId
        get(player).save()
        cache.remove(uuid)
    }

    fun get(uuid: UUID): UserData? {
        return cache.get(uuid) ?: repository.load(uuid)?.let { cache.put(it) }
    }

    fun get(player: Player): UserDataOnline {
        return (cache.get(player.uniqueId) as? UserDataOnline)
            ?: repository.load(player).let { cache.put(it) }
    }

    fun getAll(): List<UserData> {
        return cache.getAll()
    }
}