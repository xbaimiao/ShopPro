package com.github.xbaimiao.shoppro

import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import taboolib.platform.util.isNotAir
import java.text.DecimalFormat

object Util {

    fun Double.format(): Double {
        val df = DecimalFormat("#0.00")
        return df.format(this).toDouble()
    }

    fun Inventory.howManyItems(func: (item: ItemStack) -> Boolean): Int {
        var amount = 0
        for (itemStack in this) {
            if (itemStack.isNotAir()) {
                if (func.invoke(itemStack)) {
                    amount += itemStack.amount
                }
            }
        }
        return amount
    }

}