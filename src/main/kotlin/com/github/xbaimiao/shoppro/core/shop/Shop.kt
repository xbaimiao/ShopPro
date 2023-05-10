package com.github.xbaimiao.shoppro.core.shop

import org.bukkit.entity.Player

abstract class Shop {

    enum class ShopType(val string: String) {
        BUY("buy"), SELL("sell");

        companion object {
            fun formString(string: String): ShopType {
                return ShopType.values().first { it.string == string }
            }
        }

    }

    abstract fun getTitle(player: Player): String

    abstract fun getType(): ShopType

    abstract fun getName(): String

    abstract fun sellAll(player: Player)

    abstract fun open(player: Player)

}