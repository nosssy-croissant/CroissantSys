package com.github.sheauoian.croissantsys.util

import com.github.sheauoian.croissantsys.user.online.UserDataOnline

class Formula(private val formula: String, private val function: (UserDataOnline) -> Double) {
    fun calculate(user: UserDataOnline): Double {
        return function(user)
    }
    override fun toString(): String {
        return formula
    }
}