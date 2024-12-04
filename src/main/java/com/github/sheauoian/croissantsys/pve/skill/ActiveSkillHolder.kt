package com.github.sheauoian.croissantsys.pve.skill

import com.github.sheauoian.croissantsys.user.online.UserDataOnline

abstract class ActiveSkillHolder(open val function: (UserDataOnline) -> Unit)