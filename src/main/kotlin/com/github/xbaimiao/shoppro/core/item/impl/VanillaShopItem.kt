package com.github.xbaimiao.shoppro.core.item.impl

import com.github.xbaimiao.shoppro.core.item.Item
import com.github.xbaimiao.shoppro.core.item.ItemLoader
import com.github.xbaimiao.shoppro.core.item.ShopItem
import com.github.xbaimiao.shoppro.core.shop.Shop
import com.github.xbaimiao.shoppro.core.vault.CurrencyType
import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.inventory.ItemStack
import taboolib.library.xseries.parseToMaterial
import taboolib.module.chat.colored

class VanillaShopItem(
    override val key: Char,
    override val material: Material,
    override val price: Double,
    override val limitServer: Long,
    override val limitPlayer: Long,
    override val name: String,
    override val lore: List<String>,
    override val vanilla: Boolean,
    override val commands: List<String>,
    override val shop: Shop
) : ShopItem() {

    override fun vanillaItem(): ItemStack {
        return taboolib.platform.util.buildItem(material)
    }

    override fun equal(itemStack: ItemStack): Boolean {
        return itemStack.itemMeta?.hasLore() == false && itemStack.type == material
    }

    override fun buildItem(): ItemStack {
        return taboolib.platform.util.buildItem(material) {
            this.name = this@VanillaShopItem.name
            this.lore.addAll(this@VanillaShopItem.lore)
        }
    }

    companion object : ItemLoader {

        override var prefix: String? = null

        override fun formSection(char: Char, section: ConfigurationSection, shop: Shop): Item {
            return VanillaShopItem(
                char,
                section.getString("material")!!.parseToMaterial(),
                section.getDouble("price"),
                section.getLong("limit"),
                section.getLong("limit-player"),
                section.getString("name")!!.colored(),
                section.getStringList("lore").colored(),
                section.getBoolean("vanilla", true),
                section.getStringList("commands"),
                shop
            ).also {
                section.getString("currency")?.let { currency ->
                    it.currency = CurrencyType.formString(currency).func.invoke(currency)
                }
            }
        }
    }

}