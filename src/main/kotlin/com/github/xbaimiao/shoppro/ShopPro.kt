package com.github.xbaimiao.shoppro

import com.github.xbaimiao.shoppro.core.database.Database
import com.github.xbaimiao.shoppro.core.database.MysqlDatabase
import com.github.xbaimiao.shoppro.core.database.SQLiteDatabase
import com.github.xbaimiao.shoppro.core.item.ItemLoaderManager
import com.github.xbaimiao.shoppro.core.shop.ShopManager
import com.github.xbaimiao.shoppro.core.vault.DiyCurrency
import com.xbaimiao.ktor.KtorPluginsBukkit
import com.xbaimiao.ktor.KtorStat
import taboolib.common.platform.Plugin
import taboolib.common.platform.function.info
import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration
import taboolib.platform.BukkitPlugin

object ShopPro : Plugin(), KtorStat {

    @Config(value = "config.yml")
    lateinit var config: Configuration
        private set

    lateinit var database: Database

    val itemLoaderManager = ItemLoaderManager()

    override fun onActive() {
        KtorPluginsBukkit.init(BukkitPlugin.getInstance(), this)
        info("$userId 感谢您的使用")
        stat()
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