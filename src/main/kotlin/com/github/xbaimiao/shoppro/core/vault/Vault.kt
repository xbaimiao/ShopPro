package com.github.xbaimiao.shoppro.core.vault

import org.bukkit.entity.Player

interface Vault {

    fun hasMoney(player: Player, double: Double): Boolean

    fun giveMoney(player: Player, double: Double)

    fun takeMoney(player: Player, double: Double): Boolean

    fun getMoney(player: Player): Double

}