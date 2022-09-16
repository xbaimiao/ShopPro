package com.github.xbaimiao.shoppro.core.vault

import com.github.xbaimiao.shoppro.util.Util.format
import org.bukkit.entity.Player
import taboolib.platform.compat.VaultService

object VaultImpl : Currency {

    override fun hasMoney(player: Player, double: Double): Boolean {
        return getMoney(player) >= double.format()
    }

    override fun giveMoney(player: Player, double: Double) {
        VaultService.economy!!.depositPlayer(player, double.format())
    }

    override fun takeMoney(player: Player, double: Double): Boolean {
        if (!hasMoney(player, double.format())) {
            return false
        }
        VaultService.economy!!.withdrawPlayer(player, double.format())
        return true
    }

    override fun getMoney(player: Player): Double {
        return VaultService.economy!!.getBalance(player).format()
    }


}