package com.github.xbaimiao.shoppro.core.item

import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.platform.util.modifyLore

abstract class ShopItem : Item {

    abstract val price: Double
    abstract val limitServer: Long
    abstract val limitPlayer: Long

    override fun isCommodity(): Boolean {
        return true
    }

    abstract fun vanillaItem(): ItemStack

    abstract fun equal(itemStack: ItemStack): Boolean

    fun isLimit(): Boolean {
        return limitPlayer != 0L && limitServer != 0L
    }

    override fun update(player: Player, itemStack: ItemStack) {
        val playerLimit = "1"
        val serverLimit = "1"
        val priceAll = "1"
        itemStack.modifyLore {
            val newLore = ArrayList<String>()
            for (line in this) {
                val newLine = line.replace("\${name}", name)
                    .replace("\${price}", price.toString())
                    .replace("\${money}", vault.getMoney(player).toString())
                    .replace("\${price64}", (price * 64).toString())
                    .replace("\${limit}", limitPlayer.toString())
                    .replace("\${priceAll}", priceAll)
                    .replace("\${allLimit}", limitServer.toString())
                    .replace("\${limit-player}", playerLimit)
                    .replace("\${limit-server}", serverLimit)
                newLore.add(PlaceholderAPI.setPlaceholders(player, newLine))
            }
            this.clear()
            this.addAll(newLore)
        }
    }

}