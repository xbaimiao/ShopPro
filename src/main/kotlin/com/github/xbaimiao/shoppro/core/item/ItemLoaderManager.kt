package com.github.xbaimiao.shoppro.core.item

import com.github.xbaimiao.shoppro.core.item.impl.*

class ItemLoaderManager {

    val itemLoaders = ArrayList<ItemLoader>()

    init {
        itemLoaders.add(VanillaShopItem)
        itemLoaders.add(ItemImpl)
        itemLoaders.add(ItemsAdderShopItem)
        itemLoaders.add(HeadShopItem)
        itemLoaders.add(ZapItem)
        itemLoaders.add(MMOItem)
        itemLoaders.add(MythicItem)
    }

    fun getVanillaShop(): ItemLoader = VanillaShopItem.Companion

    fun getItemImpl(): ItemLoader = ItemImpl.Companion

}