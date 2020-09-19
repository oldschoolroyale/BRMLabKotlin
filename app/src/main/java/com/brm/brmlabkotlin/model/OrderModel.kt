package com.brm.brmlabkotlin.model

class OrderModel {
    var name: String? = null
    var quantity: Int? = null

    constructor()
    constructor(name: String?, quantity: Int?) {
        this.name = name
        this.quantity = quantity
    }

}