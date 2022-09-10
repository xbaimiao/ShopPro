package com.github.xbaimiao.shoppro.core.item

import com.github.xbaimiao.shoppro.core.shop.Shop
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import taboolib.library.xseries.XMaterial
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

}