package com.github.xbaimiao.shoppro.core.item

import com.github.xbaimiao.shoppro.core.shop.Shop
import com.github.xbaimiao.shoppro.core.shop.ShopManager
import com.github.xbaimiao.shoppro.core.vault.Vault
import com.github.xbaimiao.shoppro.core.vault.VaultImpl
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.permissions.Permission
import taboolib.module.chat.colored

interface Item {

    val key: Char
    val material: Material
    val lore: List<String>
    val name: String
    val vanilla: Boolean
    val commands: List<String>
    val shop: Shop

    val vault: Vault
        get() = VaultImpl

    fun isCommodity(): Boolean
    fun buildItem(): ItemStack
    fun update(player: Player): ItemStack

    fun exeCommands(player: Player, amount: Int) {
        for (a in 1..amount){
            commands.map { it.replace("%player%", player.name) }.forEach { command ->
                if (command.startsWith("[tell] ")) {
                    player.sendMessage(command.substring(7).colored())
                } else if (command.startsWith("[console] ")) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.substring(10))
                } else if (command.startsWith("[player] ")) {
                    Bukkit.dispatchCommand(player, command.substring(9))
                } else if (command.startsWith("[open] ")) {
                    val shop = ShopManager.shops.firstOrNull { it.getName() == command.substring(7) }
                    shop?.open(player)
                } else if (command.startsWith("[op] ")) {
                    Bukkit.dispatchCommand(object : Player by player {
                        override fun isOp() = true
                        override fun hasPermission(p0: Permission) = true
                        override fun hasPermission(p0: String) = true
                    }, command.substring(5))
                } else if (command == "close" || command.startsWith("[close]")) {
                    player.closeInventory()
                }
            }
        }
    }

}