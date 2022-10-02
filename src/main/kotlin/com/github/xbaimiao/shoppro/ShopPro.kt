package com.github.xbaimiao.shoppro

import com.github.xbaimiao.shoppro.core.database.Database
import com.github.xbaimiao.shoppro.core.database.MysqlDatabase
import com.github.xbaimiao.shoppro.core.database.SQLiteDatabase
import com.github.xbaimiao.shoppro.core.item.*
import com.github.xbaimiao.shoppro.core.shop.ShopManager
import com.github.xbaimiao.shoppro.core.vault.DiyCurrency
import taboolib.common.platform.Plugin
import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration

object ShopPro : Plugin() {

    @Config(value = "config.yml")
    lateinit var config: Configuration
        private set

    lateinit var database: Database

    val itemLoaderManager = ItemLoaderManager()

    override fun onEnable() {
        ShopManager.load()
        DiyCurrency.load()
        database = if (config.getBoolean("mysql.enable")) MysqlDatabase(config) else SQLiteDatabase()
    }

    fun reload() {
        config.reload()
        ShopManager.shops.clear()
        ShopManager.load()
        DiyCurrency.load()
        database = if (config.getBoolean("mysql.enable")) MysqlDatabase(config) else SQLiteDatabase()
    }

}