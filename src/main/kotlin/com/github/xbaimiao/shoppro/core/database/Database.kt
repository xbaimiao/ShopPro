package com.github.xbaimiao.shoppro.core.database

import com.github.xbaimiao.shoppro.core.item.Item
import org.bukkit.entity.Player

interface Database {

    /**
     * 获取该物品该玩家今日已购买多少次了
     */
    fun getPlayerLimit(player: Player, item: Item): Long

    /**
     * 设置该物品该玩家今日已购买多少次了
     */
    fun setPlayerLimit(player: Player, item: Item, amount: Long)

    /**
     * 获取该物品今日已购买多少次了
     */
    fun getServerLimit(item: Item)

    /**
     * 设置该物品今日已购买多少次了
     */
    fun setServerLimit(item: Item, amount: Long)

}