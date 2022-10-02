package com.github.xbaimiao.shoppro.core.item

import com.github.xbaimiao.shoppro.core.item.impl.HeadShopItem
import com.github.xbaimiao.shoppro.core.item.impl.ItemImpl
import com.github.xbaimiao.shoppro.core.item.impl.ItemsAdderShopItem
import com.github.xbaimiao.shoppro.core.item.impl.VanillaShopItem

class ItemLoaderManager {

    val itemLoaders = ArrayList<ItemLoader>()

    init {
        itemLoaders.add(VanillaShopItem)
        itemLoaders.add(ItemImpl)
        itemLoaders.add(ItemsAdderShopItem)
        itemLoaders.add(HeadShopItem)
    }

    fun getVanillaShop(): ItemLoader = VanillaShopItem.Companion

    fun getItemImpl(): ItemLoader = ItemImpl.Companion

}