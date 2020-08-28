package com.brm.brmlabkotlin.model

class DataBaseModel {
    var address : String? = null
    var id : String? = null
    var name : String? = null
    var status : String? = null
    var phone : String? = null
    var specialization: String? = null
    var category: String? = null
    var state: String? = null
    var employee: String? = null
    var owner: String? = null

    constructor()
    constructor(
        address: String?,
        id: String?,
        name: String?,
        status: String?,
        phone: String?,
        specialization: String?,
        category: String?,
        state: String?,
        employee: String?,
        owner: String?
    ) {
        this.address = address
        this.id = id
        this.name = name
        this.status = status
        this.phone = phone
        this.specialization = specialization
        this.category = category
        this.state = state
        this.employee = employee
        this.owner = owner
    }


}
