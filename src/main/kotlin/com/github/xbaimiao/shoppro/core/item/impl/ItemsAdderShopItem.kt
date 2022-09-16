package com.github.xbaimiao.shoppro.core.item.impl

import com.github.xbaimiao.shoppro.core.item.Item
import com.github.xbaimiao.shoppro.core.item.ItemLoader
import com.github.xbaimiao.shoppro.core.item.ShopItem
import com.github.xbaimiao.shoppro.core.shop.Shop
import com.github.xbaimiao.shoppro.core.vault.CurrencyType
import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.inventory.ItemStack
import taboolib.library.xseries.XMaterial
import taboolib.module.chat.colored

class ItemsAdderShopItem(
    override val key: Char,
    override val price: Double,
    iaMaterial: String,
    override val limitServer: Long,
    override val limitPlayer: Long,
    override val name: String,
    override val lore: List<String>,
    override val vanilla: Boolean,
    override val commands: List<String>,
    override val shop: Shop
) : ShopItem() {

    override val material: Material

    val custom: Int

    init {
        iaMaterial.split(":").let { strings ->
            material = XMaterial.matchXMaterial(strings[1]).get().parseMaterial()!!
            custom = strings[2].toInt()
        }
    }

    override fun equal(itemStack: ItemStack): Boolean {
        return itemStack.type == material && itemStack.itemMeta?.customModelData == custom
    }

    override fun vanillaItem(): ItemStack {
        return taboolib.platform.util.buildItem(material) {
            this.customModelData = custom
        }
    }

    override fun buildItem(): ItemStack {
        return taboolib.platform.util.buildItem(material) {
            this.name = this@ItemsAdderShopItem.name
            this.lore.addAll(this@ItemsAdderShopItem.lore)
            this.customModelData = custom
        }
    }

    companion object : ItemLoader {

        override var prefix: String? = "IA"

        override fun formSection(char: Char, section: ConfigurationSection, shop: Shop): Item {
            return ItemsAdderShopItem(
                char,
                section.getDouble("price"),
                section.getString("material")!!,
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