package com.github.xbaimiao.shoppro.core.item

import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class VanillaShopItem(
    override val key: Char,
    override val material: Material,
    override val price: Double,
    override val limitServer: Long,
    override val limitPlayer: Long,
    override val name: String,
    override val lore: List<String>,
    override val vanilla: Boolean,
    override val commands: List<String>
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

}