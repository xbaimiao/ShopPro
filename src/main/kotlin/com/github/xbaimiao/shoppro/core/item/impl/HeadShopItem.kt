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
import taboolib.library.xseries.parseToMaterial
import taboolib.module.chat.colored
import taboolib.platform.util.ItemBuilder
import java.util.*

class HeadShopItem(
    override val key: Char,
    headString: String,
    override val price: Double,
    override val limitServer: Long,
    override val limitPlayer: Long,
    override val name: String,
    override val lore: List<String>,
    override val vanilla: Boolean,
    override val commands: List<String>,
    override val shop: Shop,
    val item: Material
) : ShopItem() {

    override val material: Material = XMaterial.PLAYER_HEAD.parseMaterial()!!

    val head: String

    init {
        head = headString.substring(5)
    }

    override fun vanillaItem(): ItemStack {
        return taboolib.platform.util.buildItem(material) {
            skullTexture = ItemBuilder.SkullTexture(head, UUID.randomUUID())
        }
    }

    override fun equal(itemStack: ItemStack): Boolean {
        return itemStack.itemMeta?.hasLore() == false && itemStack.type == item
    }

    override fun buildItem(): ItemStack {
        return taboolib.platform.util.buildItem(material) {
            this.name = this@HeadShopItem.name
            this.lore.addAll(this@HeadShopItem.lore)
            skullTexture = ItemBuilder.SkullTexture(head, UUID.randomUUID())
        }
    }

    companion object : ItemLoader {

        override var prefix: String? = "HEAD"

        override fun formSection(char: Char, section: ConfigurationSection, shop: Shop): Item {
            return HeadShopItem(
                char,
                section.getString("material")!!,
                section.getDouble("price"),
                section.getLong("limit"),
                section.getLong("limit-player"),
                section.getString("name")!!.colored(),
                section.getStringList("lore").colored(),
                section.getBoolean("vanilla", true),
                section.getStringList("commands"),
                shop,
                section.getString("item")!!.parseToMaterial()
            ).also {
                section.getString("currency")?.let { currency ->
                    it.currency = CurrencyType.formString(currency).func.invoke(currency)
                }
            }
        }

    }

}