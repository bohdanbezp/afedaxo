package com.afedaxo.domain.model

import java.math.BigDecimal

data class Dish(val name: String = "", val price: BigDecimal, val currency: String) {
    fun toPriceRepr() : String {
        return "$currency $price"
    }
}