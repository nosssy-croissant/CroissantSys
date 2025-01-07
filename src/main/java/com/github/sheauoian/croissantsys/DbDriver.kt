package com.github.sheauoian.croissantsys

import org.sqlite.SQLiteConfig
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

object DbDriver {
    var con: Connection
    init {
        try {
            val config = SQLiteConfig()
            config.setDateStringFormat("yyyy-MM-dd HH:mm:ss")
            con = DriverManager.getConnection("jdbc:sqlite:database.db", config.toProperties())
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
    fun close() {
        try {
            con.close()
        } catch (e: SQLException) {
            throw RuntimeException(e)
        }
    }
}