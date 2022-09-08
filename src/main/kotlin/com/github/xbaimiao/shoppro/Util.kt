package com.github.xbaimiao.shoppro

import java.text.DecimalFormat


object Util {

    fun Double.format(): Double {
        val df = DecimalFormat("#0.00")
        return df.format(this).toDouble()
    }

}