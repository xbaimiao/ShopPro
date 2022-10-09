package com.github.xbaimiao.shoppro.core.item

import com.github.xbaimiao.shoppro.ShopPro
import com.github.xbaimiao.shoppro.core.shop.Shop
import com.github.xbaimiao.shoppro.core.vault.Currency
import com.github.xbaimiao.shoppro.core.vault.VaultImpl
import com.github.xbaimiao.shoppro.util.Util.format
import com.github.xbaimiao.shoppro.util.Util.howManyItems
import com.github.xbaimiao.shoppro.util.Util.replacePapi
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

    var currency: Currency = VaultImpl

    abstract fun vanillaItem(): ItemStack

    abstract fun equal(itemStack: ItemStack): Boolean

    fun isLimit(): Boolean {
        return limitPlayer != 0L && limitServer != 0L
    }

    override fun update(player: Player): ItemStack {
        val playerLimit = if (shop.getType() == Shop.ShopType.BUY) {
            ShopPro.database.getPlayerAlreadyData(player, this).buy
        } else {
            ShopPro.database.getPlayerAlreadyData(player, this).sell
        }
        val serverLimit = if (shop.getType() == Shop.ShopType.BUY) {
            ShopPro.database.getServerAlreadyData(this).buy
        } else {
            ShopPro.database.getServerAlreadyData(this).sell
        }
        val item = buildItem().modifyLore {
            val newLore = ArrayList<String>()
            for (line in this) {
                var newLine = line.replace("\${name}", name)
                    .replace("\${price}", price.toString())
                    .replace("\${money}", currency.getMoney(player).toString())
                    .replace("\${price64}", (price * 64).toString())
                    .replace("\${limit}", limitPlayer.toString())
                    .replace("\${allLimit}", limitServer.toString())
                    .replace("\${limit-player}", (limitPlayer - playerLimit).toString())
                    .replace("\${limit-server}", (limitServer - serverLimit).toString())
                if (shop.getType() == Shop.ShopType.SELL) {
                    val priceAll = (player.inventory.howManyItems {
                        this@ShopItem.equal(it)
                    } * price).format()
                    newLine = newLine.replace("\${priceAll}", priceAll.toString())
                }
                newLore.add(newLine.replacePapi(player))
            }
            this.clear()
            this.addAll(newLore)
        }
        return item
    }

}