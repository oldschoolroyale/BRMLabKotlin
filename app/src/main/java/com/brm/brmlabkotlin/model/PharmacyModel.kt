package com.brm.brmlabkotlin.model

import java.io.Serializable

@SuppressWarnings("serial")
class PharmacyModel: Serializable {
    var address: String? = null
    var id: String? = null
    var lat: String? = null
    var lon: String? = null
    var name: String? = null
    var time_end: String? = null
    var time_start: String? = null
    var type: String? = null
    var visit: String? = null

    constructor()
    constructor(
        address: String?,
        id: String?,
        lat: String?,
        lon: String?,
        name: String?,
        time_end: String?,
        time_start: String?,
        type: String?,
        visit: String?
    ) {
        this.address = address
        this.id = id
        this.lat = lat
        this.lon = lon
        this.name = name
        this.time_end = time_end
        this.time_start = time_start
        this.type = type
        this.visit = visit
    }

}