package com.github.xbaimiao.shoppro

import com.github.xbaimiao.shoppro.core.database.Database
import com.github.xbaimiao.shoppro.core.database.MysqlDatabase
import com.github.xbaimiao.shoppro.core.database.SQLiteDatabase
import com.github.xbaimiao.shoppro.core.item.ItemLoaderManager
import com.github.xbaimiao.shoppro.core.shop.ShopManager
import com.github.xbaimiao.shoppro.core.vault.DiyCurrency
import com.github.xbaimiao.shoppro.core.vault.VaultImpl
import com.xbaimiao.ktor.KtorPluginsBukkit
import com.xbaimiao.ktor.KtorStat
import org.bukkit.Bukkit
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import taboolib.common.platform.Plugin
import taboolib.common.platform.event.SubscribeEvent
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
        val userId = kotlin.runCatching { userId }.getOrNull()
        if (userId != null) {
            KtorPluginsBukkit.init(BukkitPlugin.getInstance(), this)
            info("$userId 感谢您的使用")
            stat()
        }
        DiyCurrency.load()
        ShopManager.load()
        VaultImpl.startTask()

        database = if (config.getBoolean("mysql.enable")) MysqlDatabase(config) else SQLiteDatabase()

        Bukkit.getOnlinePlayers().forEach {
            database.loadPlayerData(it)
        }
    }

    fun reload() {
        config.reload()
        ShopManager.shops.clear()
        DiyCurrency.load()
        ShopManager.load()

        Bukkit.getOnlinePlayers().forEach {
            database.releasePlayerData(it)
        }

        database = if (config.getBoolean("mysql.enable")) MysqlDatabase(config) else SQLiteDatabase()
        Bukkit.getOnlinePlayers().forEach {
            database.loadPlayerData(it)
        }
    }

    override fun onDisable() {
        VaultImpl.flush()
    }

    @SubscribeEvent
    fun join(event: PlayerJoinEvent) {
        val player = event.player
        database.loadPlayerData(player)
    }

    @SubscribeEvent
    fun quit(event: PlayerQuitEvent) {
        val player = event.player
        database.releasePlayerData(player)
    }

}
