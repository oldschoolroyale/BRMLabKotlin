package com.brm.brmlabkotlin.model

class StockModel {
    var name: String? = null
    var quantity: Int? = null
    var full: Double? = null
    var half: Double? = null
    var semi: Double? = null
    var stock: Int? = null

    constructor()
    constructor(name: String?, quantity: Int?, full: Double?, half: Double?, semi: Double?,
    stock: Int?) {
        this.name = name
        this.quantity = quantity
        this.full = full
        this.half = half
        this.semi = semi
        this.stock = stock
    }

}