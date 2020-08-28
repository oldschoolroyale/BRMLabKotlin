package com.brm.brmlabkotlin.model

class NoteModel {
    var name: String? = null
    var address: String? = null
    var visit: String? = null
    var type: String? = null

    constructor()

    constructor(name: String?, address: String?, visit: String?, type: String?) {
        this.name = name
        this.address = address
        this.visit = visit
        this.type = type
    }


}