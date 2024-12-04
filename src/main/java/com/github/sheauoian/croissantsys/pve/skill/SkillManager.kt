package com.github.sheauoian.croissantsys.pve.skill

class SkillManager {
    companion object {
        val instance = SkillManager()
    }
    private val skills: MutableMap<String, Skill> = mutableMapOf()

    fun register(skill: Skill) {
        skills[skill.name] = skill
    }

    fun getSkill(name: String): Skill? {
        return skills[name]
    }
}