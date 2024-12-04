package com.github.sheauoian.croissantsys.pve.skill

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage

enum class SkillTag(val displayName: Component) {
    SoftLanding("ソフトランディング");

    constructor(displayName: String) : this(MiniMessage.miniMessage().deserialize(displayName))
}