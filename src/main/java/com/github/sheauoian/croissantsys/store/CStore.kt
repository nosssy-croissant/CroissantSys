package com.github.sheauoian.croissantsys.store

import com.github.sheauoian.croissantsys.store.product.CProductHolder
import com.github.sheauoian.croissantsys.user.online.UserDataOnline
import com.github.sheauoian.croissantsys.util.Formula
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.minimessage.MiniMessage


@Serializable
@SerialName("CStore")
class CStore(val id: String, val name: String, val products: MutableList<CProductHolder> = mutableListOf()) {
    companion object {
        private val MESSAGE_PREFIX =
            MiniMessage.miniMessage().deserialize("<color:#8899bb>[<gradient:#ccaa77:#aacc77> ショップ ]</color>")

        fun sendError(user: UserDataOnline, message: String) {
            sendMessage(user, "<red>$message")
        }

        fun sendMessage(user: UserDataOnline, message: String) {
            sendMessage(user, MiniMessage.miniMessage().deserialize(message))
        }

        fun sendMessage(user: UserDataOnline, message: Component) {
            user.player.sendMessage(MESSAGE_PREFIX.append(message))
        }

        fun sendMessage(user: UserDataOnline, elements: List<Any>) {
            val message = Component.empty()
            // elements をループさせ、messageに追加していく
            elements.forEach { element ->
                when (element) {
                    is String -> message.append(MiniMessage.miniMessage().deserialize(element))
                    is Formula -> message.append(Component.text(element.calculate(user)))
                    is TextComponent -> message.append(element)
                }
            }
            sendMessage(user, message)
        }
    }

    fun addProduct(product: CProductHolder) {
        products.add(product)
    }

    fun open(user: UserDataOnline) {
        val gui = CStoreGui(user, this)
        gui.show(user.player)
    }
}