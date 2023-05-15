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
    }

    fun getVanillaShop(): ItemLoader = VanillaShopItem.Companion

    fun getItemImpl(): ItemLoader = ItemImpl.Companion

}