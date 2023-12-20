package com.github.xbaimiao.shoppro.core.database

import com.github.xbaimiao.shoppro.core.item.Item
import com.github.xbaimiao.shoppro.core.item.ShopItem
import com.github.xbaimiao.shoppro.core.item.impl.ItemsAdderShopItem
import com.github.xbaimiao.shoppro.core.shop.ShopManager
import org.bukkit.entity.Player
import taboolib.module.database.Host
import taboolib.module.database.Table
import taboolib.module.database.use
import java.sql.Connection
import javax.sql.DataSource

abstract class SqlDatabase : Database {

    abstract val host: Host<*>
    abstract val playerTable: Table<*, *>
    abstract val serverTable: Table<*, *>
    abstract val buyAmountTable: Table<*, *>
    abstract val dataSource: DataSource

    val serverTableName = "server"
    val playerTableName = "player"
    val buyAmountTableName = "buyAmount"

    val itemKeyLine = "item-key"
    val itemMaterialLine = "item-material"
    val itemCustomLine = "item-custom"
    val dataLine = "data"

    // ServerTable 无 User
    val playerLine = "user"

    override fun reset() {
        serverTable.workspace(dataSource) {
            executeUpdate("DELETE FROM $serverTableName;").run()
        }.run()
        playerTable.workspace(dataSource) {
            executeUpdate("DELETE FROM $playerTableName;").run()
        }.run()
    }

    override fun getServerAlreadyData(item: Item): LimitData {
        return serverTable.workspace(dataSource) {
            select {
                where {
                    itemKeyLine eq item.key.toString()
                    itemMaterialLine eq item.material.toString()
                    if (item is ItemsAdderShopItem) {
                        itemCustomLine eq item.custom
                    }
                }
            }
        }.firstOrNull {
            LimitData.formString(this.getString(dataLine))
        } ?: LimitData.ofNull()
    }

    override fun getPlayerAlreadyData(player: Player, item: Item): LimitData {
        return playerTable.workspace(dataSource) {
            select {
                where {
                    playerLine eq player.uniqueId.toString()
                    itemKeyLine eq item.key.toString()
                    itemMaterialLine eq item.material.toString()
                    if (item is ItemsAdderShopItem) {
                        itemCustomLine eq item.custom
                    }
                }
            }
        }.firstOrNull {
            LimitData.formString(this.getString(dataLine))
        } ?: LimitData.ofNull()
    }

    override fun addAmount(item: Item, player: Player, amount: LimitData) {
        setPlayerAlreadyData(player, item, amount.add(getPlayerAlreadyData(player, item)))
        setServerAlreadyData(item, amount.add(getServerAlreadyData(item)))
    }

    override fun setServerAlreadyData(item: Item, amount: LimitData) {
        if (serverTable.workspace(dataSource) {
                select {
                    where {
                        itemKeyLine eq item.key.toString()
                        itemMaterialLine eq item.material.toString()
                        if (item is ItemsAdderShopItem) {
                            itemCustomLine eq item.custom
                        }
                    }
                }
            }.find()) {
            serverTable.workspace(dataSource) {
                update {
                    where {
                        itemKeyLine eq item.key.toString()
                        itemMaterialLine eq item.material.toString()
                        if (item is ItemsAdderShopItem) {
                            itemCustomLine eq item.custom
                        }
                    }
                    set(dataLine, amount.toString())
                }
            }.run()
        } else {
            val custom = if (item is ItemsAdderShopItem) item.custom else 0
            serverTable.workspace(dataSource) {
                insert {
                    value(item.key.toString(), item.material.toString(), custom, amount.toString())
                }
            }.run()
        }
    }

    override fun addTradeAmount(item: Item, amount: Int) {
        dataSource.connection.use { connection: Connection ->
            var resultAmount: Int = amount

            val statement =
                connection.prepareStatement("SELECT `$dataLine` FROM `$buyAmountTableName` WHERE `$itemKeyLine` = ?;")
            statement.setString(1, item.identifier)

            val resultSet = statement.executeQuery()
            if (resultSet.next()) {
                val data = resultSet.getInt(dataLine)
                connection.prepareStatement("UPDATE `$buyAmountTableName` SET `$dataLine` = ? WHERE `$itemKeyLine` = ?;")
                    .use {
                        resultAmount = data + amount
                        it.setInt(1, resultAmount)
                        it.setString(2, item.identifier)
                        it.executeUpdate()
                    }

            } else {
                connection.prepareStatement("INSERT INTO `$buyAmountTableName` VALUES (?,?);").use {
                    it.setString(1, item.identifier)
                    it.setInt(2, amount)
                    it.executeUpdate()
                }
            }

            statement.close()
        }
    }

    override fun getTradeAmount(item: Item): Int {
        return dataSource.connection.use { connection: Connection ->
            connection.prepareStatement("SELECT `$dataLine` FROM `$buyAmountTableName` WHERE `$itemKeyLine` = ?;")
                .use { statement ->
                    statement.setString(1, item.identifier)
                    val resultSet = statement.executeQuery()
                    if (resultSet.next()) {
                        resultSet.getInt(dataLine)
                    } else {
                        0
                    }
                }
        }
    }

    override fun resetTradeAmount() {
        playerTable.workspace(dataSource) {
            executeUpdate("DELETE FROM $buyAmountTableName;").run()
        }.run()
        for (shop in ShopManager.shops) {
            for (item in shop.getAllItem()) {
                if (item is ShopItem) {
                    item.price = item.originalPrice
                }
            }
        }
    }

    override fun setPlayerAlreadyData(player: Player, item: Item, amount: LimitData) {
        if (playerTable.workspace(dataSource) {
                select {
                    where {
                        playerLine eq player.uniqueId.toString()
                        itemKeyLine eq item.key.toString()
                        itemMaterialLine eq item.material.toString()
                        if (item is ItemsAdderShopItem) {
                            itemCustomLine eq item.custom
                        }
                    }
                }
            }.find()) {
            playerTable.workspace(dataSource) {
                update {
                    where {
                        playerLine eq player.uniqueId.toString()
                        itemKeyLine eq item.key.toString()
                        itemMaterialLine eq item.material.toString()
                        if (item is ItemsAdderShopItem) {
                            itemCustomLine eq item.custom
                        }
                    }
                    set(dataLine, amount.toString())
                }
            }.run()
        } else {
            val custom = if (item is ItemsAdderShopItem) item.custom else 0
            playerTable.workspace(dataSource) {
                insert {
                    value(
                        item.key.toString(),
                        item.material.toString(),
                        custom,
                        amount.toString(),
                        player.uniqueId.toString()
                    )
                }
            }.run()
        }
    }

}