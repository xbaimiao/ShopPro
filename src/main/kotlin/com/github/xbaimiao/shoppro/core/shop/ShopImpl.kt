package com.github.xbaimiao.shoppro.core.shop

import com.github.xbaimiao.shoppro.api.ShopProBuyEvent
import com.github.xbaimiao.shoppro.api.ShopProSellEvent
import com.github.xbaimiao.shoppro.core.item.*
import org.bukkit.Bukkit
import org.bukkit.configuration.Configuration
import org.bukkit.entity.Player
import taboolib.library.xseries.parseToMaterial
import taboolib.module.chat.colored
import taboolib.module.ui.ClickType
import taboolib.module.ui.openMenu
import taboolib.module.ui.type.Basic
import taboolib.platform.util.giveItem
import taboolib.platform.util.hasItem
import taboolib.platform.util.sendLang
import taboolib.platform.util.takeItem

class ShopImpl(private val configuration: Configuration) : Shop() {

    private val slots = configuration.getStringList("slots").map { it.toCharArray().toList() }

    private val items = ArrayList<Item>()

    init {
        val section = configuration.getConfigurationSection("items")!!
        for (key in section.getKeys(false)) {
            if (section.getBoolean("$key.is-commodity", true)) {
                val materialString = section.getString("$key.material")!!
                if (materialString.startsWith("IA")) {
                    items.add(
                        ItemsAdderShopItem(
                            key[0],
                            section.getDouble("$key.price"),
                            materialString,
                            section.getLong("$key.limit"),
                            section.getLong("$key.limit-player"),
                            section.getString("$key.name")!!.colored(),
                            section.getStringList("$key.lore").colored(),
                            section.getBoolean("$key.vanilla", true),
                            section.getStringList("$key.commands")
                        )
                    )
                } else {
                    items.add(
                        VanillaShopItem(
                            key[0],
                            section.getString("$key.material")!!.parseToMaterial(),
                            section.getDouble("$key.price"),
                            section.getLong("$key.limit"),
                            section.getLong("$key.limit-player"),
                            section.getString("$key.name")!!.colored(),
                            section.getStringList("$key.lore").colored(),
                            section.getBoolean("$key.vanilla", true),
                            section.getStringList("$key.commands")
                        )
                    )
                }
            } else {
                items.add(
                    ItemImpl(
                        section.getString("$key.material")!!.parseToMaterial(),
                        section.getStringList("$key.lore").colored(),
                        section.getString("$key.name")!!.colored(),
                        key[0],
                        section.getBoolean("$key.vanilla", true),
                        section.getStringList("$key.commands")
                    )
                )
            }
        }
    }

    override fun getTitle(): String {
        return configuration.getString("title")!!.colored()
    }

    override fun getType(): ShopType {
        return ShopType.formString(configuration.getString("type")!!)
    }

    override fun getName(): String {
        return configuration.getString("name")!!.colored()
    }

    override fun open(player: Player) {
        player.openMenu<Basic>(getTitle()) {
            rows(slots.size)
            slots = ArrayList<List<Char>>().also { it.addAll(this@ShopImpl.slots) }
            onClick {
                it.isCancelled = true
            }
            for (item in this@ShopImpl.items) {
                val itemStack = item.buildItem()
                item.update(player, itemStack)
                set(item.key, itemStack)
                if (item is ShopItem) {
                    if (getType() == ShopType.BUY) {
                        onClick(item.key) { event ->
                            if (event.clickType != ClickType.CLICK) {
                                return@onClick
                            }
                            val amount = when (event.clickEvent().click) {
                                org.bukkit.event.inventory.ClickType.LEFT -> 1
                                org.bukkit.event.inventory.ClickType.RIGHT -> 64
                                else -> 1
                            }
                            buy(amount, item, player)
                            event.currentItem?.let { item.update(player, it) }
                        }
                    }
                    if (getType() == ShopType.SELL) {
                        onClick(item.key) { event ->
                            if (event.clickType != ClickType.CLICK) {
                                return@onClick
                            }
                            val amount = when (event.clickEvent().click) {
                                org.bukkit.event.inventory.ClickType.LEFT -> 1
                                org.bukkit.event.inventory.ClickType.RIGHT -> 64
                                else -> 1
                            }
                            sell(amount, item, player)
                            event.currentItem?.let { item.update(player, it) }
                        }
                    }
                } else {
                    onClick(item.key) {
                        item.exeCommands(player)
                    }
                }
            }
        }
    }

    private fun buy(amount: Int, item: ShopItem, player: Player) {
        if (item.isLimit()){
            // TODO
        }
        if (item.vault.takeMoney(player, item.price * amount)) {
            if (item.vanilla) {
                val vanilla = item.vanillaItem()
                vanilla.amount = amount
                player.giveItem(vanilla)
            }
            Bukkit.getPluginManager().callEvent(ShopProBuyEvent(item, amount, player))
            item.exeCommands(player)
            player.sendLang("buy-item", amount, item.name, item.price * amount)
        } else {
            player.sendLang("not-money")
        }
    }

    private fun sell(amount: Int, item: ShopItem, player: Player) {
        if (item.isLimit()){
            // TODO
        }
        if (player.inventory.hasItem(amount) { item.equal(it) }) {
            player.inventory.takeItem(amount) { item.equal(it) }
            item.vault.giveMoney(player, item.price * amount)
            Bukkit.getPluginManager().callEvent(ShopProSellEvent(item, amount, player))
            player.sendLang("sell-item", amount, item.name, item.price * amount)
        } else {
            player.sendLang("not-item")
        }
    }

}