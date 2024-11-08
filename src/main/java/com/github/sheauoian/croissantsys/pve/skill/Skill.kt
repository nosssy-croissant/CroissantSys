package com.github.sheauoian.croissantsys.pve.skill

import com.github.sheauoian.croissantsys.user.online.UserDataOnline
import com.github.sheauoian.croissantsys.util.Formula

class Skill(
    private val description: List<Any>,
    private val variables: Map<String, Double>,
    val function: (UserDataOnline) -> Unit
) {
    fun use(user: UserDataOnline) {
        function(user)
    }

    /**
     * Constructs a string representation of the skill's description.
     * The description list can contain both String and Formula objects.
     * For each element in the list:
     * - If it is a String, append it directly to the result.
     * - If it is a Formula, calculate the formula using the provided user data and append the result.
     *
     * @param user The user data used to calculate formulas.
     * @return A string representation of the skill's description.
     */
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

    /**
     * Returns a string representation of the skill's description.
     * Joins all elements in the description list with a space.
     *
     * @return A string representation of the skill's description.
     */
    override fun toString(): String {
        return description.joinToString(" ")
    }
}