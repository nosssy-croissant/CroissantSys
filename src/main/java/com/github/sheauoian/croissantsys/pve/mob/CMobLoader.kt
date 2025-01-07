package com.github.sheauoian.croissantsys.pve.mob

import com.github.sheauoian.croissantsys.CroissantSys
import kotlinx.serialization.json.Json
import java.io.File

object CMobLoader {
    fun loadAll(): List<CMob> {
        val mobs = mutableListOf<CMob>()
        val dir = File(CroissantSys.instance.dataFolder, "mobs")
        if (!dir.exists()) {
            dir.mkdirs()
            return mobs
        }
        dir.listFiles()?.forEach {
            val json = Json {
                ignoreUnknownKeys = true
                prettyPrint = true
            }
            mobs.add(json.decodeFromString(CMob.serializer(), it.readText()))
        }
        return mobs
    }

    fun load(id: String): CMob {
        val file = File(CroissantSys.instance.dataFolder, "mobs/$id.yml")
        val json = Json {
            ignoreUnknownKeys = true
            prettyPrint = true
        }
        return json.decodeFromString(CMob.serializer(), file.readText())
    }

    fun save(mob: CMob) {
        val file = File(CroissantSys.instance.dataFolder, "mobs/${mob.mythicId}.yml")
        val json = Json {
            ignoreUnknownKeys = true
            prettyPrint = true
        }
        file.writeText(json.encodeToString(CMob.serializer(), mob))
    }
}