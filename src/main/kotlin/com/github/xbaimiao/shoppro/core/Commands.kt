package com.github.xbaimiao.shoppro.core

import com.github.xbaimiao.shoppro.ShopPro
import com.github.xbaimiao.shoppro.core.shop.ShopManager
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import taboolib.common.platform.command.*
import taboolib.expansion.createHelper
import taboolib.platform.util.onlinePlayers
import taboolib.platform.util.sendLang

@CommandHeader("shoppro", aliases = ["sp"], permissionDefault = PermissionDefault.TRUE)
object Commands {

    @CommandBody
    val main = mainCommand {
        createHelper()
    }

    @CommandBody
    val open = subCommand {
        dynamic("name") {
            suggestion<CommandSender> { _, _ ->
                ShopManager.shops.map { it.getName() }
            }
            execute<Player> { sender, _, argument ->
                val shop = ShopManager.shops.first { it.getName() == argument }
                shop.open(sender)
            }
            dynamic("玩家", permission = "admin") {
                suggestion<CommandSender> { _, _ ->
                    onlinePlayers.map { it.name }
                }
                execute<CommandSender> { _, context, argument ->
                    val player = Bukkit.getPlayerExact(argument)!!
                    val shop = ShopManager.shops.first { it.getName() == context.argument(-1) }
                    shop.open(player)
                }
            }
        }
    }

    @CommandBody(permission = "admin")
    val resetLimit = subCommand {
        execute<CommandSender> { sender, _, _ ->
            ShopPro.database.reset()
            sender.sendLang("reset")
        }
    }

    @CommandBody(permission = "admin")
    val reload = subCommand {
        execute<CommandSender> { sender, _, _ ->
            ShopPro.reload()
            sender.sendLang("reload")
        }
    }

}