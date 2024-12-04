package com.github.sheauoian.croissantsys.pve.skill

import com.github.sheauoian.croissantsys.user.online.UserDataOnline
import com.github.sheauoian.croissantsys.util.Formula
import com.github.sheauoian.croissantsys.util.ItemBuilder
import org.bukkit.Material

class NormalSkill(
    internal val skillName: String,
    private val variables: Map<String, Formula>,
    private val material: Material,
    private val description: List<Any>,
    override val function: (UserDataOnline) -> Unit
) : ActiveSkillHolder(function) {
    companion object {
        val skills = mapOf(
            "test" to NormalSkill(
                "test",
                mapOf(),
                Material.STONE,
                listOf("test")
            ) {
                it.player.sendMessage("test")
            }
        )
    }


    val iconBuilder = ItemBuilder(material).displayName(skillName)
    fun use(user: UserDataOnline) { function(user) }
    fun getDescriptionString(user: UserDataOnline): String {
        val result = StringBuilder()
        for (element in description) {
            when (element) {
                is String -> result.append(element)
                is Formula -> result.append(element.calculate(user).toString())
            }
        }
        return result.toString()
    }
    fun getDescriptionString(): String {
        return description.joinToString(" ")
    }
}