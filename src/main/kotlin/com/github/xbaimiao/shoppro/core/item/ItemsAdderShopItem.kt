package com.github.xbaimiao.shoppro.core.item

import com.github.xbaimiao.shoppro.core.shop.Shop
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import taboolib.library.xseries.XMaterial
import taboolib.module.nms.getItemTag

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

    private var customPotionColor: Int? = null

    init {
        iaMaterial.split(":").let { strings ->
            material = XMaterial.matchXMaterial(strings[1]).get().parseMaterial()!!
            custom = strings[2].toInt()
            strings.getOrNull(3)?.let { customPotionColor = it.toInt() }
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
        }.also { item ->
            customPotionColor?.let {
                val tag = item.getItemTag()
                tag.putDeep("meta.CustomPotionColor.data", it)
                tag.putDeep("meta.CustomPotionColor.type", "INT")
                tag.saveTo(item)
            }
        }
    }

}