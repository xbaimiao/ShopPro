package com.github.xbaimiao.shoppro

import com.github.xbaimiao.shoppro.core.database.Database
import com.github.xbaimiao.shoppro.core.shop.ShopManager
import taboolib.common.platform.Plugin
import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration

object ShopPro : Plugin() {

    @Config(value = "config.yml")
    lateinit var config: Configuration
        private set

    lateinit var database: Database

    override fun onEnable() {
        ShopManager.load()
    }

    fun reload() {
        ShopManager.shops.clear()
        ShopManager.load()
    }

}