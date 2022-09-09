package com.github.xbaimiao.shoppro.core.item

import com.github.xbaimiao.shoppro.core.shop.Shop
import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.platform.util.modifyLore

open class ItemImpl(
    override val material: Material, override val lore: List<String>, override val name: String,
    override val key: Char, override val vanilla: Boolean, override val commands: List<String>,
    override val shop: Shop
) : Item {

    override fun isCommodity(): Boolean {
        return false
    }

    override fun buildItem(): ItemStack {
        return taboolib.platform.util.buildItem(material) {
            this.name = this@ItemImpl.name
            this.lore.addAll(this@ItemImpl.lore)
        }
    }

    override fun update(player: Player): ItemStack {
        val item = buildItem().modifyLore {
            val newLore = ArrayList<String>()
            for (line in this) {
                newLore.add(PlaceholderAPI.setPlaceholders(player, line))
            }
            this.clear()
            this.addAll(newLore)
        }
        return item
    }

}