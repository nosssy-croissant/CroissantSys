package com.github.sheauoian.croissantsys.store

import com.github.sheauoian.croissantsys.store.price.ItemPriceType
import com.github.sheauoian.croissantsys.store.price.LevelPriceType
import com.github.sheauoian.croissantsys.store.price.MoneyPriceType
import com.github.sheauoian.croissantsys.store.price.PriceType
import com.github.sheauoian.croissantsys.store.product.type.CProductType
import com.github.sheauoian.croissantsys.store.product.type.BoolFlagCProductType
import com.github.sheauoian.croissantsys.store.product.type.ItemCProductType
import com.github.sheauoian.croissantsys.store.product.type.WearingCProductType
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import java.io.File
import kotlinx.serialization.modules.subclass
import kotlinx.serialization.modules.polymorphic

class CStoreManager {
    companion object {
        val instance = CStoreManager()
    }

    val stores = mutableListOf<CStore>()

    fun reload() {
        stores.clear()

        val storesFolder = File("plugins/CroissantSys/stores")
        if (storesFolder.exists() && storesFolder.isDirectory) {
            storesFolder.listFiles { file -> file.extension == "yml" }?.forEach { file ->
                val store = loadStoreFromFile(file)
                stores.add(store)
            }
        }
    }

    fun save() {
        val storesFolder = File("plugins/CroissantSys/stores")
        if (!storesFolder.exists()) {
            storesFolder.mkdirs()
        }
        stores.forEach { store ->
            saveStoreToFile(store)
        }
    }
     val module = SerializersModule {
         polymorphic(CProductType::class) {
             subclass(WearingCProductType.serializer())
             subclass(ItemCProductType.serializer())
             subclass(BoolFlagCProductType.serializer())
         }
         polymorphic(PriceType::class) {
             subclass(ItemPriceType.serializer())
             subclass(LevelPriceType.serializer())
             subclass(MoneyPriceType.serializer())
         }
     }

    fun loadStoreFromFile(file: File): CStore {
        val config = Json {
            ignoreUnknownKeys = true
            serializersModule = module
            prettyPrint = true
        }
        val store: CStore = config.decodeFromString<CStore>(file.readText())
        return store
    }

    fun saveStoreToFile(store: CStore) {
        val file = File("plugins/CroissantSys/stores", "${store.id}.yml")
        val config = Json {
            ignoreUnknownKeys = true
            serializersModule = module
            // json を整列
            prettyPrint = true
        }
        file.writeText(config.encodeToString<CStore>(CStore.serializer(), store))
    }

    fun addStore(id: String, name: String): CStore {
        val store = CStore(id, name)
        stores.add(store)
        return store
    }

    fun getStore(id: String): CStore? {
        return stores.find { it.id == id }
    }
}